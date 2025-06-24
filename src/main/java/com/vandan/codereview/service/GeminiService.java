package com.vandan.codereview.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getReviewFromGemini(String diffText) {
        try {
            String prompt = "You're a senior software engineer reviewing a pull request. "
                    + "Please provide feedback on this code diff. "
                    + "Mention bugs, improvements, code smells, and suggestions clearly. "
                    + "Here's the diff:\n\n"
                    + diffText;

            Map<String, Object> requestBody = Map.of(
                    "contents", Collections.singletonList(
                            Map.of("parts", Collections.singletonList(
                                    Map.of("text", prompt)
                            ))
                    )
            );

            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/gemini-2.0-flash:generateContent")
                            .queryParam("key", geminiApiKey)
                            .build()
                    )
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        System.err.println("Gemini API returned error: " + clientResponse.statusCode());
                        return Mono.error(new RuntimeException("Gemini API error: " + clientResponse.statusCode()));
                    })
                    .bodyToMono(Map.class)
                    .map(response -> {
                        try {
                            var candidates = (java.util.List<?>) response.get("candidates");
                            if (candidates != null && !candidates.isEmpty()) {
                                var firstCandidate = (Map<?, ?>) candidates.get(0);
                                var content = (Map<?, ?>) firstCandidate.get("content");
                                var parts = (java.util.List<?>) content.get("parts");
                                if (parts != null && !parts.isEmpty()) {
                                    var part = (Map<?, ?>) parts.get(0);
                                    return part.get("text").toString();
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Failed to parse Gemini response: " + e.getMessage());
                        }
                        return "No response from Gemini.";
                    })
                    .onErrorReturn("Error while calling Gemini")
                    .block();

        } catch (Exception e) {
            System.err.println("Gemini call failed: " + e.getMessage());
            return "Gemini call failed";
        }
    }

}
