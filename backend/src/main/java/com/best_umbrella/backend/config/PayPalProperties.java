package com.best_umbrella.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PayPalProperties {
    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode:sandbox}")
    private String mode;

    public String getClientId() { return clientId; }
    public String getClientSecret() { return clientSecret; }
    public String getMode() { return mode; }

    public String getBaseUrl() {
        return "live".equalsIgnoreCase(mode)
                ? "https://api-m.paypal.com"
                : "https://api-m.sandbox.paypal.com";
    }
}