# Cours Spring Boot : La Validation Automatique (`@Valid`)

Dans la vie d'une application, 90% des erreurs proviennent des utilisateurs qui remplissent mal les formulaires (un âge négatif, un mot de passe trop court, un email sans `@`).

Historiquement, les développeurs écrivaient des dizaines de `if` dans leurs Services pour vérifier chaque champ. Aujourd'hui, on utilise la **Validation Déclarative**.

## 1. Le Concept

La règle est simple : **On sécurise toujours les portes de la ville (le DTO), pas l'intérieur du château (le Service).**

Le DTO est le premier objet qui reçoit les données d'internet. C'est sur lui que l'on va coller des "étiquettes de sécurité" (les annotations).

### Les étiquettes les plus utiles pour un développeur mobile :

*   **Pour les Textes (String) :**
    *   `@NotBlank` : Interdit les textes vides, nuls, ou qui ne contiennent que des espaces. (Idéal pour un pseudo, un titre).
    *   `@Email` : Vérifie que le texte ressemble bien à une adresse email.
    *   `@Size(min = 8, max = 20)` : Force une longueur (idéal pour un mot de passe).
*   **Pour les Nombres (int, double) :**
    *   `@Positive` : Le nombre doit être supérieur à 0 (ex: un prix, une quantité).
    *   `@Min(18)` : Le nombre doit être au minimum 18 (ex: un âge).
*   **Pour les Objets (ex: id de catégorie) :**
    *   `@NotNull` : Interdit que la valeur soit absente.

## 2. Comment ça s'écrit ?

Dans votre `record` (DTO), vous importez ces étiquettes depuis le paquet `jakarta.validation.constraints` :

```java
public record UtilisateurInscriptionDTO(
    @NotBlank(message = "Le pseudo est obligatoire")
    String pseudo,
    
    @Email(message = "L'email n'est pas valide")
    String email,
    
    @Size(min = 8, message = "Le mot de passe doit faire 8 caractères minimum")
    String motDePasse
) {}
```

## 3. Déclencher le piège (`@Valid`)

Mettre des étiquettes sur le DTO ne suffit pas. Il faut dire au garde-frontière (Le Contrôleur) de lire ces étiquettes. On fait cela en ajoutant **`@Valid`** juste avant le `@RequestBody`.

```java
@PostMapping("/inscription")
public ResponseEntity<?> inscrire(@Valid @RequestBody UtilisateurInscriptionDTO dto) {
    // Si on est ici, l'email est valide et le mot de passe est sécurisé !
    return ResponseEntity.ok("Succès !");
}
```

## 4. Que se passe-t-il quand l'utilisateur se trompe ?

Si l'application Flutter envoie un mauvais email, Spring va lever une exception très spécifique appelée : `MethodArgumentNotValidException`.

La beauté de la chose, c'est que vous pouvez utiliser votre super-pouvoir appris juste avant (Le `@ControllerAdvice`) pour attraper cette erreur !

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    // On attrape l'erreur de validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> gererErreurDeFormulaire(MethodArgumentNotValidException e) {
        
        // On extrait le message exact que vous aviez écrit dans l'annotation (ex: "Le pseudo est obligatoire")
        String messageDerreur = e.getBindingResult().getFieldError().getDefaultMessage();
        
        return ResponseEntity.badRequest().body("Erreur formulaire : " + messageDerreur);
    }
}
```

**Résultat :** Vos services sont vides de `if`, vos contrôleurs sont ultra-courts, et Flutter reçoit des messages d'erreur parfaits et ciblés !
