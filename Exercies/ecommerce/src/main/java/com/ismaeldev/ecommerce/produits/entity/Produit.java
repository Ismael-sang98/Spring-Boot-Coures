package com.ismaeldev.ecommerce.produits.entity;

import com.ismaeldev.ecommerce.category.entity.Categorie;
import jakarta.persistence.*;

@Entity
public class Produit {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private double prix;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    public Produit(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}
