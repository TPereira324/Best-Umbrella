package com.best_umbrella.backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.FetchType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
public class Noti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_id")
    private Long notificacaoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ut_not_id")
    @JsonBackReference
    private Utilizador utilizador;

    @Column(name = "not_msg", nullable = false)
    private String mensagem;
    
    private String tipo;

    @Column(name = "dataenv")
    private LocalDateTime dataEnvio;

    private String estado;

    // Getters e Setters
    public Long getNotificacaoId() {
        return notificacaoId;
    }

    public void setNotificacaoId(Long notificacaoId) {
        this.notificacaoId = notificacaoId;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
