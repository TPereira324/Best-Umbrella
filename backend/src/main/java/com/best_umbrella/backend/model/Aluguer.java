package com.best_umbrella.backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

@Entity
@Table(name = "ugem")
public class Aluguer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ugem_id")
    private Long aluguerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ugem_ut_id")
    @JsonBackReference
    private Utilizador utilizador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ugem_chuva_id")
    @JsonBackReference
    private GuardaChuva guardaChuva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_inicio_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonBackReference
    private PontodeAluguer pontoInicio;

    @ManyToOne(fetch = FetchType.LAZY)
    private PontodeAluguer pontoFim;

    @Column(name = "ugem_datein")
    private LocalDateTime dataInicio;

    @Column(name = "ugem_datout")
    private LocalDateTime dataFim;


    // return Date
    @Transient
    private Double custo;
    @Transient
    private String estado;

    // Getters e Setters
    public Long getAluguerId() {
        return aluguerId;
    }

    public void setAluguerId(Long aluguerId) {
        this.aluguerId = aluguerId;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public GuardaChuva getGuardaChuva() {
        return guardaChuva;
    }

    public void setGuardaChuva(GuardaChuva guardaChuva) {
        this.guardaChuva = guardaChuva;
    }

    public PontodeAluguer getPontoInicio() {
        return pontoInicio;
    }

    public void setPontoInicio(PontodeAluguer pontoInicio) {
        this.pontoInicio = pontoInicio;
    }

    public PontodeAluguer getPontoFim() {
        return pontoFim;
    }

    public void setPontoFim(PontodeAluguer pontoFim) {
        this.pontoFim = pontoFim;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
// hello