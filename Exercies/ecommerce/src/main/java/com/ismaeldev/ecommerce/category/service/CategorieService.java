package com.ismaeldev.ecommerce.category.service;

import com.ismaeldev.ecommerce.category.dto.CategorieDTO;
import com.ismaeldev.ecommerce.category.entity.Categorie;
import com.ismaeldev.ecommerce.category.repository.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository repository;
    public CategorieService(CategorieRepository repository) {
        this.repository = repository;
    }

    public CategorieDTO creeCat(CategorieDTO NouvelCategorie){
        Categorie cat = new Categorie();
        cat.setNom(NouvelCategorie.nom());

        Categorie save = repository.save(cat);
        return new CategorieDTO(save.getNom());
    }

    public CategorieDTO modifierCat (Long id, CategorieDTO nouveauNom){
        Categorie cat = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Catégorie introuvable.")
        );
        cat.setNom(nouveauNom.nom());

        Categorie save = repository.save(cat);
        return new CategorieDTO(save.getNom());
    }

    public List<CategorieDTO> listeCat (){
        List<Categorie> cat = repository.findAll();

        List<CategorieDTO> liste = new ArrayList<>();
        for (Categorie c : cat){
            CategorieDTO categorieDTO = new CategorieDTO(c.getNom());
            liste.add(categorieDTO);
        }
        return liste;

    }
}
