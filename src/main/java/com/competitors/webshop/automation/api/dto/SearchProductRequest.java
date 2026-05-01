package com.competitors.webshop.automation.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SearchProductRequest(
        @NotBlank(message = "Product name is required.")
        String productName
) {
}