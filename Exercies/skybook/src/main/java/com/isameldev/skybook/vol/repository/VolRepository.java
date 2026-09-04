package com.isameldev.skybook.vol.repository;

import com.isameldev.skybook.vol.entity.Vol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolRepository extends JpaRepository<Vol, Long> {
    List<Vol> findByDestination(String destination);
}
