package com.best_umbrella.backend.dto;

/**
 * DTO de Estação de Aluguer.
 *
 * Utilizado para transportar dados das estações (localização, capacidade e
 * disponibilidade) entre back-end e front-end.
 */
public class RentalStationDto {
    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer available;
    private Integer capacity;

    public RentalStationDto() {}

    public RentalStationDto(Integer id, String name, Double latitude, Double longitude, Integer available, Integer capacity) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.available = available;
        this.capacity = capacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}