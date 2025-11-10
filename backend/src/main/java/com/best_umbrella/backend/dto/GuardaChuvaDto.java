package com.best_umbrella.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de Guarda-Chuva usado nas respostas dos endpoints.
 *
 * Representa o estado e metadados do guarda-chuva, incluindo o ponto de aluguer
 * associado (pontoId) e uma lista de alugueres resumidos (AluguerSummaryDto).
 */
public class GuardaChuvaDto {
    private Integer guardaChuvaId;
    private String codigoQr;
    private String estado;
    private String cor;
    private String tipo;
    private LocalDateTime dataRegisto;
    private Integer pontoId;
    private List<AluguerSummaryDto> alugueres;

    public GuardaChuvaDto() {}

    public GuardaChuvaDto(Integer guardaChuvaId, String codigoQr, String estado, String cor, String tipo,
                          LocalDateTime dataRegisto, Integer pontoId, List<AluguerSummaryDto> alugueres) {
        this.guardaChuvaId = guardaChuvaId;
        this.codigoQr = codigoQr;
        this.estado = estado;
        this.cor = cor;
        this.tipo = tipo;
        this.dataRegisto = dataRegisto;
        this.pontoId = pontoId;
        this.alugueres = alugueres;
    }

    public Integer getGuardaChuvaId() { return guardaChuvaId; }
    public void setGuardaChuvaId(Integer guardaChuvaId) { this.guardaChuvaId = guardaChuvaId; }
    public String getCodigoQr() { return codigoQr; }
    public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDateTime getDataRegisto() { return dataRegisto; }
    public void setDataRegisto(LocalDateTime dataRegisto) { this.dataRegisto = dataRegisto; }
    public Integer getPontoId() { return pontoId; }
    public void setPontoId(Integer pontoId) { this.pontoId = pontoId; }
    public List<AluguerSummaryDto> getAlugueres() { return alugueres; }
    public void setAlugueres(List<AluguerSummaryDto> alugueres) { this.alugueres = alugueres; }
}