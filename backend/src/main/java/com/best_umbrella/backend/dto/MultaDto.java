package com.best_umbrella.backend.dto;

import java.time.LocalDateTime;

public class MultaDto {
    private Long multaId;
    private Long utilizadorId;
    private Long aluguerId;
    private Double valor;
    private String moeda;
    private String estado;
    private String motivo;
    private String descricao;
    private LocalDateTime dataEmissao;
    private LocalDateTime dataVencimento;
    private LocalDateTime dataPagamento;
    private Double jurosAcumulados;
    private Double descontoAplicado;

    public MultaDto() {}

    public MultaDto(Long multaId, Long utilizadorId, Long aluguerId, Double valor, String moeda,
                    String estado, String motivo, String descricao,
                    LocalDateTime dataEmissao, LocalDateTime dataVencimento, LocalDateTime dataPagamento,
                    Double jurosAcumulados, Double descontoAplicado) {
        this.multaId = multaId;
        this.utilizadorId = utilizadorId;
        this.aluguerId = aluguerId;
        this.valor = valor;
        this.moeda = moeda;
        this.estado = estado;
        this.motivo = motivo;
        this.descricao = descricao;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.jurosAcumulados = jurosAcumulados;
        this.descontoAplicado = descontoAplicado;
    }

    public Long getMultaId() { return multaId; }
    public void setMultaId(Long multaId) { this.multaId = multaId; }
    public Long getUtilizadorId() { return utilizadorId; }
    public void setUtilizadorId(Long utilizadorId) { this.utilizadorId = utilizadorId; }
    public Long getAluguerId() { return aluguerId; }
    public void setAluguerId(Long aluguerId) { this.aluguerId = aluguerId; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getMoeda() { return moeda; }
    public void setMoeda(String moeda) { this.moeda = moeda; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDateTime dataEmissao) { this.dataEmissao = dataEmissao; }
    public LocalDateTime getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDateTime dataVencimento) { this.dataVencimento = dataVencimento; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
    public Double getJurosAcumulados() { return jurosAcumulados; }
    public void setJurosAcumulados(Double jurosAcumulados) { this.jurosAcumulados = jurosAcumulados; }
    public Double getDescontoAplicado() { return descontoAplicado; }
    public void setDescontoAplicado(Double descontoAplicado) { this.descontoAplicado = descontoAplicado; }
}