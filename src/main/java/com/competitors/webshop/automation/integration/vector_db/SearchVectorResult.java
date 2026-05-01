package com.competitors.webshop.automation.integration.vector_db;

public record SearchVectorResult<T>(
        T entity,
        float score
) {
}