package com.ismaeldev.newsapp;

import com.ismaeldev.newsapp.dto.ArticleCreationDTO;
import com.ismaeldev.newsapp.dto.ArticleReponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.WebSocket;
import java.util.List;

@RestController
public class ArticleController {

   private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @PostMapping("/articles")
    public ResponseEntity<?> creerArticle(@RequestBody ArticleCreationDTO article){
        if(article.titre() == null || article.titre().isEmpty()){
           return ResponseEntity.badRequest().body("Erreur : Le titre ne peut pas être vide !");
        }
        ArticleReponseDTO sauvegarde = service.creerArticle(article);

        return ResponseEntity.ok(sauvegarde);
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleReponseDTO>> afficherTouteLaListe (){
        List<ArticleReponseDTO> liste = service.ToutAffchierList();
        return ResponseEntity.ok(liste);
    }

    @GetMapping("/articles/recherche")
    public ResponseEntity<List<ArticleReponseDTO>> afficherParAuteur (@RequestParam(value = "auteur") String auteur){
        List<ArticleReponseDTO> liste = service.rechercheParAuteur(auteur);
        return ResponseEntity.ok(liste);
    }

}
