package com.best_umbrella.backend.controllers;

import com.best_umbrella.backend.dto.PontoDeAluguerDto;
import com.best_umbrella.backend.model.PontodeAluguer;
import com.best_umbrella.backend.repository.PontodeAluguerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/PontoDeAluguer")
/**
 * Controlador dos endpoints de Pontos de Aluguer.
 *
 * Endpoints:
 * - GET /api/PontoDeAluguer          → lista todos os pontos
 * - GET /api/PontoDeAluguer/{id}     → obtém um ponto por ID
 */
public class PontoDeAluguerController {

    private final PontodeAluguerRepository pontodeAluguerRepository;

    @Autowired
    public PontoDeAluguerController(PontodeAluguerRepository pontodeAluguerRepository) {
        this.pontodeAluguerRepository = pontodeAluguerRepository;
    }

    @GetMapping
    public ResponseEntity<List<PontoDeAluguerDto>> listAll() {
        List<PontoDeAluguerDto> dtos = pontodeAluguerRepository.findAll().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoDeAluguerDto> getById(@PathVariable Integer id) {
        Optional<PontodeAluguer> ponto = pontodeAluguerRepository.findById(Long.valueOf(id));
        return ponto.map(p -> ResponseEntity.ok(toDto(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private PontoDeAluguerDto toDto(PontodeAluguer p) {
        int quantidade = (p.getGuardaChuvas() == null) ? 0 : p.getGuardaChuvas().size();
        return new PontoDeAluguerDto(
                p.getPontoId(),
                p.getNome(),
                p.getLatitude(),
                p.getLongitude(),
                p.getCapacidade(),
                p.getTipo(),
                quantidade
        );
    }
}