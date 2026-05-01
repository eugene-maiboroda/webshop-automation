package com.competitors.webshop.automation.integration.vector_db.qdrant.dto;

import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;

@Builder
public record SearchQdrantPointsRequest(
        float[] vector,
        int limit,
        @JsonProperty("with_payload") boolean withPayload
) {
}
