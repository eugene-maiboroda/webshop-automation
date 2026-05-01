package com.competitors.webshop.automation.config;

import com.competitors.webshop.automation.integration.openai.AiConfirmator;
import com.competitors.webshop.automation.modules.normalization.NormalizationApi;
import com.competitors.webshop.automation.modules.normalization.NormalizationService;
import com.competitors.webshop.automation.modules.normalization.StopWordFilter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import com.competitors.webshop.automation.integration.ollama.EmbeddingService;
import com.competitors.webshop.automation.integration.vector_db.qdrant.QdrantDbRepository;
import com.competitors.webshop.automation.modules.matching.EmbeddingMatchStep;
import com.competitors.webshop.automation.modules.matching.ExactMatchStep;
import com.competitors.webshop.automation.modules.matching.QdrantEmbeddingMatchStep;
import com.competitors.webshop.automation.modules.matching.GptConfirmStep;
import com.competitors.webshop.automation.modules.matching.MatchingApi;
import com.competitors.webshop.automation.repository.CompetitorProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.create(openAiChatModel);
    }

    @Bean
    public ExactMatchStep exactMatchStep(CompetitorProductRepository competitorProductRepository) {
        return new ExactMatchStep(competitorProductRepository);
    }

    @Bean
    public EmbeddingMatchStep embeddingMatchStep(QdrantDbRepository qdrantDbRepository, EmbeddingService embeddingService, CompetitorProductRepository competitorProductRepository, @Value("${products.matching.threshold:0.88}") float threshold) {
        return new QdrantEmbeddingMatchStep(qdrantDbRepository, embeddingService, competitorProductRepository, threshold);
    }

    @Bean
    public GptConfirmStep gptConfirmStep(AiConfirmator aiConfirmator) {
        return new GptConfirmStep(aiConfirmator);
    }

    @Bean
    public MatchingApi matchingApi(ExactMatchStep exactMatchStep, EmbeddingMatchStep embeddingMatchStep, GptConfirmStep gptConfirmStep) {
        return new MatchingApi(exactMatchStep, embeddingMatchStep, gptConfirmStep);
    }
    @Bean
    public NormalizationApi normalizationApi(NormalizationService normalizationService) {
        return new NormalizationApi(normalizationService);
    }
    @Bean
    public NormalizationService normalizationService(StopWordFilter stopWordFilter) {
        return new NormalizationService(stopWordFilter);
    }
    @Bean
    public StopWordFilter stopWordFilter() {
        return new StopWordFilter();
    }
}

