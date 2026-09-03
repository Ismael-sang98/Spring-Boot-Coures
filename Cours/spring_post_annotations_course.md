# Cours Spring Boot : Recevoir des données complexes (POST & JSON)

Ce document fait suite au cours sur les requêtes `GET`. Il explique comment votre serveur peut recevoir de gros volumes de données sécurisées (comme un formulaire d'inscription complet) en utilisant la puissance de la POO et de la sérialisation JSON.

---

## Les limites de l'URL et du `GET`

Avec `@GetMapping` et `@RequestParam`, les données sont visibles dans l'URL (`?nom=Marc&age=25`).
Cela pose trois problèmes majeurs :
1. **Sécurité :** Vous ne pouvez pas faire transiter un mot de passe ou un numéro de carte bancaire de manière visible dans la barre d'adresse.
2. **Volume :** Une URL a une taille maximale (souvent autour de 2000 caractères). Impossible d'envoyer le texte d'un long article de blog par l'URL.
3. **Structure :** C'est fastidieux d'envoyer des objets complexes (ex: un utilisateur qui possède une liste d'adresses).

La solution ? Utiliser la méthode HTTP **POST** et cacher les données dans le "corps" (Body) de la requête.

---

## 1. `@PostMapping` : Le guichet de dépôt

Tout comme `@GetMapping` écoute les demandes de lecture, `@PostMapping` se place au-dessus d'une méthode pour écouter les demandes d'écriture/création.

### Fonctionnement interne
Lorsque le client (navigateur, application mobile, ou Postman) envoie une requête HTTP `POST` vers l'URL spécifiée, Spring intercepte la requête et la dirige vers cette méthode. 

> [!TIP]
> **Règle métier REST :** Par convention, on utilise `POST` quand on veut **créer** une nouvelle ressource dans la base de données. Pour **modifier** une ressource existante, on utilise plutôt `@PutMapping` ou `@PatchMapping`.

### Exemple
```java
@PostMapping("/utilisateurs")
public String creerUtilisateur(...) {
    // Logique pour sauvegarder l'utilisateur
}
```

---

## 2. `@RequestBody` : Le traducteur Magique (JSON ➡️ Java)

Cette annotation se place **dans les parenthèses de la méthode**. Elle est vitale : sans elle, Spring ignorera totalement les données envoyées par le client.

### Fonctionnement interne (La Désérialisation)

1. **L'arrivée des données :** Le client envoie un texte au format JSON dans le corps de sa requête.
2. **L'interception :** Spring voit l'annotation `@RequestBody`.
3. **Le travail de Jackson :** Spring délègue le travail à une librairie interne appelée *Jackson*.
4. **La création de l'objet (Très important pour la POO) :**
   * Jackson fait un `new VotreObjet()` **en utilisant le constructeur vide** (sans paramètres).
   * Jackson lit chaque clé du JSON (ex: `"prenom"`).
   * Il cherche dans votre classe Java l'attribut correspondant (`private String prenom;`).
   * Il injecte la valeur via la *Reflection* ou via les *Setters* si vous en avez défini.
5. **Le résultat :** Votre méthode Java reçoit un objet "prêt à l'emploi", parfaitement hydraté avec les données du client.

> [!WARNING]
> **La règle d'or du constructeur vide :** 
> Pour que cette magie fonctionne, votre classe Java (votre Modèle) **doit obligatoirement posséder un constructeur vide**. Si vous avez écrit un constructeur avec paramètres, Java supprime le constructeur vide par défaut. Vous devez alors le réécrire explicitement (`public MonObjet() {}`).

### Exemple Complet

**Le Modèle Java (POO) :**

```java
public class Livre {
    private String titre;
    private int pages;

    // 1. Constructeur vide obligatoire pour @RequestBody (Jackson)
    public Livre() {} 

    // 2. Getters (utiles pour renvoyer l'objet plus tard)
    public String getTitre() { return titre; }
    public int getPages() { return pages; }
}
```

**Le Contrôleur :**

```java
@PostMapping("/ajouter-livre")
public String sauvegarderLivre(@RequestBody Livre nouveauLivre) {
    // À ce stade, 'nouveauLivre' est un vrai objet Java instancié et rempli !
    System.out.println("Titre reçu : " + nouveauLivre.getTitre());
    
    return "Le livre a été ajouté avec succès.";
}
```

---

## Synthèse du cycle de vie

Voici ce qui se passe chronologiquement lors de votre test sur Postman :

1. **Postman (Client) :** Envoie une requête `POST` à `/creer-profil` avec le JSON `{"prenom":"Marc", "age":25}` dans le corps.
2. **Tomcat (Serveur) :** Reçoit la requête HTTP.
3. **Spring (Routeur) :** Voit l'URL `/creer-profil` et le type `POST`, il l'associe à la méthode annotée avec `@PostMapping("/creer-profil")`.
4. **Spring (Désérialisation) :** Voit `@RequestBody Utilisateur`. Il crée un objet `Utilisateur` vide, le remplit avec "Marc" et "25".
5. **Votre Code Java :** S'exécute avec cet objet instancié. Vous en faites ce que vous voulez (le sauvegarder en base de données, etc.).
6. **Spring (Réponse) :** Récupère la valeur retournée par votre méthode et la renvoie à Postman.
