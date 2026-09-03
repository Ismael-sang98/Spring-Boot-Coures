package com.ismaeldev.billet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BilletController {

    private  final  BilletRepository repository;

    public BilletController(BilletRepository repository){
        this.repository = repository;
    }

    @GetMapping("/billets")
    public List<Billet> billets (){
        return repository.findAll();
    }

    @PostMapping("/reserver")
    public ResponseEntity<?> reservation (@RequestBody Billet billet){
        if(billet.getNumeroSiege() < 0 || billet.getNumeroSiege() > 50){
            return ResponseEntity.badRequest().body("Erreur : La salle ne possède que 50 sièges !");
        }else{
            repository.save(billet);
            return ResponseEntity.ok("Succès ! Billet réservé pour le siège " + billet.getNumeroSiege());
        }
    }

}
