package com.best_umbrella.backend.controllers;

import com.best_umbrella.backend.service.NotificacaoBroadcaster;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/Notificacao")
public class NotificacaoStreamController {

    private final NotificacaoBroadcaster broadcaster;

    public NotificacaoStreamController(NotificacaoBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam("utilizadorId") Long utilizadorId) {
        return broadcaster.subscribe(utilizadorId);
    }
}