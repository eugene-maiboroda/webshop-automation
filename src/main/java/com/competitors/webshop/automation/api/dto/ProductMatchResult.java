package com.competitors.webshop.automation.api.dto;

public record ProductMatchResult(
        String competitorProductName,
        float score
) {
}