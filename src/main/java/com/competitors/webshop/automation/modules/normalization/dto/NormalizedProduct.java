package com.competitors.webshop.automation.modules.normalization.dto;

import lombok.Builder;

@Builder
public record NormalizedProduct (String productName, String normalizedName) {}
