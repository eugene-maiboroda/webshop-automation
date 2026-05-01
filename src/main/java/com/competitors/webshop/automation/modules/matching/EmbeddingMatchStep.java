package com.competitors.webshop.automation.modules.matching;

import com.competitors.webshop.automation.modules.matching.dto.MatchInput;
import com.competitors.webshop.automation.modules.matching.dto.MatchResultDto;

public interface EmbeddingMatchStep {
    MatchResultDto match(MatchInput input);
}