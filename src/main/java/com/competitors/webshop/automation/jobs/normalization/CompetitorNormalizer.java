package com.competitors.webshop.automation.jobs.normalization;

import com.competitors.webshop.automation.integration.ollama.EmbeddingService;
import com.competitors.webshop.automation.integration.vector_db.VectorDbRepository;
import com.competitors.webshop.automation.integration.vector_db.qdrant.QdrantEntity;
import com.competitors.webshop.automation.model.CompetitorProduct;
import com.competitors.webshop.automation.model.MatchStatus;
import com.competitors.webshop.automation.model.Payload;
import com.competitors.webshop.automation.modules.normalization.NormalizationApi;
import com.competitors.webshop.automation.modules.normalization.dto.NormalizationInput;
import com.competitors.webshop.automation.modules.normalization.dto.NormalizedProduct;
import com.competitors.webshop.automation.repository.CompetitorProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompetitorNormalizer {

    @Value("${products.collection}")
    private String COLLECTION;

    private static final int BATCH_SIZE = 100;

    private final CompetitorProductRepository competitorRepository;
    private final NormalizationApi normalizationApi;
    private final VectorDbRepository<QdrantEntity> vectorDbRepository;
    private final EmbeddingService embeddingService;

    public void processAll() {
        Pageable pageable = PageRequest.of(0, BATCH_SIZE,
                Sort.by("createdAt").ascending());

        while (true) {
            List<CompetitorProduct> batch = competitorRepository
                    .findAllByStatus(MatchStatus.NEW, pageable);

            if (batch.isEmpty()) {
                log.info("No more products to process");
                break;
            }
            log.info("Processing batch size={}", batch.size());
            for (CompetitorProduct product : batch) {
                try {
                    process(product);
                } catch (Exception e) {
                    log.error("Failed id={}", product.getId(), e);
                }
            }
        }
    }

    private void process(CompetitorProduct product) {
        NormalizedProduct result = normalizationApi.normalize(new NormalizationInput(product.getName()));

        Payload payload = Payload.builder()
                .productName(result.productName())
                .normalizedProduct(result.normalizedName())
                .build();

        float[] embedding = embeddingService.call(payload.getNormalizedProduct());

        QdrantEntity entity = QdrantEntity.builder()
                .id(product.getId())
                .vector(embedding)
                .payload(payload)
                .build();

        vectorDbRepository.persist(COLLECTION, entity);

        competitorRepository.updateNormalized(
                product.getId(),
                result.normalizedName(),
                MatchStatus.PENDING
        );
    }
}