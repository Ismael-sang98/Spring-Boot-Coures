package com.ismaeldev.ecommerce.produits.controller;

import com.ismaeldev.ecommerce.produits.dto.CreationProduitDTO;
import com.ismaeldev.ecommerce.produits.dto.ProduitDTO;
import com.ismaeldev.ecommerce.produits.service.ProduitService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProduitController {
    private final ProduitService service;

    public ProduitController(ProduitService service) {
        this.service = service;
    }

    @PostMapping("/produit")
    public ResponseEntity<?> postProduit(@Valid @RequestBody CreationProduitDTO produit){
            return ResponseEntity.ok(service.creeProduit(produit));
    }

    @GetMapping("/produits")
    public ResponseEntity<List<ProduitDTO>> getProduit(){
        return ResponseEntity.ok(service.listeProduit());
    }
    @GetMapping("/produits/recherche")
    public ResponseEntity<List<ProduitDTO>> getByNameProduit (@RequestParam String nom){
        return ResponseEntity.ok(service.ProduitParNom(nom));
    }

    @DeleteMapping("/produit/{id}")
    private ResponseEntity<?> deleteProduit (@PathVariable Long id){
            return ResponseEntity.ok(service.supprimerProduit(id));
    }
}
