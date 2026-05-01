package com.competitors.webshop.automation.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Product {

    private float[] vector;

    private float score;

    private Payload payload;

}