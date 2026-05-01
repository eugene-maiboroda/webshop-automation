package com.competitors.webshop.automation.integration.vector_db;

import java.util.List;

public interface VectorDbRepository<T> {

    void persist(String collectionName, T body);

    List<SearchVectorResult<T>> search(String collectionName, float[] vector, int size);
}
