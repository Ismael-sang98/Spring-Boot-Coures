package com.isameldev.skybook.reservation.controller;

import com.isameldev.skybook.reservation.dto.ReservationRequestDTO;
import com.isameldev.skybook.reservation.dto.ReservationResponseDTO;
import com.isameldev.skybook.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> postReservation(@Valid @RequestBody ReservationRequestDTO reservation){
        return ResponseEntity.ok(service.postReservation(reservation));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getReservation(){
        return ResponseEntity.ok(service.getReservation());
    }
    @GetMapping("/recherche/nom")
    public ResponseEntity<List<ReservationResponseDTO>> getByNamReservation(@RequestParam String nom){
        return ResponseEntity.ok(service.getByNameReservation(nom));
    }
    @GetMapping("/recherche/destination")
    public ResponseEntity<List<ReservationResponseDTO>> getfindByDestinationVol(@RequestParam (value = "destination") String destination){
        return ResponseEntity.ok(service.getByDestinationVol(destination));
    }

}
