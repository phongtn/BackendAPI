package com.wind.external.sample;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class ExternalSampleHealthCheck extends AbstractHealthIndicator {

    private final String messageKey = "Sample External service";

    private final ExternalSampleService sampleService;

    public ExternalSampleHealthCheck(ExternalSampleService sampleService) {
        this.sampleService = sampleService;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        if (this.isRun())
            builder.up().withDetail(messageKey, "Available");
        else
            builder.down().withDetail(messageKey, "Not Available");;
    }

    private boolean isRun() {
        return !sampleService.allSampleDto().isEmpty();
    }
}
