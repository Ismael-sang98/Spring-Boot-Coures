package com.isameldev.skybook.reservation.service;


import com.isameldev.skybook.reservation.dto.ReservationRequestDTO;
import com.isameldev.skybook.reservation.dto.ReservationResponseDTO;
import com.isameldev.skybook.reservation.entity.Reservation;
import com.isameldev.skybook.reservation.repository.ReservationRepository;
import com.isameldev.skybook.vol.entity.Vol;
import com.isameldev.skybook.vol.repository.VolRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final VolRepository volRepository;

    public ReservationService(ReservationRepository repository, VolRepository volRepository) {
        this.repository = repository;
        this.volRepository = volRepository;
    }

    @Transactional
    public ReservationResponseDTO postReservation(ReservationRequestDTO reservationRequestDTO) throws IllegalArgumentException {
        Vol vol = volRepository.findById(reservationRequestDTO.idVol()).orElseThrow(
                () -> new IllegalArgumentException("Vol introuvable.")
        );
        if(vol.getPlacesDisponibles() <= 0){
            throw new IllegalArgumentException("Ce vol est complet !");
        }
        Reservation  reservation = new Reservation();
        reservation.setNomPassager(reservationRequestDTO.nomPassager());
        reservation.setVol(vol);
        Reservation save = repository.save(reservation);

        vol.setPlacesDisponibles(vol.getPlacesDisponibles()-1);
        volRepository.save(vol);

        return new ReservationResponseDTO(save.getId(), save.getNomPassager(), save.getVol().getDestination());
    }

    private List<ReservationResponseDTO> responseDTOS (List<Reservation> reservations){
        List<ReservationResponseDTO> list = new ArrayList<>();
        for (Reservation resev : reservations){
            ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO(
                    resev.getId(),
                    resev.getNomPassager(),
                    resev.getVol().getDestination()
            );
            list.add(reservationResponseDTO);
        }
        return list;
    }

    public List<ReservationResponseDTO> getReservation(){
        List<Reservation> reservations = repository.findAll();
        return responseDTOS(reservations);
    }
    public List<ReservationResponseDTO> getByNameReservation(String nom){
        List<Reservation> reservations = repository.findByNomPassager(nom);
        return responseDTOS(reservations);
    }
    public List<ReservationResponseDTO> getByDestinationVol(String destination){
        List<Reservation> reservations = repository.findByVol_Destination(destination);
        return responseDTOS(reservations);
    }

}
