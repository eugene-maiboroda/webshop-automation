package com.competitors.webshop.automation.integration.vector_db.qdrant.dto;

import lombok.Builder;

@Builder
public record SaveQdrantCollectionRequest(
        Vectors vectors
) {
    @Builder
    public record Vectors (
            int size,
            String distance
    ) {}
}
