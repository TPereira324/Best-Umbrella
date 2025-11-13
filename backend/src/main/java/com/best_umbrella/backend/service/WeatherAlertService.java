package com.best_umbrella.backend.service;

import com.best_umbrella.backend.dto.NotiDto;
import com.best_umbrella.backend.dto.OpenWeatherDto;
import com.best_umbrella.backend.model.Noti;
import com.best_umbrella.backend.model.Utilizador;
import com.best_umbrella.backend.repository.NotiRepository;
import com.best_umbrella.backend.repository.UtilizadorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WeatherAlertService {
    private static final Logger log = LoggerFactory.getLogger(WeatherAlertService.class);

    private final OpenWeatherService openWeatherService;
    private final UtilizadorRepository utilizadorRepository;
    private final NotiRepository notiRepository;
    private final NotificacaoBroadcaster broadcaster;

    public WeatherAlertService(OpenWeatherService openWeatherService,
                               UtilizadorRepository utilizadorRepository,
                               NotiRepository notiRepository,
                               NotificacaoBroadcaster broadcaster) {
        this.openWeatherService = openWeatherService;
        this.utilizadorRepository = utilizadorRepository;
        this.notiRepository = notiRepository;
        this.broadcaster = broadcaster;
    }

    public NotiDto triggerIfRainForCity(Long utilizadorId, String city) {
        Utilizador u = utilizadorRepository.findById(utilizadorId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado: " + utilizadorId));

        OpenWeatherDto weather = openWeatherService.currentByCity(city);
        if (!isRain(weather)) {
            log.info("Sem chuva para {} ({}), não serão enviadas notificações.", weather.getCity(), utilizadorId);
            return null;
        }

        Noti n = new Noti();
        n.setUtilizador(u);
        n.setMensagem(mensagemChuva(weather));
        n.setTipo("CHUVA");
        n.setDataEnvio(LocalDateTime.now());
        n.setEstado("ENVIADA");
        Noti salvo = notiRepository.save(n);

        NotiDto dto = toDto(salvo);
        broadcaster.broadcast(utilizadorId, dto);
        return dto;
    }

    public NotiDto triggerIfRainForCoords(Long utilizadorId, double lat, double lon) {
        Utilizador u = utilizadorRepository.findById(utilizadorId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado: " + utilizadorId));

        OpenWeatherDto weather = openWeatherService.currentByCoords(lat, lon);
        if (!isRain(weather)) {
            log.info("Sem chuva para lat={}, lon={} ({}), não serão enviadas notificações.", lat, lon, utilizadorId);
            return null;
        }

        Noti n = new Noti();
        n.setUtilizador(u);
        n.setMensagem(mensagemChuva(weather));
        n.setTipo("CHUVA");
        n.setDataEnvio(LocalDateTime.now());
        n.setEstado("ENVIADA");
        Noti salvo = notiRepository.save(n);

        NotiDto dto = toDto(salvo);
        broadcaster.broadcast(utilizadorId, dto);
        return dto;
    }

    private boolean isRain(OpenWeatherDto dto) {
        String condition = dto.getCondition() == null ? "" : dto.getCondition().toLowerCase();
        String icon = dto.getIcon() == null ? "" : dto.getIcon();
        boolean byText = condition.contains("chuva") || condition.contains("chuv") || condition.contains("rain");
        boolean byIcon = icon.startsWith("09") || icon.startsWith("10") || icon.startsWith("11");
        return byText || byIcon;
    }

    private String mensagemChuva(OpenWeatherDto dto) {
        String cidade = dto.getCity() == null || dto.getCity().isBlank() ? "a sua zona" : dto.getCity();
        return "Alerta de chuva em " + cidade + ": " + dto.getCondition();
    }

    private NotiDto toDto(Noti n) {
        return new NotiDto(n.getNotificacaoId(), n.getMensagem(), n.getTipo(), n.getDataEnvio(), n.getEstado());
    }
}