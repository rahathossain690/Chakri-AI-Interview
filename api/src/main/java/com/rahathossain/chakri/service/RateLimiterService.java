package com.rahathossain.chakri.service;

import com.rahathossain.chakri.exception.SystemFailureException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final Bucket apiBucket;
    private final Bucket llmBucket;

    public RateLimiterService(@Value("${rate.limiter.api.requests}") int apiRequests,
                              @Value("${rate.limiter.api.duration.minutes}") int apiDurationMinutes,
                              @Value("${rate.limiter.llm.requests}") int llmRequests,
                              @Value("${rate.limiter.llm.duration.seconds}") int llmDurationSeconds) {

        this.apiBucket = Bucket.builder()
                .addLimit(Bandwidth.classic(apiRequests, Refill.intervally(apiRequests, Duration.ofMinutes(apiDurationMinutes))))
                .build();

        this.llmBucket = Bucket.builder()
                .addLimit(Bandwidth.classic(llmRequests, Refill.intervally(llmRequests, Duration.ofSeconds(llmDurationSeconds))))
                .build();
    }

    public boolean isApiRequestAllowed() {
        return apiBucket.tryConsume(1);
    }

    public void consumeLlmRequest() {
        try {
            llmBucket.asBlocking().consume(1);
        } catch (Exception ex) {
            throw new SystemFailureException("Unable to rate limit the llm");
        }
    }
}