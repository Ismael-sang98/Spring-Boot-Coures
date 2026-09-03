package com.ismaeldev.inscription;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompteService {
    final CompteRepository repository;

    public CompteService(CompteRepository repository){
        this.repository = repository;
    }

    public CompteReponseDTO inscrireCompte(Compte compteEntrant){
       Compte nouveau =  repository.save(compteEntrant);
        return new CompteReponseDTO(nouveau.getPseudo(), nouveau.getEmail());
    }

    public List<CompteReponseDTO> comptes() {
        List<Compte> listeBrut = repository.findAll();
        List<CompteReponseDTO> listeRafinee = new ArrayList<>();

        for(Compte list : listeBrut){
            CompteReponseDTO dt = new CompteReponseDTO(list.getPseudo(), list.getEmail());
            listeRafinee.add(dt);
        }
        return listeRafinee;
    }


}
