package com.ismaeldev.ecommerce.category.entity;

import com.ismaeldev.ecommerce.produits.entity.Produit;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Categorie {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;
    String nom;

    public Categorie (){}

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

    @OneToMany(mappedBy = "categorie")
    private Collection<Produit> produits;

    public Collection<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Collection<Produit> produits) {
        this.produits = produits;
    }
}
