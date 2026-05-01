package com.competitors.webshop.automation.integration.vector_db.qdrant.dto;

import com.competitors.webshop.automation.integration.vector_db.qdrant.QdrantEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record SaveQdrantPointsRequest(List<QdrantEntity> points) {
}
