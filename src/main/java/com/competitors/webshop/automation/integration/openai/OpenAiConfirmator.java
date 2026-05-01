package com.competitors.webshop.automation.integration.openai;

import com.competitors.webshop.automation.integration.openai.dto.MatchRequest;
import com.competitors.webshop.automation.integration.openai.dto.MatchResponse;
import com.competitors.webshop.automation.modules.matching.dto.Candidate;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpenAiConfirmator implements AiConfirmator {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Value("classpath:prompts/confirm-system-ua.txt")
    private Resource systemPrompt;

    @Override
    public Optional<Candidate> confirm(String originalName, List<Candidate> candidates) {
        int maxAttempts = 5;
        long backoffMillis = 1_000;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                String userPrompt = objectMapper.writeValueAsString(new MatchRequest(originalName, candidates));

                MatchResponse response = chatClient.prompt()
                        .system(systemPrompt)
                        .user(userPrompt)
                        .call()
                        .entity(MatchResponse.class);

                if (response == null) {
                    throw new RuntimeException("Empty response from AI");
                }

                if ("NO_MATCH".equals(response.match())) {
                    return Optional.empty();
                }

                int index = Integer.parseInt(response.match());
                return candidates.stream()
                        .filter(c -> c.index() == index)
                        .findFirst();

            } catch (Exception e) {
                boolean retryable = isRetryable(e);

                if (!retryable || attempt == maxAttempts) {
                    throw new RuntimeException("AI confirm failed after retries", e);
                }

                try {
                    Thread.sleep(backoffMillis);
                } catch (InterruptedException ignored) {}

                backoffMillis = Math.min(backoffMillis * 2, 10_000);
            }
        }

        throw new IllegalStateException("Unreachable");
    }

    private boolean isRetryable(Exception e) {
        String msg = e.getMessage();
        return msg != null && (
                msg.contains("429") ||
                msg.contains("rate limit") ||
                msg.contains("timeout") ||
                msg.contains("500")
        );
    }
}