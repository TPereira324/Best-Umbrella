package com.best_umbrella.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.best_umbrella.backend.model.Multa;
import com.best_umbrella.backend.dto.MultaDto;
import com.best_umbrella.backend.service.MultaService;

@RestController
@RequestMapping("/api/Multa")
public class MultaController {

    private final MultaService multaService;

    @Autowired
    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }

    @GetMapping
    public ResponseEntity<List<MultaDto>> getAllMultas() {
        List<MultaDto> dtos = multaService.findAll().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultaDto> getMultaById(@PathVariable Long id) {
        Optional<Multa> multa = multaService.findById(id);
        return multa.map(m -> ResponseEntity.ok(toDto(m)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/utilizador/{utilizadorId}")
    public ResponseEntity<List<MultaDto>> getMultasByUtilizador(@PathVariable Long utilizadorId) {
        List<MultaDto> dtos = multaService.findByUtilizadorId(utilizadorId).stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/aluguer/{aluguerId}")
    public ResponseEntity<List<MultaDto>> getMultasByAluguer(@PathVariable Long aluguerId) {
        List<MultaDto> dtos = multaService.findByAluguerId(aluguerId).stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<MultaDto> createMulta(@RequestBody Multa multa) {
        Multa saved = multaService.save(multa);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    // Criação orientada a Aluguer (sem enviar entidades completas)
    @PostMapping("/atrelada")
    public ResponseEntity<MultaDto> createMultaAtrelada(
            @RequestParam Long aluguerId,
            @RequestParam Double valor,
            @RequestParam String motivo,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String moeda
    ) {
        Multa saved = multaService.criarMultaParaAluguer(aluguerId, valor, motivo, descricao, moeda);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<MultaDto> pagarMulta(@PathVariable Long id) {
        Optional<Multa> multa = multaService.pagarMulta(id);
        return multa.map(m -> ResponseEntity.ok(toDto(m)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<MultaDto> cancelarMulta(@PathVariable Long id) {
        Optional<Multa> multa = multaService.cancelarMulta(id);
        return multa.map(m -> ResponseEntity.ok(toDto(m)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private MultaDto toDto(Multa m) {
        Long uid = m.getUtilizador() != null ? Long.valueOf(m.getUtilizador().getUtilizadorId()) : null;
        Long aid = m.getAluguer() != null ? m.getAluguer().getAluguerId() : null;
        return new MultaDto(
                m.getMultaId(),
                uid,
                aid,
                m.getValor(),
                m.getMoeda(),
                m.getEstado(),
                m.getMotivo(),
                m.getDescricao(),
                m.getDataEmissao(),
                m.getDataVencimento(),
                m.getDataPagamento(),
                m.getJurosAcumulados(),
                m.getDescontoAplicado()
        );
    }
}