package com.ismaeldev.app_mysql.controller;

import com.ismaeldev.app_mysql.dto.CompteUtilisateurDTO;
import com.ismaeldev.app_mysql.dto.CreationUtilisateurDTO;
import com.ismaeldev.app_mysql.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UtilisateurController {

    private final UtilisateurService service;

    public UtilisateurController(UtilisateurService service) {
        this.service = service;
    }

    @PostMapping("/user")
    public ResponseEntity<CompteUtilisateurDTO> create(@RequestBody CreationUtilisateurDTO newUser){
        return ResponseEntity.ok(service.creation(newUser));
    }

    @GetMapping("/users")
    public ResponseEntity<List<CompteUtilisateurDTO>> users (){
        return ResponseEntity.ok(service.allProfil());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> filterById (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.profilById(id));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> supprimer (@PathVariable Long id){
        try{
            return ResponseEntity.ok(service.supprimer(id));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> modifier (@PathVariable Long id, @RequestBody CreationUtilisateurDTO mod){
        try {
           return ResponseEntity.ok(service.modifier(id, mod));
        }catch (IllegalArgumentException e){
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
