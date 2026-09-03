package com.ismaeldev.inscription;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompteController {
    private final CompteService service;

    public CompteController(CompteService service){
        this.service = service;
    }

    @PostMapping("/inscription")
    public ResponseEntity<CompteReponseDTO> inscription(@RequestBody Compte compte){

        CompteReponseDTO reponse = service.inscrireCompte(compte);
        return ResponseEntity.ok(reponse);
    }

    @GetMapping("/compte")
    public ResponseEntity<List<CompteReponseDTO>> get(){
        List<CompteReponseDTO> list = service.comptes();
        return ResponseEntity.ok(list);
    }
}
