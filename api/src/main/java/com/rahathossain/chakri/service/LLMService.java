package com.rahathossain.chakri.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahathossain.chakri.exception.SystemFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

@Service
public class LLMService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model.id}")
    private String modelId;

    private final RateLimiterService rateLimiterService;

    public LLMService(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Async
    public CompletableFuture<String> runPromptAsync(String prompt) {

        rateLimiterService.consumeLlmRequest();

        String url = MessageFormat.format("https://generativelanguage.googleapis.com/v1beta/models/{0}:generateContent?key={1}",
                modelId,
                apiKey);

        String requestBody = generateJsonBody(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return CompletableFuture.completedFuture(parseResponse(response.getBody()));
    }

    private String generateJsonBody(String prompt) {
        return String.format(
                """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """, prompt.replace("\"", "\\\"")
        );
    }

    private String parseResponse(String responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            return "";
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode textNode = rootNode.path("candidates").path(0).path("content").path("parts").path(0).path("text");

            if (textNode.isTextual()) {
                return textNode.asText();

            } else {
                return "";
            }

        } catch (Exception e) {
            throw new SystemFailureException("Gemini not working");
        }
    }
}
