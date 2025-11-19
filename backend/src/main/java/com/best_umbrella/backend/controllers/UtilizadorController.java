package com.best_umbrella.backend.controllers;

import com.best_umbrella.backend.model.Utilizador;
import com.best_umbrella.backend.model.Aluguer;
import com.best_umbrella.backend.model.Noti;
import com.best_umbrella.backend.dto.UtilizadorDto;
import com.best_umbrella.backend.dto.AluguerSummaryDto;
import com.best_umbrella.backend.dto.NotiDto;
import com.best_umbrella.backend.service.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.best_umbrella.backend.dto.NomeUpdateRequest;

@RestController
@RequestMapping("/api/utilizadores")
/**
 * Controlador dos endpoints de Utilizador.
 *
 * Endpoints principais:
 * - GET /api/Utilizador              → lista todos os utilizadores
 * - GET /api/Utilizador/{id}         → obtém um utilizador por ID
 * - POST /api/Utilizador             → cria um novo utilizador
 * - PUT /api/Utilizador/{id}         → atualiza um utilizador existente
 * - PATCH /api/Utilizador/{id}/nome  → atualiza apenas o nome
 * - DELETE /api/Utilizador/{id}      → remove o utilizador
 *
 * Todas as respostas utilizam UtilizadorDto.
 */
public class UtilizadorController {

    private final UtilizadorService utilizadorService;

    @Autowired
    public UtilizadorController(UtilizadorService utilizadorService) {

        this.utilizadorService = utilizadorService;
    }

    @GetMapping
    public ResponseEntity<List<UtilizadorDto>> getAllUtilizadores() {
        List<UtilizadorDto> dtos = utilizadorService.findAll().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilizadorDto> getUtilizadorById(@PathVariable Long id) {
        Optional<Utilizador> utilizador = utilizadorService.findById(Long.valueOf(id));
        return utilizador.map(u -> ResponseEntity.ok(toDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UtilizadorDto> createUtilizador(@RequestBody Utilizador utilizador) {
        Utilizador savedUtilizador = utilizadorService.save(utilizador);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(savedUtilizador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilizadorDto> updateUtilizador(@PathVariable Long id, @RequestBody Utilizador utilizador) {
        if (!utilizadorService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        utilizador.setUtilizadorId(Math.toIntExact(id));
        Utilizador saved = utilizadorService.save(utilizador);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}/alerta-chuva")
    public ResponseEntity<UtilizadorDto> setAlertaChuva(@PathVariable Long id, @RequestParam("ativo") boolean ativo) {
        Optional<Utilizador> opt = utilizadorService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Utilizador u = opt.get();
        u.setAlertaChuvaAtivo(ativo);
        Utilizador saved = utilizadorService.save(u);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}/alerta-local/cidade")
    public ResponseEntity<UtilizadorDto> setAlertaCidade(@PathVariable Long id, @RequestParam("cidade") String cidade) {
        Optional<Utilizador> opt = utilizadorService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Utilizador u = opt.get();
        u.setAlertaCidade(cidade);
        // limpar coords se cidade definida
        u.setAlertaLat(null);
        u.setAlertaLon(null);
        Utilizador saved = utilizadorService.save(u);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}/alerta-local/coords")
    public ResponseEntity<UtilizadorDto> setAlertaCoords(@PathVariable Long id,
                                                         @RequestParam("lat") Double lat,
                                                         @RequestParam("lon") Double lon) {
        Optional<Utilizador> opt = utilizadorService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Utilizador u = opt.get();
        u.setAlertaLat(lat);
        u.setAlertaLon(lon);
        // limpar cidade se coords definidas
        u.setAlertaCidade(null);
        Utilizador saved = utilizadorService.save(u);
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilizador(@PathVariable Long id) {
        if (!utilizadorService.findById(Long.valueOf(id)).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        utilizadorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/nome")
    public ResponseEntity<UtilizadorDto> updateNome(@PathVariable Long id, @RequestBody NomeUpdateRequest request) {
        Optional<Utilizador> opt = utilizadorService.findById(Long.valueOf(id));
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Utilizador u = opt.get();
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        u.setNome(request.getNome().trim());
        Utilizador saved = utilizadorService.save(u);
        return ResponseEntity.ok(toDto(saved));
    }

    private UtilizadorDto toDto(Utilizador u) {
        List<AluguerSummaryDto> aluguerDtos = u.getAlugueres() == null ? List.of() : u.getAlugueres().stream()
                .map(this::toDto)
                .toList();
        List<NotiDto> notis = u.getNotificacoes() == null ? List.of() : u.getNotificacoes().stream()
                .map(this::toDto)
                .toList();
        return new UtilizadorDto(
                u.getUtilizadorId(),
                u.getNome(),
                u.getEmail(),
                u.getTelefone(),
                u.getDataRegisto(),
                u.getRating(),
                aluguerDtos,
                notis,
                u.getAlertaChuvaAtivo(),
                u.getAlertaCidade(),
                u.getAlertaLat(),
                u.getAlertaLon()
        );
    }

    private AluguerSummaryDto toDto(Aluguer a) {
        Integer guardaId = a.getGuardaChuva() != null ? a.getGuardaChuva().getGuardaChuvaId() : null;
        Integer inicioId = a.getPontoInicio() != null ? a.getPontoInicio().getPontoId() : null;
        Integer fimId = a.getPontoFim() != null ? a.getPontoFim().getPontoId() : null;
        return new AluguerSummaryDto(
                a.getAluguerId(),
                a.getDataInicio(),
                a.getDataFim(),
                a.getCusto(),
                a.getEstado(),
                guardaId,
                inicioId,
                fimId
        );
    }

    private NotiDto toDto(Noti n) {
        return new NotiDto(n.getNotificacaoId(), n.getMensagem(), n.getTipo(), n.getDataEnvio(), n.getEstado());
    }
}
// hello world