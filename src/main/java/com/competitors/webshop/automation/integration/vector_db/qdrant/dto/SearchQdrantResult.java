package com.competitors.webshop.automation.integration.vector_db.qdrant.dto;

public record SearchQdrantResult(
        String id,
        float score

) {
}
