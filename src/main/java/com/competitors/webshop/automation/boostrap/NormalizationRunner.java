package com.competitors.webshop.automation.boostrap;

import com.competitors.webshop.automation.jobs.normalization.CompetitorNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NormalizationRunner implements CommandLineRunner {

    private final CompetitorNormalizer normalizer;

    @Override
    public void run(String... args) {
        log.info("Running Normalization Runner");
            normalizer.processAll();
    }
}