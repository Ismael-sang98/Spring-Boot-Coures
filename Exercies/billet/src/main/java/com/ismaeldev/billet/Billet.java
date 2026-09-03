package com.ismaeldev.billet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Billet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomFilm;
    private int numeroSiege;
    private double prix;

    public Billet (){}

    public Billet(String nomFilm, int numeroSiege, double prix) {
        this.nomFilm = nomFilm;
        this.numeroSiege = numeroSiege;
        this.prix = prix;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFilm() {
        return nomFilm;
    }

    public void setNomFilm(String nomFilm) {
        this.nomFilm = nomFilm;
    }

    public int getNumeroSiege() {
        return numeroSiege;
    }

    public void setNumeroSiege(int numeroSiege) {
        this.numeroSiege = numeroSiege;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
