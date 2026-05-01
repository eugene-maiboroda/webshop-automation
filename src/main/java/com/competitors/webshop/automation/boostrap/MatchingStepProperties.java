package com.competitors.webshop.automation.boostrap;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "matching-step")
public class MatchingStepProperties {

    private StepProperty exact;
    private StepProperty embedding;
    private StepProperty gpt;

    @Getter
    @Setter
    public static class StepProperty {
        boolean disabled;
    }
}