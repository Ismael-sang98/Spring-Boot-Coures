package com.ismaeldev.newsapp;

import com.ismaeldev.newsapp.dto.ArticleCreationDTO;
import com.ismaeldev.newsapp.dto.ArticleReponseDTO;
import jakarta.persistence.Entity;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository repository;
    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public ArticleReponseDTO creerArticle (ArticleCreationDTO dtoEntrant){
        Article nouvelArticle = new Article();
        nouvelArticle.setTitre(dtoEntrant.titre());
        nouvelArticle.setContenu(dtoEntrant.contenu());
        nouvelArticle.setAuteur(dtoEntrant.auteur());
        nouvelArticle.setNoteSecretes(dtoEntrant.noteSecretes());

        Article articleSauvegarder = repository.save(nouvelArticle);

        ArticleReponseDTO reponse = new ArticleReponseDTO(
                articleSauvegarder.getId(),
                articleSauvegarder.getTitre(),
                articleSauvegarder.getContenu(),
                articleSauvegarder.getAuteur()
        );
        return reponse;
    }

    public List<ArticleReponseDTO> ToutAffchierList(){
        List<Article> listeBrute = repository.findAll();

        return getArticleReponseDTOS(listeBrute);
    }

    public List<ArticleReponseDTO> rechercheParAuteur(String auteur){
        List<Article> listeBrute = repository.findByAuteur(auteur);

        return getArticleReponseDTOS(listeBrute);
    }

    @NonNull
    private List<ArticleReponseDTO> getArticleReponseDTOS(List<Article> listeBrute) {
        List<ArticleReponseDTO> listeFinale = new ArrayList<>();

        for(Article liste : listeBrute){
            ArticleReponseDTO dto = new ArticleReponseDTO(
                    liste.getId(),
                    liste.getTitre(),
                    liste.getContenu(),
                    liste.getAuteur()
            );
            listeFinale.add(dto);
        }

        return listeFinale;
    }
}
