package com.best_umbrella.backend.dto;

import java.time.LocalDateTime;

/**
 * DTO resumido de Aluguer.
 *
 * Traz apenas os campos essenciais de um aluguer para respostas aninhadas
 * (ids relacionados, datas, custo e estado) evitando carregar entidades completas.
 */
public class AluguerSummaryDto {
    private Long aluguerId;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Double custo;
    private String estado;
    private Integer guardaChuvaId;
    private Integer pontoInicioId;
    private Integer pontoFimId;

    public AluguerSummaryDto() {}

    public AluguerSummaryDto(Long aluguerId, LocalDateTime dataInicio, LocalDateTime dataFim,
                              Double custo, String estado,
                              Integer guardaChuvaId, Integer pontoInicioId, Integer pontoFimId) {
        this.aluguerId = aluguerId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.custo = custo;
        this.estado = estado;
        this.guardaChuvaId = guardaChuvaId;
        this.pontoInicioId = pontoInicioId;
        this.pontoFimId = pontoFimId;
    }

    public Long getAluguerId() { return aluguerId; }
    public void setAluguerId(Long aluguerId) { this.aluguerId = aluguerId; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public Double getCusto() { return custo; }
    public void setCusto(Double custo) { this.custo = custo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getGuardaChuvaId() { return guardaChuvaId; }
    public void setGuardaChuvaId(Integer guardaChuvaId) { this.guardaChuvaId = guardaChuvaId; }
    public Integer getPontoInicioId() { return pontoInicioId; }
    public void setPontoInicioId(Integer pontoInicioId) { this.pontoInicioId = pontoInicioId; }
    public Integer getPontoFimId() { return pontoFimId; }
    public void setPontoFimId(Integer pontoFimId) { this.pontoFimId = pontoFimId; }
}
// hello world