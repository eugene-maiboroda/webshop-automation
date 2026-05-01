package com.competitors.webshop.automation.api;

import com.competitors.webshop.automation.api.mapper.ProductMapper;
import com.competitors.webshop.automation.integration.ollama.EmbeddingService;
import com.competitors.webshop.automation.integration.vector_db.VectorDbRepository;
import com.competitors.webshop.automation.integration.vector_db.qdrant.QdrantEntity;
import com.competitors.webshop.automation.model.Payload;
import com.competitors.webshop.automation.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final EmbeddingService embeddingService;
    private final VectorDbRepository<QdrantEntity> vectorDbRepository;
    private final ProductMapper productMapper;

    @Value("${products.collection}")
    private String COLLECTION;

    @Value("${products.search.limit}")
    private int LIMIT;

//    public void saveCompetitorProduct(String productName) {
//        float[] embedding = embeddingService.call(productName);
//        Payload payload = Payload.builder().productName(productName).build();
//        QdrantEntity entity = QdrantEntity.builder().vector(embedding).payload(payload).build();
//        vectorDbRepository.persist(COLLECTION, entity);
//    }

    public List<Product> findSimilar(String myProductName) {
        float[] embedding = embeddingService.call(myProductName);
        return vectorDbRepository.search(COLLECTION, embedding, LIMIT).stream()
                .map(r -> {
                    Product product = productMapper.qdrantEntityToProduct(r.entity());
                    product.setScore(r.score());
                    return product;
                })
                .toList();
    }
}
