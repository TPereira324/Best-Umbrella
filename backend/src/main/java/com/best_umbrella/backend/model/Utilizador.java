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
@Table(name = "Utilizador")
public class Utilizador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utilizador_id")
    private Integer utilizadorId;

    @Column(name = "nome")
    private String nome;

    
    @Column(unique = true)
    private String email;

    //cumpridor
    private String password;

    private String telefone;

    @Column(name = "data_registo")
    private LocalDateTime dataRegisto;

    private Double rating;

    @OneToMany(mappedBy = "utilizador", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Aluguer> alugueres;

    @OneToMany(mappedBy = "utilizador", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Noti> notificacoes;

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
}