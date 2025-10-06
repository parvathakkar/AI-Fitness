package com.fitness.aiservice.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Service
public class GeminiService {
    private final WebClient geminiClient;

    @Value("${gemini.api.url}")
    private String geminiUrl;

    @Value("${gemini.api.key}")
    private String geminiKey;

    public GeminiService(@Qualifier("geminiWebClient") WebClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public String getAnswer(String question){
        //constructing the input body that gemini accepts
        Map<String,Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                            Map.of("text",question)
                        })
                }
        );

        return geminiClient.post()
                .uri(geminiUrl+geminiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
