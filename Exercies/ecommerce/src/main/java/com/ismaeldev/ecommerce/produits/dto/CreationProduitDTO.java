package com.ismaeldev.ecommerce.produits.dto;

import com.ismaeldev.ecommerce.category.entity.Categorie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

public record CreationProduitDTO(
        @NotBlank(message = "Le nom du produit est obligatoire")
        String nom,
        @Positive(message = "Le prix doit être strictement supérieur à zéro")
        double prix,
        @NotNull(message = "L'ID de catégorie est obligatoire")
        Long idCategorie) {
}
