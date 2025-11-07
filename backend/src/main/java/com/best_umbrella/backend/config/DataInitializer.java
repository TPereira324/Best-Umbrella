package com.best_umbrella.backend.config;

import com.best_umbrella.backend.model.Utilizador;
import com.best_umbrella.backend.service.UtilizadorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final UtilizadorService utilizadorService;

    public DataInitializer(UtilizadorService utilizadorService) {
        this.utilizadorService = utilizadorService;
    }

    @Override
    public void run(String... args) {
        // Garantir admin com email canónico e migrar do legado se existir
        ensureAdminCanonical();

    }

    private void ensureAdminCanonical() {
        String canonical = "admin@bestumbrella";
        String legacy = "admim@bestumbrella";
        Utilizador admin = utilizadorService.findByEmail(canonical);
        if (admin != null) {
            return; // já existe o admin com email correto
        }
        Utilizador legacyAdmin = utilizadorService.findByEmail(legacy);
        if (legacyAdmin != null) {
            legacyAdmin.setEmail(canonical);
            utilizadorService.save(legacyAdmin);
            return;
        }
        ensureUser("Administrador", canonical, "admin123", "910000000", 5.0);
    }

    private void ensureUser(String nome, String email, String rawPassword, String telefone, Double rating) {
        String normalizedEmail = email.trim().toLowerCase();
        Utilizador existing = utilizadorService.findByEmail(normalizedEmail);
        if (existing != null) {
            return;
        }

        Utilizador u = new Utilizador();
        u.setNome(nome);
        u.setEmail(normalizedEmail);
        u.setPassword(rawPassword);
        u.setTelefone(telefone);
        u.setRating(rating);
        u.setDataRegisto(LocalDateTime.now());
        utilizadorService.save(u);
    }
}