package com.best_umbrella.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de Utilizador exposto pelos endpoints.
 *
 * Contém dados básicos do utilizador (identificação, contacto, registo, rating)
 * e coleções resumidas de alugueres (AluguerSummaryDto) e notificações (NotiDto).
 */
public class UtilizadorDto {
    private Integer utilizadorId;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime dataRegisto;
    private Double rating;

    private List<AluguerSummaryDto> alugueres;
    private List<NotiDto> notificacoes;
    private Boolean alertaChuvaAtivo;
    private String alertaCidade;
    private Double alertaLat;
    private Double alertaLon;

    public UtilizadorDto() {}

    public UtilizadorDto(Integer utilizadorId, String nome, String email, String telefone,
                         LocalDateTime dataRegisto, Double rating,
                         List<AluguerSummaryDto> alugueres, List<NotiDto> notificacoes,
                         Boolean alertaChuvaAtivo, String alertaCidade, Double alertaLat, Double alertaLon) {
        this.utilizadorId = utilizadorId;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataRegisto = dataRegisto;
        this.rating = rating;
        this.alugueres = alugueres;
        this.notificacoes = notificacoes;
        this.alertaChuvaAtivo = alertaChuvaAtivo;
        this.alertaCidade = alertaCidade;
        this.alertaLat = alertaLat;
        this.alertaLon = alertaLon;
    }

    public Integer getUtilizadorId() { return utilizadorId; }
    public void setUtilizadorId(Integer utilizadorId) { this.utilizadorId = utilizadorId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDateTime getDataRegisto() { return dataRegisto; }
    public void setDataRegisto(LocalDateTime dataRegisto) { this.dataRegisto = dataRegisto; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public List<AluguerSummaryDto> getAlugueres() { return alugueres; }
    public void setAlugueres(List<AluguerSummaryDto> alugueres) { this.alugueres = alugueres; }
    public List<NotiDto> getNotificacoes() { return notificacoes; }
    public void setNotificacoes(List<NotiDto> notificacoes) { this.notificacoes = notificacoes; }
    public Boolean getAlertaChuvaAtivo() { return alertaChuvaAtivo; }
    public void setAlertaChuvaAtivo(Boolean alertaChuvaAtivo) { this.alertaChuvaAtivo = alertaChuvaAtivo; }
    public String getAlertaCidade() { return alertaCidade; }
    public void setAlertaCidade(String alertaCidade) { this.alertaCidade = alertaCidade; }
    public Double getAlertaLat() { return alertaLat; }
    public void setAlertaLat(Double alertaLat) { this.alertaLat = alertaLat; }
    public Double getAlertaLon() { return alertaLon; }
    public void setAlertaLon(Double alertaLon) { this.alertaLon = alertaLon; }
}