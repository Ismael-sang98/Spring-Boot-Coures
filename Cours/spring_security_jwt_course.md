# Comprendre Spring Security et JWT

Pour comprendre comment sécuriser votre API pour Flutter, oubliez le code quelques minutes. Imaginez que votre backend Spring Boot est un **Hôtel de Luxe très sécurisé**.

## 1. L'Analogie de l'Hôtel (Comment ça marche ?)

Actuellement, n'importe qui peut entrer dans votre API et fouiller dans les données. Avec Spring Security, nous allons embaucher du personnel de sécurité.

### A. La Réceptionniste (Le Contrôleur d'Authentification)
Quand l'utilisateur Flutter lance l'application pour la première fois, il va à l'adresse `/login` avec son Email et son Mot de passe.
La réceptionniste vérifie dans le grand registre de l'hôtel (MySQL) si les informations sont bonnes. Si oui, **elle lui donne un Badge VIP (Le JWT)** et le laisse entrer.

### B. Le Videur (Le Filtre de Sécurité)
Désormais, le client a son badge (JWT). Quand Flutter veut faire un retrait d'argent (ou voir une liste de vols), il n'a plus besoin d'envoyer son mot de passe !
Flutter montre juste son badge au "Videur" à l'entrée de l'hôtel. Le videur scanne le badge, voit qu'il est valide, et laisse passer la requête.

---

## 2. Le Hachage des mots de passe (BCrypt)

**Règle d'or absolue : On ne sauvegarde JAMAIS un mot de passe en texte clair (`1234`) dans MySQL.** 
Même le créateur de l'application (vous) n'a pas le droit de connaître le mot de passe de ses utilisateurs.

Spring Security utilise un outil mathématique appelé **BCrypt**. 
Quand l'utilisateur tape `1234`, BCrypt le hache instantanément pour le transformer en quelque chose comme : `$2a$10$k1a8j...`
C'est ça que vous sauvegardez dans MySQL ! 

Même si un hacker vole votre base de données, il ne verra que cette suite de caractères impossible à déchiffrer. Lors du login, BCrypt fera le calcul mathématique inverse pour vérifier si `1234` correspond bien au charabia en base.

---

## 3. Le fameux Badge : Le JWT (JSON Web Token)

C'est quoi ce fameux badge que la réceptionniste donne à Flutter ? C'est un **Token JWT**.
Un JWT est une longue chaîne de texte (très longue) séparée par deux points, qui ressemble à ça :
`eyJhbGciOiJIUzI1NiIsIn... . eyJzdWIiOiIxMjM0NTY3O... . SflKxwRJSMeKKF2QT4fwpMe...`

Ce texte contient 3 parties secrètes :
1. **L'En-tête** : Dit comment c'est crypté.
2. **Le Contenu (Le Payload)** : Contient les infos non-sensibles de l'utilisateur (ex: "Pseudo: Ismael", "Rôle: ADMIN", "Expire: dans 24h").
3. **La Signature** : C'est le tampon officiel de l'hôtel. Spring Boot signe ce token avec une **Clé Secrète** très compliquée que lui seul connaît. 

### Pourquoi c'est génial pour Flutter ?
Parce que si un hacker prend le token et essaie de modifier le "Rôle" pour passer de USER à ADMIN, la "Signature" du tampon officiel va se briser. Quand le Videur (Spring Boot) scannera le badge, il verra que la signature est fausse et jettera le hacker dehors (Erreur 403 Forbidden).

Flutter stockera ce Token dans la mémoire sécurisée du téléphone, et l'ajoutera automatiquement dans les entêtes de toutes ses prochaines requêtes HTTP (dans ce qu'on appelle un header `Authorization: Bearer <TOKEN>`).

---

## 4. La Feuille de route (Ce que nous allons coder)

Mettre ça en place demande de la méthode. Nous allons le faire en 4 étapes dans votre projet :
1. **L'Entité Spéciale** : Créer un `Utilisateur` qui obéit aux règles strictes de Spring Security.
2. **La Machinerie JWT** : Créer un Service pour générer et décrypter les Badges VIP.
3. **Le Videur (JwtFilter)** : Créer le code qui intercepte chaque requête de Flutter pour vérifier la présence du Badge.
4. **La Configuration Globale** : Dire à Spring quelles portes de l'hôtel sont publiques (ex: `/login`, `/inscription`) et quelles portes nécessitent le badge (ex: `/vols`, `/reservations`).
