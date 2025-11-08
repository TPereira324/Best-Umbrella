package com.best_umbrella.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class RegisterRequest {
    @JsonAlias({"name"})
    private String nome;
    private String email;
    private String password;
    @JsonAlias({"telemovel", "phone", "telefone"})
    private String telefone;

    public RegisterRequest() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}