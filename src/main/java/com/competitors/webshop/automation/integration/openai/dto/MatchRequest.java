package com.competitors.webshop.automation.integration.openai.dto;

import com.competitors.webshop.automation.modules.matching.dto.Candidate;

import java.util.List;

public record MatchRequest(
        String productA,
        List<Candidate> candidates
) {}