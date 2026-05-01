package com.competitors.webshop.automation.modules.matching;

import com.competitors.webshop.automation.integration.openai.AiConfirmator;
import com.competitors.webshop.automation.modules.matching.dto.Candidate;
import com.competitors.webshop.automation.modules.matching.dto.MatchInput;
import com.competitors.webshop.automation.modules.matching.dto.MatchResultDto;

import java.util.Optional;

public class GptConfirmStep {

    private final AiConfirmator aiConfirmator;

    public GptConfirmStep(AiConfirmator aiConfirmator) {
        this.aiConfirmator = aiConfirmator;
    }

    public MatchResultDto confirm(MatchInput input) {
        Optional<Candidate> candidate = aiConfirmator.confirm(
                input.originalName(), input.candidates());

        return candidate
                .map(c -> MatchResultDto.matched(c.name(), c.score()))
                .orElseGet(() -> {
                    Candidate first = input.candidates().getFirst();
                    return MatchResultDto.manual(first.name(), first.score());
                });
    }
}