package com.competitors.webshop.automation.integration.vector_db.qdrant;

import com.competitors.webshop.automation.integration.ok_http.okhttp.OkHttpService;
import com.competitors.webshop.automation.integration.ok_http.okhttp.OkResponseBody;
import com.competitors.webshop.automation.integration.vector_db.SearchVectorResult;
import com.competitors.webshop.automation.integration.vector_db.VectorDbRepository;
import com.competitors.webshop.automation.integration.vector_db.qdrant.dto.SaveQdrantCollectionRequest;
import com.competitors.webshop.automation.integration.vector_db.qdrant.dto.SaveQdrantPointsRequest;
import com.competitors.webshop.automation.integration.vector_db.qdrant.dto.SearchQdrantPointsRequest;
import com.competitors.webshop.automation.integration.vector_db.qdrant.dto.SearchQdrantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class QdrantDbRepository implements VectorDbRepository<QdrantEntity> {

    @Value("${vector-bd.qdrant.url}")
    private String QDRANT_URL;

    private final OkHttpService okHttpService;

    private final ObjectMapper objectMapper;

    @Override
    public void persist(String collectionName, QdrantEntity body) {
        createCollectionIfNotExists(collectionName);


        SaveQdrantPointsRequest qdrantPointsRequest = SaveQdrantPointsRequest.builder().points(List.of(body)).build();
        String jsonBody = objectMapper.writeValueAsString(qdrantPointsRequest);

        Request request = new Request.Builder()
                .url(QDRANT_URL + "/collections/" + collectionName + "/points")
                .put(RequestBody.create(jsonBody, MediaType.get(APPLICATION_JSON_VALUE)))
                .build();

        OkResponseBody response = okHttpService.execute(request);
        if(!response.isSuccessful()) {
            log.warn("Failed to persist entity [{}] in collection [{}]: {}",
                    body.getId(), collectionName, response.body());
            // далі — retry, dead letter queue, метрика, що завгодно
        }
    }

    @Override
    public List<SearchVectorResult<QdrantEntity>> search(String collectionName, float[] vector, int limit) {
        if (createCollectionIfNotExists(collectionName)) {
            return List.of();
        }

        SearchQdrantResponse searchQdrantResponse = searchPoints(collectionName, vector, limit);

        return searchQdrantResponse.result().stream()
                .map(r -> new SearchVectorResult<QdrantEntity>(
                        QdrantEntity.builder().id(r.id()).payload(r.payload()).build(),
                        r.score()))
                .toList();
    }

    private SearchQdrantResponse searchPoints(String collectionName, float[] vector, int limit) {
        SearchQdrantPointsRequest searchQdrantPointsRequest = SearchQdrantPointsRequest.builder().vector(vector).limit(limit).withPayload(true).build();
        String jsonBody = objectMapper.writeValueAsString(searchQdrantPointsRequest);

        Request request = new Request.Builder()
                .url(QDRANT_URL + "/collections/" + collectionName + "/points/search")
                .post(RequestBody.create(jsonBody, MediaType.get(APPLICATION_JSON_VALUE)))
                .build();

        OkResponseBody response = okHttpService.execute(request);
        if (!response.isSuccessful()) {
            log.warn("Search failed for collection [{}]: {}", collectionName, response.body());
        }

        return objectMapper.readValue(
                response.body(),
                SearchQdrantResponse.class);
    }

    private void createCollection(String name) {
        SaveQdrantCollectionRequest saveQdrantCollectionRequest = SaveQdrantCollectionRequest.builder()
                .vectors(SaveQdrantCollectionRequest.Vectors.builder().size(768).distance("Cosine").build())
                .build();

        Request request = new Request.Builder()
                .url(QDRANT_URL + "/collections/" + name)
                .put(RequestBody.create(objectMapper.writeValueAsString(saveQdrantCollectionRequest), MediaType.get(APPLICATION_JSON_VALUE)))
                .build();

        okHttpService.execute(request);
    }

    private boolean collectionExists(String name) {
        Request request = new Request.Builder()
                .url(QDRANT_URL + "/collections/" + name)
                .get()
                .build();
        return okHttpService.execute(request).isSuccessful();
    }

    private boolean createCollectionIfNotExists(String collectionName) {
        boolean collectionExists = collectionExists(collectionName);
        if (!collectionExists) {
            createCollection(collectionName);
            return true;
        }

        return false;
    }
}