package com.ismaeldev.ecommerce.produits.dto;

import com.ismaeldev.ecommerce.category.entity.Categorie;

public record ProduitDTO(Long id, String nom, double prix, String nomCategorie) {
}