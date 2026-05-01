package com.competitors.webshop.automation.jobs;

import com.competitors.webshop.automation.jobs.normalization.CompetitorNormalizer;
import com.competitors.webshop.automation.jobs.normalization.OurProductNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NormalizationJob {

    private final CompetitorNormalizer competitorNormalizer;
    private final OurProductNormalizer ourProductNormalizer;

    @Scheduled(cron = "0 30 2 * * *")
    public void run() {
        log.info("NormalizationJob started");
        competitorNormalizer.processAll();
        ourProductNormalizer.processAll();
        log.info("NormalizationJob finished");
    }
}