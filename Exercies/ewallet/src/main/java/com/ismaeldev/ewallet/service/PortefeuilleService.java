package com.ismaeldev.ewallet.service;

import com.ismaeldev.ewallet.dto.PortefeuilleCreationDTO;
import com.ismaeldev.ewallet.dto.PortefeuilleReponseDTO;
import com.ismaeldev.ewallet.dto.TransactionDTO;
import com.ismaeldev.ewallet.entity.Portefeuille;
import com.ismaeldev.ewallet.repo.PortefeuilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Port;
import java.util.List;
import java.util.Optional;

@Service
public class PortefeuilleService {

    final PortefeuilleRepository repository;

    public PortefeuilleService(PortefeuilleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PortefeuilleReponseDTO creerPortefeuille(PortefeuilleCreationDTO creation){
        Portefeuille portefeuille = new Portefeuille();
        portefeuille.setProprietaire(creation.proprietaire());
        portefeuille.setSolde(0.0);

        Portefeuille sauvegarde =  repository.save(portefeuille);

        PortefeuilleReponseDTO retour = new PortefeuilleReponseDTO(
                sauvegarde.getId(),
                sauvegarde.getProprietaire(),
                sauvegarde.getSolde()
        );
        return retour;
    }


    @Transactional
    public PortefeuilleReponseDTO compte(Long id) throws IllegalArgumentException {
        Portefeuille portefeuille = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Portefeuille introuvable"));
        PortefeuilleReponseDTO compte = new PortefeuilleReponseDTO(
                portefeuille.getId(),
                portefeuille.getProprietaire(),
                portefeuille.getSolde()
        );
        return compte;
    }


    @Transactional
    public TransactionDTO faireDepot(Long id, double montant) throws IllegalArgumentException{
        Portefeuille portefeuille = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Portefeuille introuvable"));
        portefeuille.setSolde( portefeuille.getSolde() + montant );
        Portefeuille sauvegarde = repository.save(portefeuille);
        return new TransactionDTO(sauvegarde.getSolde());
    }

    @Transactional
    public TransactionDTO faireRetrait(Long id, double montant) throws IllegalArgumentException{
        Portefeuille portefeuille = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Portefeuille introuvable"));
        if (montant > portefeuille.getSolde()){
            throw new IllegalArgumentException("Votre solde est insuffisant.");
        }
        //double nouveauSolde = portefeuille.get().getSolde() - montant;
        portefeuille.setSolde( portefeuille.getSolde() - montant );
        Portefeuille sauvegarde = repository.save(portefeuille);
        return new TransactionDTO(sauvegarde.getSolde());
    }
}
