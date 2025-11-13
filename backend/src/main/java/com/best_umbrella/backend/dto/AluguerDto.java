package com.best_umbrella.backend.dto;

import java.time.LocalDateTime;

public class AluguerDto {
    private Long aluguerId;
    private Integer utilizadorId;
    private Integer guardaChuvaId;
    private Integer pontoInicioId;
    private Integer pontoFimId;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Double custo;
    private String estado;

    public AluguerDto() {}

    public AluguerDto(Long aluguerId, Integer utilizadorId, Integer guardaChuvaId,
                      Integer pontoInicioId, Integer pontoFimId,
                      LocalDateTime dataInicio, LocalDateTime dataFim,
                      Double custo, String estado) {
        this.aluguerId = aluguerId;
        this.utilizadorId = utilizadorId;
        this.guardaChuvaId = guardaChuvaId;
        this.pontoInicioId = pontoInicioId;
        this.pontoFimId = pontoFimId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.custo = custo;
        this.estado = estado;
    }

    public Long getAluguerId() { return aluguerId; }
    public void setAluguerId(Long aluguerId) { this.aluguerId = aluguerId; }
    public Integer getUtilizadorId() { return utilizadorId; }
    public void setUtilizadorId(Integer utilizadorId) { this.utilizadorId = utilizadorId; }
    public Integer getGuardaChuvaId() { return guardaChuvaId; }
    public void setGuardaChuvaId(Integer guardaChuvaId) { this.guardaChuvaId = guardaChuvaId; }
    public Integer getPontoInicioId() { return pontoInicioId; }
    public void setPontoInicioId(Integer pontoInicioId) { this.pontoInicioId = pontoInicioId; }
    public Integer getPontoFimId() { return pontoFimId; }
    public void setPontoFimId(Integer pontoFimId) { this.pontoFimId = pontoFimId; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public Double getCusto() { return custo; }
    public void setCusto(Double custo) { this.custo = custo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}