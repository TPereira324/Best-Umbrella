package com.best_umbrella.backend.controllers;

import com.best_umbrella.backend.dto.ReturnUploadResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class ReturnController {

    private final Path uploadDir = Paths.get("uploads", "returns");

    @PostMapping(path = "/returns", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReturnUploadResponse> uploadReturn(
            @RequestPart("image") MultipartFile image,
            @RequestPart("umbrellaId") String umbrellaId,
            @RequestPart(value = "notes", required = false) String notes
    ) {
        try {
            if (image == null || image.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ReturnUploadResponse(false, "Imagem obrigatória", null, null));
            }

            Files.createDirectories(uploadDir);
            long returnId = Instant.now().toEpochMilli();
            String original = StringUtils.cleanPath(image.getOriginalFilename() == null ? "uploaded" : image.getOriginalFilename());
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot);
            String filename = returnId + "-" + (umbrellaId == null ? "unknown" : umbrellaId) + ext;
            Path target = uploadDir.resolve(filename);
            Files.write(target, image.getBytes());

            String imageUrl = "/api/returns/" + returnId + "/image";
            ReturnUploadResponse resp = new ReturnUploadResponse(true, "Upload concluído", returnId, imageUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ReturnUploadResponse(false, "Falha ao guardar imagem", null, null));
        }
    }

    @GetMapping(value = "/returns/{returnId}/image")
    public ResponseEntity<byte[]> getReturnImage(@PathVariable("returnId") long returnId) {
        try {
            Optional<Path> file = findFileByReturnId(returnId);
            if (file.isEmpty()) return ResponseEntity.notFound().build();
            byte[] bytes = Files.readAllBytes(file.get());
            MediaType mt = guessMediaType(file.get());
            return ResponseEntity.ok().contentType(mt).body(bytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/returns/{returnId}/image/download")
    public ResponseEntity<byte[]> downloadReturnImage(@PathVariable("returnId") long returnId) {
        try {
            Optional<Path> file = findFileByReturnId(returnId);
            if (file.isEmpty()) return ResponseEntity.notFound().build();
            byte[] bytes = Files.readAllBytes(file.get());
            String fname = file.get().getFileName().toString();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fname + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Optional<Path> findFileByReturnId(long returnId) throws IOException {
        Files.createDirectories(uploadDir);
        String prefix = Long.toString(returnId) + "-";
        try (Stream<Path> stream = Files.list(uploadDir)) {
            return stream.filter(p -> p.getFileName().toString().startsWith(prefix))
                    .sorted(Comparator.comparing(Path::getFileName))
                    .findFirst();
        }
    }

    private MediaType guessMediaType(Path p) {
        String name = p.getFileName().toString().toLowerCase();
        if (name.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return MediaType.IMAGE_JPEG;
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}