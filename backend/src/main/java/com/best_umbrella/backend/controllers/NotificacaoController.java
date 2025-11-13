package com.best_umbrella.backend.controllers;

import com.best_umbrella.backend.dto.NotiDto;
import com.best_umbrella.backend.service.WeatherAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Notificacao")
public class NotificacaoController {

    private final WeatherAlertService weatherAlertService;

    public NotificacaoController(WeatherAlertService weatherAlertService) {
        this.weatherAlertService = weatherAlertService;
    }

    @PostMapping("/alerta-chuva")
    public ResponseEntity<?> triggerRainAlertByCity(@RequestParam("utilizadorId") Long utilizadorId,
                                                    @RequestParam(value = "city", required = false) String city,
                                                    @RequestParam(value = "lat", required = false) Double lat,
                                                    @RequestParam(value = "lon", required = false) Double lon) {
        NotiDto dto;
        if (city != null && !city.isBlank()) {
            dto = weatherAlertService.triggerIfRainForCity(utilizadorId, city);
        } else if (lat != null && lon != null) {
            dto = weatherAlertService.triggerIfRainForCoords(utilizadorId, lat, lon);
        } else {
            dto = weatherAlertService.triggerIfRainForCity(utilizadorId, "Lisboa");
        }

        if (dto == null) {
            return ResponseEntity.ok("Sem chuva no local indicado. Nada a notificar.");
        }
        return ResponseEntity.ok(dto);
    }
}