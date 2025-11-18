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
    private Integer corId;
    private Integer tipoId;
    private LocalDateTime dataRegisto;
    private Integer pontoId;
    private List<AluguerSummaryDto> alugueres;

    public GuardaChuvaDto() {}

    public GuardaChuvaDto(Integer guardaChuvaId, String codigoQr, Integer corId, Integer tipoId,
                          LocalDateTime dataRegisto, Integer pontoId, List<AluguerSummaryDto> alugueres) {
        this.guardaChuvaId = guardaChuvaId;
        this.codigoQr = codigoQr;
        this.corId = corId;
        this.tipoId = tipoId;
        this.dataRegisto = dataRegisto;
        this.pontoId = pontoId;
        this.alugueres = alugueres;
    }

    public Integer getGuardaChuvaId() { return guardaChuvaId; }
    public void setGuardaChuvaId(Integer guardaChuvaId) { this.guardaChuvaId = guardaChuvaId; }
    public String getCodigoQr() { return codigoQr; }
    public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }
    public Integer getCorId() { return corId; }
    public void setCorId(Integer corId) { this.corId = corId; }
    public Integer getTipoId() { return tipoId; }
    public void setTipoId(Integer tipoId) { this.tipoId = tipoId; }
    public LocalDateTime getDataRegisto() { return dataRegisto; }
    public void setDataRegisto(LocalDateTime dataRegisto) { this.dataRegisto = dataRegisto; }
    public Integer getPontoId() { return pontoId; }
    public void setPontoId(Integer pontoId) { this.pontoId = pontoId; }
    public List<AluguerSummaryDto> getAlugueres() { return alugueres; }
    public void setAlugueres(List<AluguerSummaryDto> alugueres) { this.alugueres = alugueres; }
}