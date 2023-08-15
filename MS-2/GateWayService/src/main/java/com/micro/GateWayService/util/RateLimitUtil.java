package com.micro.GateWayService.util;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

import java.time.Duration;

public class RateLimitUtil {
    public static Bucket createBucketForRateLimit(RateLimit rateLimit) {
        switch (rateLimit) {
            case FREE:
                return createBucketWithLimit(2, Duration.ofMinutes(1));
            case NORMAL:
                return createBucketWithLimit(5, Duration.ofMinutes(1));
            case PREMIUM:
                return createBucketWithLimit(10, Duration.ofMinutes(1));
            default:
                throw new IllegalArgumentException("Unsupported rate limit: " + rateLimit);
        }
    }

    private static Bucket createBucketWithLimit(int tokens, Duration duration) {
        Bandwidth limit = Bandwidth.simple(tokens, duration);
        return Bucket4j.builder().addLimit(limit).build();
    }

}
