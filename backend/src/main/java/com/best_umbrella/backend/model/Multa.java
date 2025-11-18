package com.best_umbrella.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "multa")
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mul_id")
    private Long multaId;

    @ManyToOne
    @JoinColumn(name = "mul_ut_id")
    private Utilizador utilizador;

    @ManyToOne
    @JoinColumn(name = "ugem_id")
    private Aluguer aluguer;

    @Column(name = "mul_valor")
    private Double valor;
    private String  moeda;
    private String estado;
    @Column(name = "mul_mot")
    private String motivo;
    private String descricao;

    @Column(name = "mul_dataem")
    private LocalDateTime dataEmissao;

    @Column(name = "mul_dataven")
    private LocalDateTime dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    private Double jurosAcumulados;
    private Double descontoAplicado;

    // Getters e Setters
    public Long getMultaId() {
        return multaId;
    }

    public void setMultaId(Long multaId) {
        this.multaId = multaId;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public Aluguer getAluguer() {
        return aluguer;
    }

    public void setAluguer(Aluguer aluguer) {
        this.aluguer = aluguer;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDateTime getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDateTime dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Double getJurosAcumulados() {
        return jurosAcumulados;
    }

    public void setJurosAcumulados(Double jurosAcumulados) {
        this.jurosAcumulados = jurosAcumulados;
    }

    public Double getDescontoAplicado() {
        return descontoAplicado;
    }

    public void setDescontoAplicado(Double descontoAplicado) {
        this.descontoAplicado = descontoAplicado;
    }
}
