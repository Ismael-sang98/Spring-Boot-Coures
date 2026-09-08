# Comprendre la classe `JwtService`

Vous avez totalement raison de ne pas vouloir copier-coller aveuglément. 
La librairie `jjwt` que nous utilisons possède un vocabulaire un peu spécial. Voici l'explication détaillée de cette usine à Badges.

---

## 1. Le Vocabulaire de base
Dans le monde du JWT, on n'utilise pas le mot "Donnée" ou "Variable". On utilise le mot **"Claim"** (Revendication en français). 
*   Quand Flutter dit au serveur : *"Dans mon badge, il est écrit que mon email est ismael@gmail.com"*, c'est un **Claim**. 
*   Le serveur va vérifier si ce "Claim" est vrai.

---

## 2. La Clé Secrète (`CLE_SECRETE`)
```java
private static final String CLE_SECRETE = "404E635266556A58...";
```
Un JWT n'est pas "crypté" (n'importe qui peut lire ce qu'il y a dedans). Par contre, il est **Signé**.
La clé secrète, c'est l'encre indélébile du tampon officiel de votre serveur. Seul votre serveur Spring Boot possède cette clé. S'il signe un badge avec, et que quelqu'un modifie une seule lettre dans le badge, la signature explose et le serveur le saura.

---

## 3. L'impression du Badge (`generateToken`)
```java
public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
            .setSubject(userDetails.getUsername()) 
            .setIssuedAt(new Date(System.currentTimeMillis())) 
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) 
            .signWith(getSignInKey(), SignatureAlgorithm.HS256) 
            .compact();
}
```
Ici, on utilise le "Builder" (le constructeur) de la librairie JWT. On lui donne des instructions claires :
*   `setSubject` : Le "Sujet" du badge, c'est le propriétaire. On y met l'email.
*   `setIssuedAt` : La date de création (maintenant).
*   `setExpiration` : La date d'expiration (maintenant + 24 heures en millisecondes).
*   `signWith` : On signe le tout avec notre clé secrète en utilisant l'algorithme mathématique puissant `HS256`.
*   `compact()` : On écrase tout ça pour le transformer en une seule ligne de texte (`eyJhbG...`).

---

## 4. Lire le Badge (`extractAllClaims`)
```java
private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSignInKey()) // On fournit la clé pour vérifier la signature
            .build()
            .parseClaimsJws(token) // On lit le token
            .getBody(); // On récupère les données
}
```
Quand Flutter vous renverra le badge, il faudra le lire. 
Le `parserBuilder()` est le scanner du Videur. Il prend la clé secrète, vérifie que le sceau n'a pas été brisé, et si c'est bon, il ouvre le badge et nous donne accès au `Body` (qui contient tous nos **Claims**, c'est-à-dire l'email et les dates).

## 5. Vérifier la validité (`isTokenValid`)
```java
public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
}
```
Une fois que le scanner a extrait l'email depuis le badge (`username`), on vérifie 2 choses toutes simples :
1. Est-ce que l'email écrit sur le badge est bien le même que l'email de l'utilisateur qui fait la requête ?
2. Est-ce que le badge n'est pas expiré ? (En regardant la date d'expiration).

Si c'est OUI aux deux, la méthode renvoie `true` : l'utilisateur a le droit d'entrer !
