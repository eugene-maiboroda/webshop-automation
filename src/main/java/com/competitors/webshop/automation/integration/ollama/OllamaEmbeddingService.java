package com.competitors.webshop.automation.integration.ollama;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OllamaEmbeddingService implements EmbeddingService {

    private final OllamaEmbeddingModel embeddingModel;

    @Override
    public float[] call(String payload) {
        EmbeddingRequest embeddingRequest = new EmbeddingRequest(List.of(payload), null);
        EmbeddingResponse response = embeddingModel.call(embeddingRequest);
    return response.getResult().getOutput();
    }
}
