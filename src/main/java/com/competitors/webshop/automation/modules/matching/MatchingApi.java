package com.competitors.webshop.automation.modules.matching;

import com.competitors.webshop.automation.modules.matching.dto.MatchInput;
import com.competitors.webshop.automation.modules.matching.dto.MatchResultDto;

public class MatchingApi {

    private final ExactMatchStep exactMatchStep;
    private final EmbeddingMatchStep embeddingMatchStep;
    private final GptConfirmStep gptConfirmStep;

    public MatchingApi(ExactMatchStep exactStep, EmbeddingMatchStep embeddingStep, GptConfirmStep gptConfirmStep) {
        this.exactMatchStep = exactStep;
        this.embeddingMatchStep = embeddingStep;
        this.gptConfirmStep = gptConfirmStep;
    }

    public MatchResultDto exactMatch(MatchInput input) {
        return exactMatchStep.match(input);
    }
    public MatchResultDto embeddingMatch(MatchInput input) {
        return embeddingMatchStep.match(input);
    }
    public MatchResultDto gptConfirm(MatchInput input) {
        return gptConfirmStep.confirm(input);
    }
}