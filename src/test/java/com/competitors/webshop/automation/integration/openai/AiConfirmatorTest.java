package com.competitors.webshop.automation.integration.openai;

import com.competitors.webshop.automation.modules.matching.dto.Candidate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class AiConfirmatorTest {

    @Autowired
    private AiConfirmator aiConfirmator;

    @Test
    void shouldConfirmSameProduct() {
        List<Candidate> candidates = List.of(
                new Candidate(1, "Audio-Technica AT-2031 Studio Condenser Microphone", new BigDecimal("199.99"), "USD", 0.89f),
                new Candidate(2, "Audio-Technica AT-2035 Cardioid Condenser Microphone", new BigDecimal("249.99"), "USD", 0.88f),
                new Candidate(3, "Audio-Technica AT-4041 Studio Microphone", new BigDecimal("299.99"), "USD", 0.80f)
        );

        Optional<Candidate> result = aiConfirmator.confirm(
                "Audio-Technica kondensatormikrofon nyre for studiobruk AT-2035",candidates);

        System.out.println("Matched: " + result);
    }

    @Test
    void shouldReturnEmptyForNoMatch() {
        List<Candidate> candidates = List.of(
                new Candidate(1, "Electro Voice PL80a vokalmikrofon Dynamisk, superkardioide", new BigDecimal("1429.00"), "USD", 0.70f),
                new Candidate(2, "Electro Voice PL80c vokalmikrofon Dynamisk, superkardioide", new BigDecimal("1479.00"), "USD", 0.66f)
        );

        Optional<Candidate> result = aiConfirmator.confirm(
                "Electro-Voice Mikrofon Dynamisk Håndholdt Supernyre", candidates);

        System.out.println("Matched: " + result);
    }
}