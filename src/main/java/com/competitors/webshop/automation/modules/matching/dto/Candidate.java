package com.competitors.webshop.automation.modules.matching.dto;

import java.math.BigDecimal;

public record Candidate(
        int index,
        String name,
        BigDecimal price,
        String currency,
        float score
) {}