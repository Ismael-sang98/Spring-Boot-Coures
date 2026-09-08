# 🔐 Roadmap : Spring Security + JWT

> **Objectif** : Implémenter un système d'authentification complet avec JWT dans Spring Boot.
> Tu lis chaque étape, tu comprends, et tu écris le code toi-même.

---

## 🧠 Avant de commencer — Comprendre le flux

Avant de toucher au code, tu dois avoir ce schéma en tête :

```
[Client]  →  POST /auth/register  →  [Serveur crée le compte]
[Client]  →  POST /auth/login     →  [Serveur retourne un TOKEN JWT]
[Client]  →  GET /api/...         →  [Client envoie le TOKEN dans le header]
                                  →  [Serveur vérifie le TOKEN]
                                  →  [Serveur autorise ou refuse]
```

**C'est quoi un JWT ?**
Un JWT (JSON Web Token) est une chaîne de texte encodée en Base64, divisée en 3 parties :
- **Header** : le type et l'algorithme de signature
- **Payload** : les données (ex: email, rôle, date d'expiration)
- **Signature** : garantit que personne n'a modifié le token

> Le serveur signe le token avec une **clé secrète**. Si quelqu'un modifie le token, la signature ne correspond plus et le serveur le rejette.

---

## 📦 Étape 1 — Ajouter les dépendances

**Ce que tu dois faire :**
Ouvre ton `pom.xml` et ajoute deux dépendances :

1. **Spring Security** : cherche `spring-boot-starter-security` sur [Maven Repository](https://mvnrepository.com)
2. **JJWT** : cherche `jjwt-api`, `jjwt-impl` et `jjwt-jackson` de `io.jsonwebtoken` (version 0.11.5 recommandée)

**Pourquoi ?**
- `spring-boot-starter-security` : ajoute toute la mécanique de sécurité Spring
- `jjwt` : la librairie pour créer, signer et lire les tokens JWT

> ⚠️ Dès que tu ajoutes Spring Security, **toutes tes routes sont automatiquement bloquées**. C'est normal. Tu vas tout configurer à l'étape 8.

---

## 👤 Étape 2 — Créer l'entité `User`

**Ce que tu dois faire :**
Crée une classe `User` dans ton dossier `model/`.

Elle doit contenir :
- `id` (Long, auto-généré)
- `email` (String, unique)
- `password` (String — ce sera le mot de passe **hashé**, jamais en clair)
- `role` (String ou Enum — ex: `"USER"`, `"ADMIN"`)

**La partie importante :**
Cette classe doit **implémenter l'interface `UserDetails`** de Spring Security.

`UserDetails` est le contrat que Spring attend. En l'implémentant, tu dis à Spring :
*"Voici comment représenter un utilisateur dans mon application."*

Les méthodes à implémenter :
- `getUsername()` → retourne l'email
- `getPassword()` → retourne le mot de passe hashé
- `getAuthorities()` → retourne le rôle (transformé en `GrantedAuthority`)
- `isAccountNonExpired()`, `isAccountNonLocked()`, `isCredentialsNonExpired()`, `isEnabled()` → retourne `true` pour l'instant

---

## 🗄️ Étape 3 — Créer le `UserRepository`

**Ce que tu dois faire :**
Crée une interface `UserRepository` qui étend `JpaRepository<User, Long>`.

Ajoute une seule méthode personnalisée :
- Trouver un utilisateur **par son email** (Spring Data génère la requête automatiquement depuis le nom de la méthode)

**Pourquoi ?**
À la connexion, l'utilisateur envoie son email. Tu dois pouvoir retrouver son compte en base depuis cet email.

---

## 🔍 Étape 4 — Créer le `UserDetailsService`

**Ce que tu dois faire :**
Crée une classe `UserDetailsServiceImpl` (ou `CustomUserDetailsService`) qui **implémente `UserDetailsService`**.

Cette interface a **une seule méthode** à implémenter : `loadUserByUsername(String username)`.

Dedans :
1. Utilise ton `UserRepository` pour chercher l'utilisateur par email
2. Si l'utilisateur n'existe pas → lance une `UsernameNotFoundException`
3. Si il existe → retourne-le directement (car `User` implémente déjà `UserDetails`)

**Pourquoi ?**
Spring Security appelle cette méthode automatiquement lors de l'authentification pour charger l'utilisateur depuis ta base de données.

---

## 🔑 Étape 5 — Créer l'utilitaire `JwtUtils`

**Ce que tu dois faire :**
Crée une classe `JwtUtils` (ou `JwtService`) dans un dossier `security/` ou `util/`.

Cette classe doit gérer **3 responsabilités** :

### 5.1 — Générer un token
- Prend un `UserDetails` en paramètre
- Utilise `Jwts.builder()` pour construire le token
- Définit : le sujet (email), la date de création, la date d'expiration
- Signe le token avec une **clé secrète** (stocke-la dans `application.yml`, pas dans le code)
- Retourne le token sous forme de `String`

### 5.2 — Extraire l'email depuis un token
- Prend un token `String` en paramètre
- Utilise `Jwts.parserBuilder()` pour lire le token
- Extrait et retourne le "subject" (qui est l'email)

### 5.3 — Valider un token
- Prend un token et un `UserDetails` en paramètre
- Vérifie que l'email dans le token correspond à l'utilisateur
- Vérifie que le token n'est pas expiré
- Retourne `true` ou `false`

> 💡 La clé secrète doit être longue et complexe. Mets-la dans `application.yml` sous une propriété comme `app.jwt.secret` et lis-la avec `@Value`.

---

## 🛡️ Étape 6 — Créer le filtre JWT `JwtAuthenticationFilter`

**Ce que tu dois faire :**
Crée une classe `JwtAuthenticationFilter` qui **étend `OncePerRequestFilter`**.

Ce filtre est **le gardien** : il s'exécute sur chaque requête HTTP avant qu'elle arrive au controller.

La logique à implémenter dans la méthode `doFilterInternal()` :

1. Lire le header `Authorization` de la requête HTTP
2. Vérifier qu'il commence par `"Bearer "` — sinon, passer à la suite sans rien faire
3. Extraire le token (tout ce qui vient après `"Bearer "`)
4. Extraire l'email depuis le token avec ta `JwtUtils`
5. Vérifier que l'utilisateur n'est pas déjà authentifié dans le contexte Spring
6. Charger l'utilisateur depuis la base avec ton `UserDetailsService`
7. Valider le token avec ta `JwtUtils`
8. Si valide → créer un objet `UsernamePasswordAuthenticationToken` et le mettre dans le `SecurityContextHolder`
9. Continuer la chaîne de filtres avec `filterChain.doFilter()`

**Pourquoi le `SecurityContextHolder` ?**
C'est là que Spring Security stocke l'identité de l'utilisateur courant. Si tu y mets quelque chose, Spring considère que l'utilisateur est authentifié pour cette requête.

---

## ⚙️ Étape 7 — Configurer Spring Security `SecurityConfig`

**Ce que tu dois faire :**
Crée une classe `SecurityConfig` annotée avec `@Configuration` et `@EnableWebSecurity`.

Tu vas y déclarer un bean `SecurityFilterChain`. C'est ici que tu définis les règles globales de sécurité :

### Ce que tu dois configurer :
- **Désactiver CSRF** : pas nécessaire pour une API REST stateless
- **Définir les routes publiques** : `/api/auth/**` accessible sans token
- **Protéger le reste** : toutes les autres routes nécessitent d'être authentifié
- **Déclarer la session stateless** : Spring ne doit pas créer de session HTTP (on utilise JWT)
- **Ajouter ton filtre** : insère ton `JwtAuthenticationFilter` avant le filtre standard de Spring

### Les beans à déclarer dans cette classe :
- `PasswordEncoder` → utilise `BCryptPasswordEncoder` (pour hasher les mots de passe)
- `AuthenticationManager` → récupéré depuis `AuthenticationConfiguration`, utilisé dans le controller pour vérifier les credentials

---

## 🚪 Étape 8 — Créer le `AuthController`

**Ce que tu dois faire :**
Crée un `AuthController` mappé sur `/api/auth`.

### Endpoint 1 : `POST /api/auth/register`
Reçoit un objet (email + password) dans le body.

La logique :
1. Vérifier que l'email n'existe pas déjà en base
2. **Hasher le mot de passe** avec ton `PasswordEncoder` avant de sauvegarder
3. Créer et sauvegarder le `User` en base
4. Retourner un message de succès (ou directement un token si tu veux connecter l'utilisateur directement après l'inscription)

### Endpoint 2 : `POST /api/auth/login`
Reçoit un objet (email + password) dans le body.

La logique :
1. Utiliser l'`AuthenticationManager` pour vérifier les credentials
2. Si les credentials sont incorrects → Spring lève automatiquement une exception
3. Si c'est correct → charger l'utilisateur depuis la base
4. Générer un token JWT avec ta `JwtUtils`
5. Retourner le token dans la réponse

---

## 🧪 Étape 9 — Tester avec Postman

Teste dans cet ordre :

**Test 1 — Inscription**
- `POST /api/auth/register`
- Body : `{ "email": "test@mail.com", "password": "motdepasse" }`
- Résultat attendu : succès

**Test 2 — Connexion**
- `POST /api/auth/login`
- Body : `{ "email": "test@mail.com", "password": "motdepasse" }`
- Résultat attendu : reçois un token JWT

**Test 3 — Route protégée sans token**
- `GET /api/...` (n'importe quelle route protégée)
- Sans header Authorization
- Résultat attendu : **401 Unauthorized**

**Test 4 — Route protégée avec token**
- `GET /api/...`
- Header : `Authorization: Bearer <colle_ton_token_ici>`
- Résultat attendu : **200 OK** ✅

---

## 📋 Checklist récapitulative

```
[ ] Étape 1  → Dépendances ajoutées (spring-security + jjwt)
[ ] Étape 2  → Entité User créée (implémente UserDetails)
[ ] Étape 3  → UserRepository créé (avec findByEmail)
[ ] Étape 4  → UserDetailsService implémenté
[ ] Étape 5  → JwtUtils créé (générer / extraire / valider)
[ ] Étape 6  → JwtAuthenticationFilter créé
[ ] Étape 7  → SecurityConfig configuré
[ ] Étape 8  → AuthController créé (register + login)
[ ] Étape 9  → Tests Postman passés ✅
```

---

## 💡 Conseils

- **Va dans l'ordre.** Chaque étape dépend de la précédente.
- **Lis les erreurs attentivement.** Spring Security donne des messages clairs.
- **Si tu bloques** sur un concept, demande une explication — pas le code.
- **Ne copie pas de code.** Même si tu ne comprends pas tout, essaie d'abord.

---

> Bon courage ! L'auth est souvent la partie la plus difficile pour les débutants, mais une fois comprise, elle devient une seconde nature. 💪
