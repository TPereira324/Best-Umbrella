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
        return fetchAndMap(url);
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

    private static String encode(String s) { return s == null ? "" : s.replace(" ", "%20"); }
    private static String str(Object o) { return o == null ? "" : String.valueOf(o); }
    private static double num(Object o) {
        if (o instanceof Number n) return n.doubleValue();
        try { return Double.parseDouble(String.valueOf(o)); } catch (Exception e) { return 0.0; }
    }
}