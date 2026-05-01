package com.competitors.webshop.automation.modules.normalization;

import com.competitors.webshop.automation.modules.normalization.dto.NormalizationInput;
import com.competitors.webshop.automation.modules.normalization.dto.NormalizedProduct;

public class NormalizationApi {

    private final NormalizationService normalizationService;

    public NormalizationApi(NormalizationService normalizationService) {
        this.normalizationService = normalizationService;
    }

    public NormalizedProduct normalize(NormalizationInput input) {
        return normalizationService.normalize(input);
    }
}
