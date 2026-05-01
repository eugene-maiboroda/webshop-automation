package com.competitors.webshop.automation.api.mapper;

import com.competitors.webshop.automation.config.MapStructConfig;
import com.competitors.webshop.automation.integration.vector_db.qdrant.QdrantEntity;
import com.competitors.webshop.automation.model.Product;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface ProductMapper {

    QdrantEntity productToQdrantEntity(Product product);

    Product qdrantEntityToProduct(QdrantEntity entity);

}