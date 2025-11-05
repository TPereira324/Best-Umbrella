package com.best_umbrella.backend.controllers;

import com.best_umbrella.backend.dto.RentalStationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/rental-stations")
public class RentalStationController {

    @GetMapping
    public ResponseEntity<List<RentalStationDto>> listStations() {
        List<RentalStationDto> rentalStations = Arrays.asList(
            new RentalStationDto(1, "IADE", 38.7818, -9.10251, 3, 6),
            new RentalStationDto(2, "Parque das Nações", 38.76800, -9.09400, 6, 10),
            new RentalStationDto(3, "Metro Moscavide", 38.77639, -9.10169, 8, 10),
            new RentalStationDto(4, "Metro Oriente", 38.76784, -9.09935, 4, 8),
            new RentalStationDto(5, "Terreiro do Paço", 38.7073, -9.1367, 10, 15),
            new RentalStationDto(6, "Rossio", 38.713718, -9.139681, 7, 12),
            new RentalStationDto(7, "Baixa-Chiado", 38.71056, -9.14000, 8, 12),
            new RentalStationDto(8, "Marquês de Pombal", 38.724686, -9.150442, 12, 20)
        );
        return ResponseEntity.ok(rentalStations);
    }
}