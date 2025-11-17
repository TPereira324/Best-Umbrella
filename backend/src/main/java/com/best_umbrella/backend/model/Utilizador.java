package com.best_umbrella.backend.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "utilizador")
public class Utilizador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ut_id")
    private Integer utilizadorId;

    @Column(name = "ut_name")
    private String nome;

    @Column(name = "ut_email", unique = true)
    private String email;

    //cumpridor
    @Column(name = "ut_password")
    private String password;

    @Column(name = "ut_telefone")
    private String telefone;

    @Column(name = "ut_datareg")
    private LocalDateTime dataRegisto;

    @Column(name = "ut_rating")
    private Double rating;

    @OneToMany(mappedBy = "utilizador", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Aluguer> alugueres;

    @OneToMany(mappedBy = "utilizador", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Noti> notificacoes;

    @Column(name = "alerta_chuva_ativo")
    private Boolean alertaChuvaAtivo;

    @Column(name = "alerta_cidade")
    private String alertaCidade;

    @Column(name = "alerta_lat")
    private Double alertaLat;

    @Column(name = "alerta_lon")
    private Double alertaLon;

    // Getters e Setters
    public Integer getUtilizadorId() {
        return utilizadorId;
    }

    public void setUtilizadorId(Integer utilizadorId) {
        this.utilizadorId = utilizadorId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(LocalDateTime dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<Aluguer> getAlugueres() {
        return alugueres;
    }

    public void setAlugueres(List<Aluguer> alugueres) {
        this.alugueres = alugueres;
    }

    public List<Noti> getNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(List<Noti> notificacoes) {
        this.notificacoes = notificacoes;
    }

    public Boolean getAlertaChuvaAtivo() { return alertaChuvaAtivo; }
    public void setAlertaChuvaAtivo(Boolean alertaChuvaAtivo) { this.alertaChuvaAtivo = alertaChuvaAtivo; }

    public String getAlertaCidade() { return alertaCidade; }
    public void setAlertaCidade(String alertaCidade) { this.alertaCidade = alertaCidade; }

    public Double getAlertaLat() { return alertaLat; }
    public void setAlertaLat(Double alertaLat) { this.alertaLat = alertaLat; }

    public Double getAlertaLon() { return alertaLon; }
    public void setAlertaLon(Double alertaLon) { this.alertaLon = alertaLon; }
}