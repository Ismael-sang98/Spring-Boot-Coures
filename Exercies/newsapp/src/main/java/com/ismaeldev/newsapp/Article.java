package com.ismaeldev.newsapp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Article {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String contenu;
    private String auteur;
    private String noteSecretes;

   public Article(){}

    public Article(String titre, String contenu,String auteur, String noteSecretes) {
        this.titre = titre;
        this.contenu = contenu;
        this.auteur = auteur;
        this.noteSecretes = noteSecretes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getNoteSecretes() {
        return noteSecretes;
    }

    public void setNoteSecretes(String noteSecretes) {
        this.noteSecretes = noteSecretes;
    }
}
