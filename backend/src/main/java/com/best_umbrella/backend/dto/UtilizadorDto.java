package com.best_umbrella.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UtilizadorDto {
    private Integer utilizadorId;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime dataRegisto;
    private Double rating;

    private List<AluguerSummaryDto> alugueres;
    private List<NotiDto> notificacoes;

    public UtilizadorDto() {}

    public UtilizadorDto(Integer utilizadorId, String nome, String email, String telefone,
                         LocalDateTime dataRegisto, Double rating,
                         List<AluguerSummaryDto> alugueres, List<NotiDto> notificacoes) {
        this.utilizadorId = utilizadorId;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataRegisto = dataRegisto;
        this.rating = rating;
        this.alugueres = alugueres;
        this.notificacoes = notificacoes;
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
}