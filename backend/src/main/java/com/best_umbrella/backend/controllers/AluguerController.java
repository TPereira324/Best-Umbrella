package com.best_umbrella.backend.controllers;

import com.best_umbrella.backend.dto.AluguerDto;
import com.best_umbrella.backend.model.Aluguer;
import com.best_umbrella.backend.service.AluguerService;
import com.best_umbrella.backend.service.GuardaChuvaService;
import com.best_umbrella.backend.model.GuardaChuva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Aluguer")
/**
 * Controlador dos endpoints de Aluguer.
 *
 * Endpoints:
 * - GET /api/Aluguer                 → lista todos os alugueres
 * - GET /api/Aluguer/{id}            → obtém um aluguer por ID
 * - POST /api/Aluguer/start          → inicia um aluguer
 * - POST /api/Aluguer/{id}/end       → termina um aluguer
 */
public class AluguerController {

    private final AluguerService aluguerService;
    private final GuardaChuvaService guardaChuvaService;

    @Autowired
    public AluguerController(AluguerService aluguerService, GuardaChuvaService guardaChuvaService) {
        this.aluguerService = aluguerService;
        this.guardaChuvaService = guardaChuvaService;
    }

    @GetMapping
    public ResponseEntity<List<AluguerDto>> listAll() {
        List<AluguerDto> dtos = aluguerService.findAll().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AluguerDto> getById(@PathVariable Long id) {
        Optional<Aluguer> aluguer = aluguerService.findById(id);
        return aluguer.map(a -> ResponseEntity.ok(toDto(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/start")
    public ResponseEntity<?> start(
            @RequestParam Long utilizadorId,
            @RequestParam Integer guardaChuvaId,
            @RequestParam Integer pontoInicioId
    ) {
        try {
            Aluguer aluguer = aluguerService.iniciarAluguer(utilizadorId, guardaChuvaId, pontoInicioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(aluguer));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/start-by-qr")
    public ResponseEntity<?> startByQr(
            @RequestParam Long utilizadorId,
            @RequestParam String codigoQr,
            @RequestParam Integer pontoInicioId
    ) {
        try {
            GuardaChuva gc = guardaChuvaService.findByCodigoQr(codigoQr);
            if (gc == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guarda-chuva com QR não encontrado");
            }
            Aluguer aluguer = aluguerService.iniciarAluguer(utilizadorId, gc.getGuardaChuvaId(), pontoInicioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(aluguer));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/{aluguerId}/end")
    public ResponseEntity<?> end(
            @PathVariable Long aluguerId,
            @RequestParam Integer pontoFimId
    ) {
        try {
            Aluguer aluguer = aluguerService.terminarAluguer(aluguerId, pontoFimId);
            return ResponseEntity.ok(toDto(aluguer));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    private AluguerDto toDto(Aluguer a) {
        Integer uid = a.getUtilizador() != null ? a.getUtilizador().getUtilizadorId() : null;
        Integer gid = a.getGuardaChuva() != null ? a.getGuardaChuva().getGuardaChuvaId() : null;
        Integer inicioId = a.getPontoInicio() != null ? a.getPontoInicio().getPontoId() : null;
        Integer fimId = a.getPontoFim() != null ? a.getPontoFim().getPontoId() : null;
        return new AluguerDto(
                a.getAluguerId(),
                uid,
                gid,
                inicioId,
                fimId,
                a.getDataInicio(),
                a.getDataFim(),
                a.getCusto(),
                a.getEstado()
        );
    }
}