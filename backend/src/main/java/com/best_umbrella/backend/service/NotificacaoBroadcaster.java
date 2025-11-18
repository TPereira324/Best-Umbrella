package com.best_umbrella.backend.service;

import com.best_umbrella.backend.dto.NotiDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificacaoBroadcaster {
    private static final Logger log = LoggerFactory.getLogger(NotificacaoBroadcaster.class);
    // timeout alto para ligações SSE de longa duração
    private static final long SSE_TIMEOUT_MS = 30 * 60 * 1000L; // 30 minutos

    private final Map<Long, CopyOnWriteArrayList<SseEmitter>> emittersPorUtilizador = new ConcurrentHashMap<>();
    private final Map<SseEmitter, Long> criadoEm = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long utilizadorId) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);
        emittersPorUtilizador.computeIfAbsent(utilizadorId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        criadoEm.put(emitter, System.currentTimeMillis());

        emitter.onCompletion(() -> removerEmitter(utilizadorId, emitter));
        emitter.onTimeout(() -> removerEmitter(utilizadorId, emitter));
        emitter.onError((ex) -> removerEmitter(utilizadorId, emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .id(String.valueOf(Instant.now().toEpochMilli()))
                    .data("ok"));
        } catch (IOException e) {
            log.warn("Falha ao enviar evento de conexão SSE: {}", e.getMessage());
        }

        return emitter;
    }

    public void broadcast(Long utilizadorId, NotiDto dto) {
        List<SseEmitter> lista = emittersPorUtilizador.get(utilizadorId);
        if (lista == null || lista.isEmpty()) {
            log.debug("Sem subscritores SSE para utilizador {}", utilizadorId);
            return;
        }

        for (SseEmitter emitter : lista) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notificacao")
                        .data(dto, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                log.debug("A remover emitter obsoleto por IOException: {}", e.getMessage());
                removerEmitter(utilizadorId, emitter);
            }
        }
    }

    // Mantém ligações vivas em proxies/gateways que fecham conexões ociosas
    @Scheduled(fixedRate = 25_000)
    public void heartbeat() {
        for (Map.Entry<Long, CopyOnWriteArrayList<SseEmitter>> entry : emittersPorUtilizador.entrySet()) {
            Long uid = entry.getKey();
            for (SseEmitter emitter : entry.getValue()) {
                try {
                    emitter.send(SseEmitter.event().name("heartbeat").data("ping"));
                } catch (IOException e) {
                    removerEmitter(uid, emitter);
                }
            }
        }
    }

    private void removerEmitter(Long utilizadorId, SseEmitter emitter) {
        List<SseEmitter> lista = emittersPorUtilizador.get(utilizadorId);
        if (lista != null) {
            lista.remove(emitter);
            if (lista.isEmpty()) {
                emittersPorUtilizador.remove(utilizadorId);
            }
        }
        criadoEm.remove(emitter);
    }

    @Scheduled(fixedRate = 60_000)
    public void pruneInativos() {
        long agora = System.currentTimeMillis();
        for (Map.Entry<Long, CopyOnWriteArrayList<SseEmitter>> entry : emittersPorUtilizador.entrySet()) {
            Long uid = entry.getKey();
            for (SseEmitter emitter : entry.getValue()) {
                Long created = criadoEm.getOrDefault(emitter, agora);
                if (agora - created > SSE_TIMEOUT_MS) {
                    removerEmitter(uid, emitter);
                }
            }
        }
    }
}
// hello world