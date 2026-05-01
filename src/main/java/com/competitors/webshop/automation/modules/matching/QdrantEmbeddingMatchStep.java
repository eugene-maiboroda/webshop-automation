package com.competitors.webshop.automation.modules.matching;

import com.competitors.webshop.automation.integration.ollama.EmbeddingService;
import com.competitors.webshop.automation.integration.vector_db.SearchVectorResult;
import com.competitors.webshop.automation.integration.vector_db.qdrant.QdrantDbRepository;
import com.competitors.webshop.automation.integration.vector_db.qdrant.QdrantEntity;
import com.competitors.webshop.automation.model.CompetitorProduct;
import com.competitors.webshop.automation.modules.matching.dto.Candidate;
import com.competitors.webshop.automation.modules.matching.dto.MatchInput;
import com.competitors.webshop.automation.modules.matching.dto.MatchResultDto;
import com.competitors.webshop.automation.repository.CompetitorProductRepository;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QdrantEmbeddingMatchStep implements EmbeddingMatchStep {

    @Value("${products.collection}")
    private String COLLECTION;

    @Value("${products.search.limit}")
    private int LIMIT;

    private final QdrantDbRepository qdrantDbRepository;
    private final EmbeddingService embeddingService;
    private final CompetitorProductRepository competitorProductRepository;
    private final float threshold;

    public QdrantEmbeddingMatchStep(
            QdrantDbRepository qdrantDbRepository,
            EmbeddingService embeddingService,
            CompetitorProductRepository competitorProductRepository,
            float threshold) {
        this.qdrantDbRepository = qdrantDbRepository;
        this.embeddingService = embeddingService;
        this.competitorProductRepository = competitorProductRepository;
        this.threshold = threshold;
    }

    @Override
    public MatchResultDto match(MatchInput input) {
        float[] embedding = embeddingService.call(input.normalizedName());
        List<SearchVectorResult<QdrantEntity>> results = qdrantDbRepository.search(COLLECTION, embedding, LIMIT);

        if (results.isEmpty()) return MatchResultDto.notMatched();

        SearchVectorResult<QdrantEntity> top = results.getFirst();

        if (top.score() >= threshold) {
            return MatchResultDto.matched(top.entity().getPayload().getProductName(), top.score());
        }

        List<SearchVectorResult<QdrantEntity>> uncertain = results.stream()
                .filter(r -> r.score() >= 0.65f)
                .toList();

        if (uncertain.isEmpty()) return MatchResultDto.notMatched();

        List<String> names = uncertain.stream()
                .map(r -> r.entity().getPayload().getProductName())
                .toList();

        Map<String, CompetitorProduct> byName = competitorProductRepository.findByNameIn(names)
                .stream()
                .collect(Collectors.toMap(CompetitorProduct::getName, e -> e));

        List<Candidate> candidates = new ArrayList<>();
        for (int i = 0; i < uncertain.size(); i++) {
            String name = uncertain.get(i).entity().getPayload().getProductName();
            CompetitorProduct entity = byName.get(name);
            if (entity != null) {
                candidates.add(new Candidate(i + 1, entity.getName(), entity.getPrice(), entity.getCurrency(), uncertain.get(i).score()));
            }
        }

        if (candidates.isEmpty()) return MatchResultDto.notMatched();

        return MatchResultDto.uncertain(candidates);
    }
}