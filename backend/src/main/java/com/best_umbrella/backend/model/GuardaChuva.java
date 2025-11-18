package com.best_umbrella.backend.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "guardachuva")
public class GuardaChuva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gchuva_id")
    private Integer guardaChuvaId;

    @Column(name = "gchuva_num", unique = true, nullable = false)
    private String codigoQr;

    @Column(name = "gchuva_cor_id")
    private Integer corId;

    @Column(name = "gchuva_tipo_id")
    private Integer tipoId;

    @Column(name = "gchuva_datareg")
    private LocalDateTime dataRegisto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "est_id")
    @JsonBackReference
    private PontodeAluguer pontodeAluguer;

    @OneToMany(mappedBy = "guardaChuva", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Aluguer> alugueres;

    // Getters e Setters
    public Integer getGuardaChuvaId() {
        return guardaChuvaId;
    }

    public void setGuardaChuvaId(Integer guardaChuvaId) {
        this.guardaChuvaId = guardaChuvaId;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public Integer getCorId() {
        return corId;
    }

    public void setCorId(Integer corId) {
        this.corId = corId;
    }

    public Integer getTipoId() {
        return tipoId;
    }

    public void setTipoId(Integer tipoId) {
        this.tipoId = tipoId;
    }

    public LocalDateTime getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(LocalDateTime dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    public PontodeAluguer getPontodeAluguer() {
        return pontodeAluguer;
    }

    public void setPontodeAluguer(PontodeAluguer pontodeAluguer) {
        this.pontodeAluguer = pontodeAluguer;
    }

    public List<Aluguer> getAlugueres() {
        return alugueres;
    }

    public void setAlugueres(List<Aluguer> alugueres) {
        this.alugueres = alugueres;
    }
}