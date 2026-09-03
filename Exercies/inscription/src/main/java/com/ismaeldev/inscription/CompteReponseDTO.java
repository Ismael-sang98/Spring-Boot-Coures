package com.ismaeldev.inscription;

public class CompteReponseDTO {
    String pseudo;
    String email;

    public CompteReponseDTO(String pseudo, String email) {
        this.pseudo = pseudo;
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEmail() {
        return email;
    }
}
