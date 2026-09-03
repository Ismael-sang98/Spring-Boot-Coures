package com.ismaeldev.ecommerce.produits.repository;

import com.ismaeldev.ecommerce.produits.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByNom(String nom);
}
