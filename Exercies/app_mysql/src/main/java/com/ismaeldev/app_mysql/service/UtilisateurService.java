package com.ismaeldev.app_mysql.service;

import com.ismaeldev.app_mysql.dto.CompteUtilisateurDTO;
import com.ismaeldev.app_mysql.dto.CreationUtilisateurDTO;
import com.ismaeldev.app_mysql.entity.Utilisateur;
import com.ismaeldev.app_mysql.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UtilisateurService {
    private final UtilisateurRepository repository;

    public UtilisateurService(UtilisateurRepository repository) {
        this.repository = repository;
    }

    public CompteUtilisateurDTO creation (CreationUtilisateurDTO compte){
        Utilisateur user = new Utilisateur();
        user.setPseudo(compte.pseudo());
        user.setEmail(compte.email());
        user.setMotDepasse(compte.mpt());
        Utilisateur save = repository.save(user);
        return new CompteUtilisateurDTO(save.getPseudo(),save.getEmail());
    }

    public CompteUtilisateurDTO profilById (Long id){
        Utilisateur user = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Compte indisponible")
        );
        return new CompteUtilisateurDTO(user.getPseudo(), user.getEmail());
    }

    public List<CompteUtilisateurDTO> allProfil(){
        List<Utilisateur> users = repository.findAll();

        List<CompteUtilisateurDTO> profils = new ArrayList<>();

        for(Utilisateur user : users) {
            CompteUtilisateurDTO list = new CompteUtilisateurDTO(
                    user.getPseudo(),
                    user.getEmail()
            );
            profils.add(list);
        }
        return profils;
    }

    public String supprimer (Long id) throws IllegalArgumentException {
        Utilisateur user = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Compte introuvable.")
        );
        repository.delete(user);
        return "Compte supprimé avec succès";
    }

    public CompteUtilisateurDTO modifier(Long id, CreationUtilisateurDTO compte){
        Utilisateur user = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Compte introuvable.")
        );
        user.setPseudo(compte.pseudo());
        user.setEmail(compte.email());

        Utilisateur save = repository.save(user);

        return new CompteUtilisateurDTO(save.getPseudo(), save.getEmail());
    }



}
