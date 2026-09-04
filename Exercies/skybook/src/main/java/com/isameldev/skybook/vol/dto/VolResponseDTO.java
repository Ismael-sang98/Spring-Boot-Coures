package com.isameldev.skybook.vol.dto;

import com.isameldev.skybook.reservation.dto.ReservationRequestDTO;

import java.util.List;

public record VolResponseDTO(String destination, double prix, int placeDisponible) {
}
