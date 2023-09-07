package com.gateway.Gateway.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EndpointMapProperties {
    private Map<String, Integer> endpoints = new HashMap<>();

    public Map<String, Integer> getEndpoints() {
        endpoints.put("multistrikeOi/NIFTY",300);
        endpoints.put("combinedOi/NIFTY",300);
        endpoints.put("techsignal/RELIANCE/5",10);
        endpoints.put("futuresDashboard/NEAR",300);
        endpoints.put("momentum-score/N",300);
        endpoints.put("reloutperf",300);
        endpoints.put("volume/HIGH_DELIVERY_PERCENTAGE",300);

        return endpoints;
    }
}
