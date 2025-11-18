package com.best_umbrella.backend.dto;

import java.time.LocalDateTime;

public class NotiDto {
    private Long notificacaoId;
    private String mensagem;
    private String tipo;
    private LocalDateTime dataEnvio;
    private String estado;

    public NotiDto() {}

    public NotiDto(Long notificacaoId, String mensagem, String tipo, LocalDateTime dataEnvio, String estado) {
        this.notificacaoId = notificacaoId;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.dataEnvio = dataEnvio;
        this.estado = estado;
    }

    public Long getNotificacaoId() { return notificacaoId; }
    public void setNotificacaoId(Long notificacaoId) { this.notificacaoId = notificacaoId; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
// hello