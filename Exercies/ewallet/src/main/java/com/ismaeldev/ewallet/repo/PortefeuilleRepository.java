package com.ismaeldev.ewallet.repo;

import com.ismaeldev.ewallet.entity.Portefeuille;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortefeuilleRepository extends JpaRepository<Portefeuille, Long> {
}
