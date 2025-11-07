package com.best_umbrella.backend.model;

import jakarta.persistence.*;
import jakarta.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "Ponto_de_aluguer")
public class PontodeAluguer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ponto_id")
    private Integer pontoId;

    private String nome;
    private Double latitude;
    private Double longitude;
    private Integer capacidade;
    private String tipo;

    @OneToMany(mappedBy = "pontodeAluguer", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GuardaChuva> guardaChuvas;
    
    @OneToMany(mappedBy = "pontoInicio", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Aluguer> alugueresInicio;
    
    @OneToMany(mappedBy = "pontoFim", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Aluguer> alugueresTermino;

    // Getters e Setters
    public Integer getPontoId() {
        return pontoId;
    }

    public void setPontoId(Integer pontoId) {
        this.pontoId = pontoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<GuardaChuva> getGuardaChuvas() {
        return guardaChuvas;
    }

    public void setGuardaChuvas(List<GuardaChuva> guardaChuvas) {
        this.guardaChuvas = guardaChuvas;
    }
    
    public List<Aluguer> getAlugueresInicio() {
        return alugueresInicio;
    }

    public void setAlugueresInicio(List<Aluguer> alugueresInicio) {
        this.alugueresInicio = alugueresInicio;
    }

    public List<Aluguer> getAlugueresTermino() {
        return alugueresTermino;
    }

    public void setAlugueresTermino(List<Aluguer> alugueresTermino) {
        this.alugueresTermino = alugueresTermino;
    }
}
