# Cours Spring Boot : Les Bases de Données avec Spring Data JPA

Ce document explique comment lier vos classes Java (POO) à une base de données relationnelle (SQL) sans avoir à écrire de requêtes SQL manuellement. C'est ce qu'on appelle l'**ORM (Object-Relational Mapping)**.

---

## 1. `@Entity` : Transformer une Classe en Table SQL

En Java classique, un objet ne vit que dans la mémoire vive (RAM) de l'ordinateur. Dès que l'application s'arrête, il disparaît.
Pour le persister, nous utilisons l'annotation `@Entity` au-dessus de la classe.

### Fonctionnement interne
Au démarrage, l'outil **Hibernate** (le moteur sous le capot de Spring Data JPA) scanne votre code. Quand il trouve `@Entity`, il va se connecter à la base de données et créer automatiquement une table portant le nom de votre classe, avec des colonnes correspondant à vos attributs.

### Les règles obligatoires d'une Entité
Pour qu'une classe soit une `@Entity` valide, elle doit respecter **trois règles d'or** :
1. Avoir l'annotation `@Entity`.
2. Avoir un constructeur vide (sans paramètres) pour que Hibernate puisse reconstruire l'objet depuis la base.
3. Avoir une **Clé Primaire** (`@Id`).

### Exemple
```java
import jakarta.persistence.*;

@Entity
public class Produit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // La Clé Primaire (ex: 1, 2, 3...)
    
    private String nom;
    private double prix;

    public Produit() {} // Règle 2 : Constructeur vide
    
    // Getters et Setters...
}
```

> [!NOTE]
> **Que fait `@GeneratedValue` ?**
> `GenerationType.IDENTITY` dit à la base de données (et non à Java) de s'occuper de générer un ID unique automatiquement (Auto-Incrémentation) à chaque nouvelle ligne insérée.

---

## 2. `JpaRepository` : Le pont vers la Base de Données

Une fois la table créée, comment la lire ou la modifier ?
Dans d'autres langages, il faut écrire une classe complexe qui gère la connexion, exécute `SELECT * FROM produit`, et lit les résultats ligne par ligne.

Avec Spring, vous créez simplement une **Interface** (un contrat) qui hérite de `JpaRepository`.

### La magie de Spring
Vous n'écrivez **jamais le code** de cette interface. Au démarrage, Spring génère le code à votre place (ce qu'on appelle un *Proxy*).

```java
import org.springframework.data.jpa.repository.JpaRepository;

// Le <Produit, Long> signifie : "Gère la table Produit, dont l'ID est de type Long"
public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
```

### Les méthodes gratuites
Rien qu'en écrivant ces deux lignes d'interface, vous débloquez immédiatement des dizaines de méthodes puissantes :
* `repository.save(objet)` : Insère ou met à jour une ligne (équivalent de `INSERT` ou `UPDATE`).
* `repository.findAll()` : Récupère toute la table dans une `List<Produit>`.
* `repository.findById(1L)` : Cherche un produit précis (retourne un `Optional` car l'objet peut ne pas exister).
* `repository.deleteById(1L)` : Supprime une ligne de la base.

### Les "Derived Query Methods" (Bonus)
Vous pouvez même inventer des méthodes juste en écrivant leur nom en anglais ! Spring comprendra la requête SQL à générer.
```java
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    // Spring lira la méthode et génèrera "SELECT * FROM produit WHERE nom = ?"
    List<Produit> findByNom(String nom); 
}
```

---

## 3. L'Injection de Dépendance dans le Contrôleur

Pour utiliser votre Repository, il faut l'appeler depuis votre `@RestController`. 
Spring utilise **l'Injection de Dépendances**.

```java
@RestController
public class MagasinController {

    // 1. Déclaration de l'outil
    private final ProduitRepository repository;

    // 2. Le constructeur (Spring l'appelle automatiquement et lui "injecte" l'outil)
    public MagasinController(ProduitRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/produits")
    public Produit ajouter(@RequestBody Produit p) {
        // 3. Utilisation !
        return repository.save(p);
    }
}
```

> [!TIP]
> Pourquoi le constructeur ? Cela garantit que votre Contrôleur ne peut pas exister (ni démarrer) si le lien avec la base de données n'est pas prêt. C'est une sécurité de la POO.
