package com.competitors.webshop.automation.integration.vector_db.qdrant.dto;

import com.competitors.webshop.automation.model.Payload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchQdrantResponse(
        List<QdrantResult> result
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record QdrantResult(
            String id,
            float score,
            Payload payload) {
    }
}
