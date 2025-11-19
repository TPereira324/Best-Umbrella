package com.best_umbrella.backend.config;

import com.best_umbrella.backend.model.Utilizador;
import com.best_umbrella.backend.service.UtilizadorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(1)
/**
 * Inicializador de dados de arranque.
 *
 * Corre no startup para garantir um conjunto mínimo de Utilizadores na base,
 * incluindo admin canónico e alguns utilizadores de demonstração.
 * Não altera configuração de porta/servidor.
 */
public class DataInitializer implements CommandLineRunner {

    private final UtilizadorService utilizadorService;

    public DataInitializer(UtilizadorService utilizadorService) {
        this.utilizadorService = utilizadorService;
    }

    @Override
    public void run(String... args) {
        // Garantir admin com email canónico e migrar do legado se existir
        ensureAdminCanonical();

        // Semear utilizadores pedidos (não altera porta do servidor)
        ensureUser("Taha-Wur Pereira", "taha@umbrella.pt", "12345", "912345678", 4.8);
        ensureUser("Joybeth Mateus", "joybeth@umbrella.pt", "12345", "913456789", 4.5);
        ensureUser("Márcio Quintas", "marcio@umbrella.pt", "12345", "914567890", 4.7);
        ensureUser("Feleciano Ramos", "feleciano@umbrella.pt", "12345", "915678901", 4.6);
        ensureUser("Fábio Teixeira", "fabio@umbrella.pt", "12345", "916789012", 4.4);
        ensureUser("Moira Silva", "moira@umbrella.pt", "12345", "917890123", 4.3);

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
// hello world