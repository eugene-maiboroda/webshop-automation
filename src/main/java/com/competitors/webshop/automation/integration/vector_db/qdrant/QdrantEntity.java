package com.competitors.webshop.automation.integration.vector_db.qdrant;

import com.competitors.webshop.automation.model.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QdrantEntity {

    private String id;
    private float[] vector;
    private Payload payload;

}
