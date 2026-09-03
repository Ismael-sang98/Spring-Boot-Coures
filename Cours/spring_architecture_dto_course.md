# Cours Spring Boot : Architecture en 3 Couches et DTOs

Quand vous créez un backend pour une application Flutter, votre code va très vite grossir. Si vous mettez toute la logique (les `if/else`, les calculs, la base de données) directement dans le `@RestController`, votre fichier va devenir un monstre de 1000 lignes impossible à maintenir.

L'industrie du logiciel utilise donc une norme stricte : **L'Architecture en 3 couches (3-Tier Architecture)**.

---

## 1. L'Architecture en 3 Couches

L'idée est de séparer les responsabilités. Chaque classe ne doit faire qu'une seule chose.

### Couche 1 : La Présentation (`@RestController`)
*   **Son rôle :** C'est le guichet d'accueil. Il écoute internet, reçoit le JSON de Flutter, et renvoie le JSON à Flutter.
*   **Interdit :** Il ne doit **jamais** faire de calculs compliqués ni parler directement à la base de données. Il délègue le travail au Service.

### Couche 2 : La Logique Métier (`@Service`)
*   **Son rôle :** C'est le cerveau de l'application. C'est ici que l'on vérifie si un utilisateur a le droit de réserver, qu'on calcule des prix, ou qu'on crypte un mot de passe.
*   **L'annotation :** On place `@Service` au-dessus de la classe. (Spring va la garder en mémoire comme il le fait avec `@RestController`).

### Couche 3 : Les Données (`@Repository`)
*   **Son rôle :** C'est le gardien de la base de données (ce que vous savez déjà faire avec `JpaRepository`).

**Le flux d'une requête est toujours :**
`Flutter` ➡️ `Controller` ➡️ `Service` ➡️ `Repository` ➡️ `Base de données`

---

## 2. Le Concept de DTO (Data Transfer Object)

C'est la règle d'or pour la sécurité et l'optimisation mobile.

### Le Problème
Imaginez une table `@Entity Utilisateur` avec les champs : `id`, `nom`, `email`, `motDePasse`, `dateInscription`.
Si depuis votre contrôleur, vous faites `return repository.findAll();`, Spring va convertir toute l'Entité en JSON. 
**Catastrophe :** Votre application Flutter (et n'importe quel hacker qui écoute) va recevoir le `motDePasse` en clair !

### La Solution : Le DTO
Un DTO est une simple classe Java "jetable" qui ne sert qu'à transporter les données entre Spring et Flutter. Elle n'est PAS connectée à la base de données (pas de `@Entity`).

1. La base de données renvoie l'entité `Utilisateur` (avec le mot de passe).
2. Le `@Service` prend cet `Utilisateur` et copie seulement son `id` et son `nom` dans un nouvel objet appelé `UtilisateurDTO`.
3. Le `@RestController` renvoie l' `UtilisateurDTO` à Flutter.

Flutter reçoit un JSON propre et sécurisé, sans le mot de passe !

---

## 3. Exemple Concret de Code

### A. L'Entité (La Base de données)
```java
@Entity
public class Utilisateur {
    @Id @GeneratedValue
    private Long id;
    private String nom;
    private String motDePasse; // NE DOIT JAMAIS FUITER !
    
    // Constructeurs, Getters, Setters...
}
```

### B. Le DTO (Ce que Flutter reçoit)
```java
public class UtilisateurDTO {
    private String nom;
    
    public UtilisateurDTO(String nom) {
        this.nom = nom;
    }
    
    public String getNom() { return nom; }
}
```

### C. Le Service (Le Cerveau)
```java
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {
    private final UtilisateurRepository repository;

    public UtilisateurService(UtilisateurRepository repository) {
        this.repository = repository;
    }

    public UtilisateurDTO creerUtilisateur(Utilisateur user) {
        // 1. Sauvegarde dans la vraie base de données (avec le mot de passe)
        Utilisateur savedUser = repository.save(user);
        
        // 2. Conversion en DTO pour protéger les données
        return new UtilisateurDTO(savedUser.getNom());
    }
}
```

### D. Le Contrôleur (Le Guichet)
```java
@RestController
public class UtilisateurController {
    
    // Le Contrôleur ne connaît plus le Repository ! Il ne parle qu'au Service.
    private final UtilisateurService service;

    public UtilisateurController(UtilisateurService service) {
        this.service = service;
    }

    @PostMapping("/utilisateurs")
    public ResponseEntity<UtilisateurDTO> inscription(@RequestBody Utilisateur user) {
        // Le contrôleur ne fait que passer la balle au service
        UtilisateurDTO reponsePourFlutter = service.creerUtilisateur(user);
        return ResponseEntity.ok(reponsePourFlutter);
    }
}
```
