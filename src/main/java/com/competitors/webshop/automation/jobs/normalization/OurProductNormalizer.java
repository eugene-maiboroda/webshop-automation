package com.competitors.webshop.automation.jobs.normalization;

import com.competitors.webshop.automation.model.MatchStatus;
import com.competitors.webshop.automation.model.OurProduct;
import com.competitors.webshop.automation.modules.normalization.NormalizationApi;
import com.competitors.webshop.automation.modules.normalization.dto.NormalizationInput;
import com.competitors.webshop.automation.modules.normalization.dto.NormalizedProduct;
import com.competitors.webshop.automation.repository.OurProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OurProductNormalizer {

    private static final int BATCH_SIZE = 100;

    private final OurProductRepository ourProductRepository;
    private final NormalizationApi normalizationApi;

    public void processAll() {
        Pageable pageable = PageRequest.of(0, BATCH_SIZE,
                Sort.by("createdAt").ascending());

        while (true) {
            List<OurProduct> batch = ourProductRepository
                    .findAllByStatus(MatchStatus.NEW, pageable);

            if (batch.isEmpty()) break;

            for (OurProduct product : batch) {
                try {
                    process(product);
                } catch (Exception e) {
                    log.error("Failed id={}", product.getId(), e);
                }
            }
        }
    }

    private void process(OurProduct product) {
        NormalizedProduct result = normalizationApi.normalize(new NormalizationInput(product.getName()));
        ourProductRepository.updateNormalized(
                product.getId(),
                result.normalizedName(),
                MatchStatus.PENDING
        );
    }
}