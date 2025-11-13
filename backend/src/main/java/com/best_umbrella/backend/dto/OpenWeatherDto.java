package com.best_umbrella.backend.dto;

public class OpenWeatherDto {
    private String city;
    private String country;
    private double temperature;
    private double feelsLike;
    private int humidity;
    private double windSpeed;
    private String condition;
    private String icon;

    public OpenWeatherDto() {}

    public OpenWeatherDto(String city, String country, double temperature, double feelsLike, int humidity,
                          double windSpeed, String condition, String icon) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.condition = condition;
        this.icon = icon;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }
    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}