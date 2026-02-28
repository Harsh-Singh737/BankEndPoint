package com.bankingtransactions.bankingendpoints.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimiterConfig {

    private final Bucket bucket;

    public RateLimiterConfig() {
        Bandwidth limit = Bandwidth.simple(5, Duration.ofMinutes(1));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    public Bucket getBucket() {
        return bucket;
    }
}
