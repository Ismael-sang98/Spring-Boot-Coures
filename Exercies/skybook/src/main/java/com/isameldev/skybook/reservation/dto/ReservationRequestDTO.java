package com.isameldev.skybook.reservation.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationRequestDTO(
        @NotNull Long idVol,
        @NotBlank(message = "Le nom du passager est obligatoire")
        String nomPassager) {
}
