package com.best_umbrella.backend.controllers;

import com.best_umbrella.backend.dto.LoginRequest;
import com.best_umbrella.backend.dto.RegisterRequest;
import com.best_umbrella.backend.dto.UtilizadorDto;
import com.best_umbrella.backend.model.Utilizador;
import com.best_umbrella.backend.service.UtilizadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
/**
 * Controlador de autenticação legado (compatibilidade).
 *
 * Endpoints:
 * - POST /users/register → regista um novo utilizador
 * - POST /users/login    → autentica e devolve UtilizadorDto
 *
 * Mantido para compatibilidade com clientes antigos que usam base "/users".
 */
public class LegacyAuthController {

    private final UtilizadorService utilizadorService;
    private final PasswordEncoder passwordEncoder;

    public LegacyAuthController(UtilizadorService utilizadorService, PasswordEncoder passwordEncoder) {
        this.utilizadorService = utilizadorService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<UtilizadorDto> register(@RequestBody RegisterRequest request) {
        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();
        Utilizador existing = email == null ? null : utilizadorService.findByEmail(email);
        if (existing != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Utilizador novo = new Utilizador();
        novo.setNome(request.getNome());
        novo.setEmail(email);
        novo.setTelefone(request.getTelefone());
        novo.setPassword(request.getPassword());

        Utilizador saved = utilizadorService.save(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<UtilizadorDto> login(@RequestBody LoginRequest request) {
        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();
        Utilizador user = email == null ? null : utilizadorService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!isBCryptHash(user.getPassword())) {
            if (!request.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.ok(toDto(user));
    }

    private UtilizadorDto toDto(Utilizador u) {
        return new UtilizadorDto(
                u.getUtilizadorId(),
                u.getNome(),
                u.getEmail(),
                u.getTelefone(),
                u.getDataRegisto(),
                u.getRating(),
                u.getAlugueres() == null ? java.util.List.of() : u.getAlugueres().stream().map(a -> new com.best_umbrella.backend.dto.AluguerSummaryDto(
                        a.getAluguerId(),
                        a.getDataInicio(),
                        a.getDataFim(),
                        a.getCusto(),
                        a.getEstado(),
                        a.getGuardaChuva() != null ? a.getGuardaChuva().getGuardaChuvaId() : null,
                        a.getPontoInicio() != null ? a.getPontoInicio().getPontoId() : null,
                        a.getPontoFim() != null ? a.getPontoFim().getPontoId() : null
                )).toList(),
                u.getNotificacoes() == null ? java.util.List.of() : u.getNotificacoes().stream().map(n -> new com.best_umbrella.backend.dto.NotiDto(
                        n.getNotificacaoId(), n.getMensagem(), n.getTipo(), n.getDataEnvio(), n.getEstado()
                )).toList(),
                u.getAlertaChuvaAtivo(),
                u.getAlertaCidade(),
                u.getAlertaLat(),
                u.getAlertaLon()
        );
    }

    private boolean isBCryptHash(String value) {
        if (value == null) return false;
        return (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"))
                && value.length() >= 60;
    }
}