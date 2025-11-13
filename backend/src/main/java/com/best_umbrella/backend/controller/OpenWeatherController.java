package com.best_umbrella.backend.controller;

import com.best_umbrella.backend.dto.OpenWeatherDto;
import com.best_umbrella.backend.service.OpenWeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class OpenWeatherController {
    private final OpenWeatherService service;

    public OpenWeatherController(OpenWeatherService service) {
        this.service = service;
    }

    @GetMapping("/current")
    public ResponseEntity<OpenWeatherDto> currentByCity(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon
    ) {
        OpenWeatherDto dto;
        if (city != null && !city.isBlank()) {
            dto = service.currentByCity(city);
        } else if (lat != null && lon != null) {
            dto = service.currentByCoords(lat, lon);
        } else {
            // fallback: Lisboa
            dto = service.currentByCity("Lisboa");
        }
        return ResponseEntity.ok(dto);
    }
}