package com.ismaeldev.contacts;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.util.List;

@RestController
public class ContactController {
    private final ContactRepository repository;
    private final ResourceUrlProvider resourceUrlProvider;

    public ContactController (ContactRepository repository, ResourceUrlProvider resourceUrlProvider){
        this.repository = repository;
        this.resourceUrlProvider = resourceUrlProvider;
    }

    @PostMapping("/contacts")
    public String ajout(@RequestBody Contact contact){
        repository.save(contact);

        return contact.getNom()+" a été ajouter avec succes.";
    }

    @GetMapping("/contacts")
    public List<Contact> contacts(){
        return repository.findAll();
    }

    @GetMapping("/contacts/recherche")
    public List<Contact> contacts(@RequestParam String nom){
        return repository.findByNom(nom);
    }
}
