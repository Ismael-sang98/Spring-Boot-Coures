package com.ismaeldev.todolist;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TacheController {
    private  final TacheRepository repository;

    public TacheController(TacheRepository repository){
        this.repository = repository;
    }

    @PostMapping("/taches")
    public String ajouterTache(@RequestBody Tache tache){
        repository.save(tache);
        return "La Tache "+tache.getTitre()+" a été ajouter avec succes";
    }

    @GetMapping("/taches")
    public List<Tache> taches (){
        return repository.findAll();
    }

    @GetMapping("/terminer-tache")
    public String terminerTache(@RequestParam Long id){
        Tache tache = repository.findById(id).get();
        tache.setTerminee(true);
        repository.save(tache);
        return "La tache "+tache.getTitre()+" a été terminer.";
    }

}
