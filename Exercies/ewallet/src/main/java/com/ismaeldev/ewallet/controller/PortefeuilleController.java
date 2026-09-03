package com.ismaeldev.ewallet.controller;


import com.ismaeldev.ewallet.dto.MessageTransactionDTO;
import com.ismaeldev.ewallet.dto.PortefeuilleCreationDTO;
import com.ismaeldev.ewallet.dto.PortefeuilleReponseDTO;
import com.ismaeldev.ewallet.dto.TransactionDTO;
import com.ismaeldev.ewallet.service.PortefeuilleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PortefeuilleController {
    final private PortefeuilleService service;

    public PortefeuilleController(PortefeuilleService service) {
        this.service = service;
    }

    @PostMapping("/portefeuilles")
    public ResponseEntity<PortefeuilleReponseDTO> creation (@RequestBody PortefeuilleCreationDTO creationDTO){
        return ResponseEntity.ok(service.creerPortefeuille(creationDTO));
    }

    @PostMapping("/portefeuilles/{id}/depot")
    public ResponseEntity<?> depot ( @PathVariable Long id, @RequestBody TransactionDTO transactionDTO){
        try{
            TransactionDTO transaction = service.faireDepot(id,transactionDTO.solde());
            return ResponseEntity.ok().body(
                    new MessageTransactionDTO("Dépôt réussi",transaction.solde())
            );
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }



    }

    @PostMapping("/portefeuilles/{id}/retrait")
    public ResponseEntity<?> retrait (@PathVariable Long id, @RequestBody TransactionDTO transactionDTO){
        try{
            TransactionDTO transaction = service.faireRetrait(id,transactionDTO.solde());
            return ResponseEntity.ok().body(new MessageTransactionDTO("Retrait réussi",transaction.solde()));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping ("/portefeuilles/{id}")
    public ResponseEntity<?> compte(@PathVariable Long id){
        PortefeuilleReponseDTO compte = service.compte(id);
        try {
            return ResponseEntity.ok(compte);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
