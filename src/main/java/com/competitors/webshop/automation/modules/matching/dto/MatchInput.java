package com.competitors.webshop.automation.modules.matching.dto;

import com.competitors.webshop.automation.model.OurProduct;

import java.util.List;

public record MatchInput(
        String modelCode,        // для ExactMatchStep
        String normalizedName,   // для EmbeddingMatchStep
        String originalName,     // для GptConfirmStep
        List<Candidate> candidates
) {
    public static MatchInput from(OurProduct product) {
        return new MatchInput(product.getModelCode(), product.getNormalizedName(), product.getName(), List.of());
    }

    public MatchInput withCandidates(List<Candidate> candidates) {
        return new MatchInput(modelCode, normalizedName, originalName, candidates);
    }
}