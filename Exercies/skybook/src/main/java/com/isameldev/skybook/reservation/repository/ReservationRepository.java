package com.isameldev.skybook.reservation.repository;

import com.isameldev.skybook.reservation.entity.Reservation;
import com.isameldev.skybook.vol.entity.Vol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByNomPassager(String nomPassager);
    List<Reservation> findByVol_Destination(String volDestination);
}
