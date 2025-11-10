package com.best_umbrella.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.best_umbrella.backend.model.GuardaChuva;
import com.best_umbrella.backend.model.Aluguer;
import com.best_umbrella.backend.model.PontodeAluguer;
import com.best_umbrella.backend.dto.GuardaChuvaDto;
import com.best_umbrella.backend.dto.AluguerSummaryDto;
import com.best_umbrella.backend.service.GuardaChuvaService;

@RestController
@RequestMapping("/api/guardachuvas")
/**
 * Controlador dos endpoints de Guarda-Chuva.
 *
 * Endpoints principais:
 * - GET /api/guardachuvas                  → lista todos os guarda-chuvas
 * - GET /api/guardachuvas/{id}             → obtém um guarda-chuva por ID
 * - GET /api/guardachuvas/codigo/{codigo}  → obtém por código QR
 * - POST /api/guardachuvas                 → cria um novo guarda-chuva
 * - PUT /api/guardachuvas/{id}             → atualiza um guarda-chuva existente
 * - DELETE /api/guardachuvas/{id}          → remove o guarda-chuva
 *
 * As respostas utilizam GuardaChuvaDto e referências de Aluguer via AluguerSummaryDto.
 */
public class GuardaChuvaController {

    private final GuardaChuvaService guardaChuvaService;

    @Autowired
    public GuardaChuvaController(GuardaChuvaService guardaChuvaService) {
        this.guardaChuvaService = guardaChuvaService;
    }

    @GetMapping
    public ResponseEntity<List<GuardaChuvaDto>> getAllGuardaChuvas() {
        List<GuardaChuvaDto> dtos = guardaChuvaService.findAll().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuardaChuvaDto> getGuardaChuvaById(@PathVariable Integer id) {
        Optional<GuardaChuva> guardaChuva = guardaChuvaService.findById(id);
        return guardaChuva.map(gc -> ResponseEntity.ok(toDto(gc)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigoQr}")
    public ResponseEntity<GuardaChuvaDto> getGuardaChuvaByCodigoQr(@PathVariable String codigoQr) {
        GuardaChuva guardaChuva = guardaChuvaService.findByCodigoQr(codigoQr);
        if (guardaChuva != null) {
            return ResponseEntity.ok(toDto(guardaChuva));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GuardaChuvaDto> createGuardaChuva(@RequestBody GuardaChuva guardaChuva) {
        GuardaChuva savedGuardaChuva = guardaChuvaService.save(guardaChuva);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(savedGuardaChuva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuardaChuvaDto> updateGuardaChuva(@PathVariable Integer id, @RequestBody GuardaChuva guardaChuva) {
        if (!guardaChuvaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        guardaChuva.setGuardaChuvaId(id);
        GuardaChuva saved = guardaChuvaService.save(guardaChuva);
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuardaChuva(@PathVariable Integer id) {
        Optional<GuardaChuva> guardaChuva = guardaChuvaService.findById(id);
        if (guardaChuva.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        guardaChuvaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private GuardaChuvaDto toDto(GuardaChuva gc) {
        Integer pontoId = gc.getPontodeAluguer() != null ? gc.getPontodeAluguer().getPontoId() : null;
        List<AluguerSummaryDto> aluguerDtos = gc.getAlugueres() == null ? List.of() : gc.getAlugueres().stream()
                .map(this::toDto)
                .toList();
        return new GuardaChuvaDto(
                gc.getGuardaChuvaId(),
                gc.getCodigoQr(),
                gc.getEstado(),
                gc.getCor(),
                gc.getTipo(),
                gc.getDataRegisto(),
                pontoId,
                aluguerDtos
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
}