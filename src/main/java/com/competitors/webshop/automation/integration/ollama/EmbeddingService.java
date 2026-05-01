package com.competitors.webshop.automation.integration.ollama;

public interface EmbeddingService {

    float[] call(String payload);

}
