# Cours Spring Boot : Maîtriser ResponseEntity et les Codes HTTP

Dans le développement web professionnel, renvoyer la bonne donnée ne suffit pas. Il faut aussi renvoyer le bon "Code de Statut HTTP" pour que l'ordinateur qui reçoit la réponse comprenne si tout s'est bien passé ou s'il y a eu une erreur. C'est le rôle de `ResponseEntity`.

---

## 1. Qu'est-ce que `ResponseEntity` ?

Imaginez qu'une réponse HTTP est une **lettre envoyée par la poste**.
*   Jusqu'à présent, vos méthodes renvoyaient un `String` ou un `Livre`. Vous ne fournissiez que la **lettre** (le contenu). Spring Boot se chargeait d'acheter une enveloppe standard (Code 200 OK) et de l'envoyer.
*   **`ResponseEntity`**, c'est l'enveloppe complète. Elle vous permet de choisir le contenu (le Body), mais aussi le tampon officiel de la poste (le Code HTTP) et les étiquettes de suivi (les Headers).

### Les codes HTTP les plus courants :
*   🟢 **200 OK** : Tout s'est bien passé.
*   🟢 **201 Created** : La création en base de données a réussi.
*   🟡 **400 Bad Request** : Le client a envoyé de mauvaises données (ex: numéro de siège invalide).
*   🟡 **404 Not Found** : L'ID cherché n'existe pas en base de données.
*   🔴 **500 Internal Server Error** : Le code Java a planté (bug du serveur).

---

## 2. Le Mystère des Chevrons : `<T>` et `<?>`

`ResponseEntity` est ce qu'on appelle en Java une classe **Générique** (comme `List<T>`). Elle doit savoir quel type d'objet elle transporte dans son enveloppe.

### Le cas classique : `ResponseEntity<String>`
Si vous êtes sûr à 100% que votre méthode renverra toujours du texte, vous écrivez :
```java
public ResponseEntity<String> reserver() {
    return ResponseEntity.ok("Succès !");
}
```

### Le cas classique : `ResponseEntity<Billet>`
Si vous renvoyez l'objet fraîchement créé en JSON :
```java
public ResponseEntity<Billet> reserver() {
    Billet b = new Billet("Titanic", 12, 10.5);
    return ResponseEntity.ok(b); // Renverra du JSON
}
```

### Pourquoi utilise-t-on `ResponseEntity<?>` ?

Le symbole `?` en Java s'appelle le **Wildcard** (le Joker). Il signifie : *"Je ne sais pas encore exactement quel type d'objet je vais renvoyer, accepte n'importe quoi"*.

**Pourquoi est-ce utile ?**
Parce que dans une vraie API, si tout va bien, vous voulez renvoyer un Objet Java (qui deviendra du JSON). Mais s'il y a une erreur, vous voulez souvent renvoyer un simple message d'erreur en texte (`String`).
Vous avez donc une méthode qui renvoie **soit un String, soit un Billet**.

Si vous écrivez `ResponseEntity<Billet>`, Java va refuser de compiler si vous essayez de faire `return ResponseEntity.badRequest().body("Erreur texte");`.

**La solution :** On met le joker `<?>`.

```java
@PostMapping("/reserver")
public ResponseEntity<?> reserver(@RequestBody Billet billet) {
    if (billet.getNumeroSiege() > 50) {
        // Ici on renvoie un STRING (Erreur 400)
        return ResponseEntity.badRequest().body("Erreur de siège"); 
    } else {
        // Ici on renvoie un OBJET BILLET (Succès 200)
        return ResponseEntity.ok(billet); 
    }
}
```
Grâce au `?`, Java accepte que la méthode puisse renvoyer des types différents selon les circonstances !

---

## 3. Comment construire une `ResponseEntity` ?

Spring fournit des méthodes raccourcies très pratiques pour créer ces enveloppes :

*   **Pour un succès :**
    *   `ResponseEntity.ok(objet)` ➡️ Code 200
*   **Pour une erreur de l'utilisateur :**
    *   `ResponseEntity.badRequest().body("Message")` ➡️ Code 400
    *   `ResponseEntity.notFound().build()` ➡️ Code 404 (L'enveloppe est vide, pas de body)
*   **Pour les codes spécifiques :**
    *   `ResponseEntity.status(HttpStatus.CREATED).body(objet)` ➡️ Code 201
    *   `ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé")` ➡️ Code 403
