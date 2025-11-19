package com.best_umbrella.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenWeatherProperties {
    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.units:metric}")
    private String units;

    @Value("${openweather.api.lang:pt}")
    private String lang;

    public String getApiKey() { return apiKey; }
    public String getUnits() { return units; }
    public String getLang() { return lang; }
}
// hello world