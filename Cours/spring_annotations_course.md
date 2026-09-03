# Cours Spring Boot : Les fondamentaux des API REST

Ce document explique les trois annotations fondamentales pour créer une API Web avec Spring Boot. Il est conçu pour faire le pont entre vos connaissances en Java Orienté Objet (POO) et la "magie" du framework Spring.

> [!NOTE]
> **Qu'est-ce qu'une annotation (`@`) ?**
> En Java, une annotation n'est qu'une méta-donnée (un "post-it"). Elle ne contient pas de logique d'exécution. Au démarrage de l'application, Spring utilise un mécanisme appelé **Reflection** pour lire ces post-its et exécuter le code lourd (gestion réseau, instanciation) à votre place.

---

## 1. `@RestController` : Le Chef d'Orchestre

Cette annotation se place **au-dessus de la définition de la classe**. Elle transforme une classe Java standard en un composant géré par Spring, capable de répondre à des requêtes web.

### Fonctionnement interne

L'annotation `@RestController` est en réalité la fusion de deux comportements :

1. **La gestion de l'objet (IoC - Inversion of Control) :**
   Spring détecte la classe au démarrage, fait un `new VotreClasse()` et stocke cet objet en mémoire. Vous n'avez plus besoin d'instancier la classe manuellement.
2. **Le formatage de la réponse (`@ResponseBody`) :**
   Spring sait que cette classe est destinée à une API (REST). Par conséquent, ce que retournent vos méthodes (texte, objet, liste) ne sera pas interprété comme une page HTML à afficher, mais sera renvoyé comme de la **donnée brute** (souvent convertie automatiquement en JSON) directement au client (navigateur, application mobile, etc.).

### Exemple
```java
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonControleurWeb {
    // Les méthodes ici seront exposées sur le web
}
```

---

## 2. `@GetMapping` : Le Routeur (Le standardiste)

Cette annotation se place **au-dessus d'une méthode**. Elle relie une adresse URL (un "chemin") à une méthode Java spécifique, uniquement pour les requêtes de type `GET`.

### Fonctionnement interne

* **Le protocole HTTP :** Dans le web, une requête `GET` sert à "demander/lire" de l'information (ex: afficher une page, lister des produits).
* **Le Routage :** Lorsque le serveur (Tomcat) reçoit une requête, Spring regarde le chemin de l'URL (par exemple `/bonjour`). Il consulte ensuite ses "post-its" et, s'il trouve `@GetMapping("/bonjour")`, il exécute la méthode correspondante.

> [!TIP]
> Si vous souhaitez créer (sauvegarder) des données, vous utiliseriez `@PostMapping`. Pour modifier, `@PutMapping`. Pour supprimer, `@DeleteMapping`.

### Exemple
```java
@GetMapping("/bonjour")
public String direBonjour() {
    // Cette méthode est appelée automatiquement si on visite http://localhost:8080/bonjour
    return "Bonjour tout le monde !"; 
}
```

---

## 3. `@RequestParam` : L'Attrapeur de Données

Cette annotation se place **à l'intérieur des parenthèses d'une méthode, devant un paramètre**. Elle permet de récupérer des données envoyées par l'utilisateur directement dans l'URL.

### Fonctionnement interne

Dans une URL, tout ce qui se trouve après le point d'interrogation `?` s'appelle un *Query Parameter* (paramètre de requête). 
Exemple d'URL : `http://localhost:8080/recherche?motCle=java&page=2`

Spring intercepte la requête HTTP entrante, extrait la valeur associée à la clé demandée, convertit le texte (String) dans le type de votre variable Java (int, boolean, String), et l'injecte lors de l'appel de votre méthode.

### Les options importantes
* `value` (ou `name`) : Le nom exact de la clé dans l'URL.
* `required` : (Booléen, par défaut `true`). Si `true` et que le paramètre manque dans l'URL, Spring renvoie une erreur 400 (Bad Request).
* `defaultValue` : Une valeur par défaut de secours si l'utilisateur ne fournit pas le paramètre (ce qui évite les erreurs 400).

### Exemple
```java
@GetMapping("/profil")
// URL visée : http://localhost:8080/profil?pseudo=Alex&age=30
public String afficherProfil(
        @RequestParam(value = "pseudo") String pseudo,
        @RequestParam(value = "age", defaultValue = "18") int age
) {
    return "Profil de " + pseudo + " (Age: " + age + ")";
}
```

---

## Synthèse

Voici comment ces trois annotations collaborent dans une architecture MVC (Modèle-Vue-Contrôleur) orientée API :

1. L'application démarre ➡️ Spring voit **`@RestController`** ➡️ Il crée l'objet.
2. L'utilisateur visite `?nom=Marc` sur `/salut` ➡️ Spring voit **`@GetMapping("/salut")`** ➡️ Il prépare l'exécution de la méthode.
3. Spring voit **`@RequestParam("nom")`** ➡️ Il extrait "Marc" de l'URL et le passe à la méthode.
4. La méthode retourne `"Salut Marc"` ➡️ Grâce à **`@RestController`**, Spring renvoie ce texte brut à l'utilisateur.
