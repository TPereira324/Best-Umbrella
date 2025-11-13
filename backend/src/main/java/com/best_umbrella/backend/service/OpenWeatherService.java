package com.best_umbrella.backend.service;

import com.best_umbrella.backend.config.OpenWeatherProperties;
import com.best_umbrella.backend.dto.OpenWeatherDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenWeatherService {
    private final OpenWeatherProperties props;
    private final RestTemplate rest = new RestTemplate();

    public OpenWeatherService(OpenWeatherProperties props) {
        this.props = props;
    }

    public OpenWeatherDto currentByCity(String city) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=%s&lang=%s",
                encode(city), props.getApiKey(), props.getUnits(), props.getLang()
        );
        return fetchAndMap(url);
    }

    public OpenWeatherDto currentByCoords(double lat, double lon) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=%s&lang=%s",
                lat, lon, props.getApiKey(), props.getUnits(), props.getLang()
        );
        OpenWeatherDto dto = fetchAndMap(url);
        // Preferir nome canónico através do geocoding reverso (ex.: Lisboa em vez de Socorro)
        String resolved = resolveCityName(lat, lon);
        if (resolved != null && !resolved.isBlank()) {
            dto.setCity(resolved);
        }
        return dto;
    }

    private OpenWeatherDto fetchAndMap(String url) {
        Map<String, Object> resp = rest.getForObject(url, Map.class);
        if (resp == null) {
            return new OpenWeatherDto("", "", 0, 0, 0, 0, "Sem dados", "");
        }

        String city = str(resp.get("name"));
        String country = "";
        Object sysObj = resp.get("sys");
        if (sysObj instanceof Map) {
            Map<String, Object> sys = (Map<String, Object>) sysObj;
            country = str(sys.get("country"));
        }

        Map<String, Object> main = Map.of();
        Object mainObj = resp.get("main");
        if (mainObj instanceof Map) {
            main = (Map<String, Object>) mainObj;
        }
        double temp = num(main.get("temp"));
        double feels = num(main.get("feels_like"));
        int humidity = (int) Math.round(num(main.get("humidity")));

        Map<String, Object> wind = Map.of();
        Object windObj = resp.get("wind");
        if (windObj instanceof Map) {
            wind = (Map<String, Object>) windObj;
        }
        double windSpeed = num(wind.get("speed"));

        String condition = "";
        String icon = "";
        Object wobj = resp.get("weather");
        if (wobj instanceof List<?>) {
            List<?> list = (List<?>) wobj;
            if (!list.isEmpty()) {
                Object first = list.get(0);
                if (first instanceof Map) {
                    Map<String, Object> wm = (Map<String, Object>) first;
                    condition = str(wm.get("description"));
                    icon = str(wm.get("icon"));
                }
            }
        }

        return new OpenWeatherDto(city, country, temp, feels, humidity, windSpeed, condition, icon);
    }

    // Resolve o nome da cidade a partir de lat/lon usando a API de geocoding reverso da OpenWeather
    private String resolveCityName(double lat, double lon) {
        try {
            String url = String.format(
                    "https://api.openweathermap.org/geo/1.0/reverse?lat=%s&lon=%s&limit=1&appid=%s",
                    lat, lon, props.getApiKey()
            );
            Object obj = rest.getForObject(url, List.class);
            if (!(obj instanceof List<?> list) || list.isEmpty()) return "";
            Object first = list.get(0);
            if (!(first instanceof Map)) return "";
            Map<String, Object> geo = (Map<String, Object>) first;

            // Tenta usar o nome local em PT; caso contrário usa o name geral.
            Object localNamesObj = geo.get("local_names");
            if (localNamesObj instanceof Map<?, ?> ln) {
                Object ptName = ln.get("pt");
                if (ptName != null) return String.valueOf(ptName);
            }

            String name = str(geo.get("name"));
            String state = str(geo.get("state"));

            // Heurística simples: se o estado for Lisboa/Lisbon, preferir "Lisboa"
            if (!state.isBlank() && (state.equalsIgnoreCase("Lisboa") || state.equalsIgnoreCase("Lisbon"))) {
                return "Lisboa";
            }
            return name;
        } catch (Exception e) {
            return "";
        }
    }

    private static String encode(String s) { return s == null ? "" : s.replace(" ", "%20"); }
    private static String str(Object o) { return o == null ? "" : String.valueOf(o); }
    private static double num(Object o) {
        if (o instanceof Number n) return n.doubleValue();
        try { return Double.parseDouble(String.valueOf(o)); } catch (Exception e) { return 0.0; }
    }
}