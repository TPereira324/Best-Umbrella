package com.best_umbrella.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * Configuração de CORS (Cross-Origin Resource Sharing).
 *
 * Permite chamadas do front-end (localhost e 10.0.2.2 — Android emulator)
 * para os endpoints da API (`/api/**`) e os legados (`/users/**`).
 * Métodos permitidos: GET, POST, PUT, DELETE, OPTIONS.
 */
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("http://10.0.2.2:*", "http://localhost:*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
                registry.addMapping("/users/**")
                        .allowedOriginPatterns("http://10.0.2.2:*", "http://localhost:*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}
// hello world