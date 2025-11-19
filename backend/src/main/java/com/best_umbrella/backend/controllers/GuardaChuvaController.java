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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<List<GuardaChuvaDto>> getAllGuardaChuvas(
            @RequestParam(value = "estado", required = false) String estado
    ) {
        List<GuardaChuvaDto> dtos = (estado == null || estado.isBlank()
                ? guardaChuvaService.findAll()
                : guardaChuvaService.findByEstado(estado))
                .stream()
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

    @GetMapping(value = "/codigo/{codigoQr}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrPng(@PathVariable String codigoQr,
                                           @RequestParam(value = "size", defaultValue = "256") int size) {
        try {
            String deepLink = "bumb://rent?code=" + codigoQr;
            byte[] img = generateQrPng(deepLink, Math.max(128, Math.min(size, 1024)));
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(img);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<?> getDisponiveis() {
        List<GuardaChuva> disponiveis = guardaChuvaService.findDisponiveis();
        if (disponiveis == null || disponiveis.isEmpty()) {
            return ResponseEntity.ok("Ocupado: nenhum guarda-chuva disponível");
        }
        List<GuardaChuvaDto> dtos = disponiveis.stream().map(this::toDto).toList();
        return ResponseEntity.ok(dtos);
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
                gc.getCorId(),
                gc.getTipoId(),
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

    private static byte[] generateQrPng(String text, int size) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }
}
// hello world