package com.competitors.webshop.automation.modules.normalization;

import com.competitors.webshop.automation.modules.normalization.dto.NormalizationInput;
import com.competitors.webshop.automation.modules.normalization.dto.NormalizedProduct;

public class NormalizationService {

    private final StopWordFilter stopWordFilter;

    public NormalizationService(StopWordFilter stopWordFilter) {
        this.stopWordFilter = stopWordFilter;
    }

    public NormalizedProduct normalize(NormalizationInput input) {
        String normalized = stopWordFilter.apply(input.rawName());
        return new NormalizedProduct(input.rawName(), normalized);
    }

    public static void main(String[] args) {
        StopWordFilter stopWordFilter = new StopWordFilter();
        NormalizationService normalizationService = new NormalizationService(stopWordFilter);
        NormalizationApi api = new NormalizationApi(normalizationService);

        NormalizationInput input = new NormalizationInput("Audio-Technica AT-2020 XLR studio kondensatormikrofon AT-2020");
        NormalizedProduct result = api.normalize(input);
        System.out.println("Original: " + result.productName());
        System.out.println("Normalized: " + result.normalizedName());
    }
}