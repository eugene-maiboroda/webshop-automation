package com.competitors.webshop.automation.integration.openai;

import com.competitors.webshop.automation.modules.matching.dto.Candidate;

import java.util.List;
import java.util.Optional;

public interface AiConfirmator {
    Optional<Candidate> confirm(String originalName, List<Candidate> candidates);
}