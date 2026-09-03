package com.ismaeldev.ecommerce.category.controller;

import com.ismaeldev.ecommerce.category.dto.CategorieDTO;
import com.ismaeldev.ecommerce.category.service.CategorieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategorieController {
    private final CategorieService service;

    public CategorieController(CategorieService service) {
        this.service = service;
    }

    @PostMapping("/categorie")
    public ResponseEntity<CategorieDTO> postCat (@RequestBody CategorieDTO categorieDTO){
        return ResponseEntity.ok(service.creeCat(categorieDTO));
    }

    @GetMapping("/categorie")
    public ResponseEntity<List<CategorieDTO>> getCat(){
        return ResponseEntity.ok(service.listeCat());
    }

    @PutMapping("/categorie/put/{id}")
    public ResponseEntity<?> putCat(@PathVariable Long id, @RequestBody CategorieDTO categorieDTO){
            return ResponseEntity.ok(service.modifierCat(id,categorieDTO));
    }
}

