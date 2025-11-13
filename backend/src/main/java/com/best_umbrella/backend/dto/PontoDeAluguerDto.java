package com.best_umbrella.backend.dto;

public class PontoDeAluguerDto {
    private Integer pontoId;
    private String nome;
    private Double latitude;
    private Double longitude;
    private Integer capacidade;
    private String tipo;
    private Integer quantidadeGuardaChuvas;

    public PontoDeAluguerDto() {}

    public PontoDeAluguerDto(Integer pontoId, String nome, Double latitude, Double longitude,
                              Integer capacidade, String tipo, Integer quantidadeGuardaChuvas) {
        this.pontoId = pontoId;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacidade = capacidade;
        this.tipo = tipo;
        this.quantidadeGuardaChuvas = quantidadeGuardaChuvas;
    }

    public Integer getPontoId() { return pontoId; }
    public void setPontoId(Integer pontoId) { this.pontoId = pontoId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getQuantidadeGuardaChuvas() { return quantidadeGuardaChuvas; }
    public void setQuantidadeGuardaChuvas(Integer quantidadeGuardaChuvas) { this.quantidadeGuardaChuvas = quantidadeGuardaChuvas; }
}