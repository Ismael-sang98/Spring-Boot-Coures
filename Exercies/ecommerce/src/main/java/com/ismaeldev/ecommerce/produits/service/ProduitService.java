package com.ismaeldev.ecommerce.produits.service;

import com.ismaeldev.ecommerce.category.entity.Categorie;
import com.ismaeldev.ecommerce.category.repository.CategorieRepository;
import com.ismaeldev.ecommerce.produits.dto.CreationProduitDTO;
import com.ismaeldev.ecommerce.produits.dto.ProduitDTO;
import com.ismaeldev.ecommerce.produits.entity.Produit;
import com.ismaeldev.ecommerce.produits.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProduitService {
    private final CategorieRepository categorieRepository;
    private final ProduitRepository repository;

    public ProduitService(CategorieRepository categorieRepositor,ProduitRepository repository) {
        this.categorieRepository = categorieRepositor;
        this.repository = repository;
    }

    public ProduitDTO creeProduit (CreationProduitDTO nouveauProduit){
        Categorie categorie = categorieRepository.findById(nouveauProduit.idCategorie()).orElseThrow(
                () -> new IllegalArgumentException("Catégorie introuvable.")
        );

        Produit produit = new Produit();
        produit.setNom(nouveauProduit.nom());
        produit.setPrix(nouveauProduit.prix());
        produit.setCategorie(categorie);

        Produit save = repository.save(produit);

        return new ProduitDTO(
                save.getId(),
                save.getNom(),
                save.getPrix(),
                save.getCategorie().getNom()
        );
    }

    public List<ProduitDTO> listeProduit (){
        List<Produit> produit = repository.findAll();
        return getDto(produit);
    }

    public List<ProduitDTO> ProduitParNom (String nom){
        List<Produit> produit = repository.findByNom(nom);
        return getDto(produit);
    }

    private List<ProduitDTO> getDto (List<Produit> produits){
        List<ProduitDTO> liste = new ArrayList<>();
        for(Produit prod : produits){
            ProduitDTO produitDTO = new ProduitDTO(
                    prod.getId(),
                    prod.getNom(),
                    prod.getPrix(),
                    prod.getCategorie().getNom()
            );
            liste.add(produitDTO);
        }
        return liste;
    }

    public String supprimerProduit (Long id){
        Produit produit = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Produit introuvable.")
        );
        repository.delete(produit);
        return "Produit supprimer avec succes.";
    }


}
