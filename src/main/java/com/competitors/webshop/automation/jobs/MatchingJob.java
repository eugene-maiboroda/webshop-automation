package com.competitors.webshop.automation.jobs;


import com.competitors.webshop.automation.boostrap.MatchingStepProperties;
import com.competitors.webshop.automation.jobs.mapper.MatchingMapper;
import com.competitors.webshop.automation.model.MatchResult;
import com.competitors.webshop.automation.model.OurProduct;
import com.competitors.webshop.automation.modules.matching.MatchingApi;
import com.competitors.webshop.automation.modules.matching.dto.MatchInput;
import com.competitors.webshop.automation.model.MatchStatus;
import com.competitors.webshop.automation.modules.matching.dto.MatchResultDto;
import com.competitors.webshop.automation.repository.MatchResultRepository;
import com.competitors.webshop.automation.repository.OurProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchingJob {

    private static final int BATCH_SIZE = 100;

    private final MatchingApi matchingApi;
    private final OurProductRepository ourProductRepo;
    private final MatchResultRepository matchResultRepo;
    private final MatchingStepProperties props;
    private final MatchingMapper matchingMapper;


    @Scheduled(fixedDelay = 60_000)
    public void run() {
        Pageable pageable = PageRequest.of(0, BATCH_SIZE, Sort.by("createdAt").ascending());

        while (true) {
            List<OurProduct> batch = ourProductRepo.findAllByStatus(MatchStatus.PENDING, pageable);

            if (batch.isEmpty()) {
                break;
            }

            batch.forEach(this::processOne);
        }
    }

    private void processOne(OurProduct product) {
        try {
            MatchResultDto result = process(MatchInput.from(product));
            MatchResult entity = matchingMapper.toEntity(product.getId(), result);
            matchResultRepo.save(entity);
            ourProductRepo.updateStatus(product.getId(), result.status());
        } catch (Exception e) {
            ourProductRepo.updateStatus(product.getId(), MatchStatus.FAILED);
        }
    }

    private MatchResultDto process(MatchInput input) {
        MatchResultDto result = matchingApi.exactMatch(input);
        if (result.isMatched()) return result;

        if (!props.getEmbedding().isDisabled()) {
            result = matchingApi.embeddingMatch(input);
            if (result.isMatched()) return result;

            if (result.isUncertain()) {
                input = input.withCandidates(result.candidates());
            }
        }

        if (!props.getGpt().isDisabled()) {
            result = matchingApi.gptConfirm(input);
        }

        return result;
    }
}