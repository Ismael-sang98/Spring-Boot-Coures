package com.ismaeldev.app_mysql.repository;

import com.ismaeldev.app_mysql.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
}
