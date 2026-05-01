package com.competitors.webshop.automation.modules.matching.dto;

import com.competitors.webshop.automation.model.MatchStatus;

import java.util.List;

public record MatchResultDto(
        MatchStatus status,
        String competitorName,
        float score,
        List<Candidate> candidates
) {
    public static MatchResultDto matched(String name, float score) {
        return new MatchResultDto(MatchStatus.MATCHED, name, score, List.of());
    }

    public static MatchResultDto uncertain(List<Candidate> candidates) {
        return new MatchResultDto(MatchStatus.UNCERTAIN, null, 0f, candidates);
    }

    public static MatchResultDto manual(String name, float score) {
        return new MatchResultDto(MatchStatus.MANUAL, name, score, List.of());
    }

    public static MatchResultDto notMatched() {
        return new MatchResultDto(MatchStatus.NO_MATCH, null, 0f, List.of());
    }

    public boolean isMatched() {
        return status == MatchStatus.MATCHED;
    }

    public boolean isUncertain() {
        return status == MatchStatus.UNCERTAIN;
    }
}