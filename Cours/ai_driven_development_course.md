# Développer avec l'IA : Le Guide du Tech Lead

Le métier de développeur a changé. Vous n'êtes plus seulement celui qui tape le code, vous êtes devenu un **Chef d'Orchestre (Tech Lead)**. L'IA est votre équipe de développeurs ultra-rapides, mais qui ont besoin d'instructions parfaites pour ne pas faire n'importe quoi.

Voici la méthode exacte pour piloter une IA de la création d'un projet jusqu'à sa mise en production, avec un code de niveau Senior.

---

## PHASE 1 : Le Cadrage (Avant d'écrire du code)

Le plus grand piège est de demander à l'IA d'écrire du code dès le premier message. Un développeur Senior commence toujours par l'architecture.

**Le Prompt de départ parfait :**
> *"Agis comme un Architecte Logiciel Senior. Je veux créer un backend pour une application Flutter de type [Idée : ex. Uber Eats]. L'environnement technique est : Spring Boot 3, Java 17+, Spring Data JPA, et MySQL.*
> *Ne génère aucun code pour l'instant. Propose-moi la structure de la base de données (Les Entités et leurs relations @OneToMany / @ManyToOne).*
> *Attends ma validation pour continuer."*

**L'intérêt :** Vous vérifiez que l'IA a bien compris le métier avant qu'elle ne se perde dans le code. 
*Note pour Antigravity : Vous pouvez utiliser la commande `/boost` pour que je réfléchisse plus profondément à l'architecture !*

---

## PHASE 2 : L'Implémentation Itérative (Le Pilotage)

Une fois l'architecture validée, on passe à la construction. La règle d'or : **Couche par couche, entité par entité.**

Dans vos requêtes, vous devez IMPOSER vos règles (vos "Standards de Code"). Si vous ne le faites pas, l'IA utilisera des vieilles pratiques trouvées sur internet en 2015.

**Le Prompt de Génération parfait :**
> *"Nous allons coder l'Entité `Utilisateur`. Génère le code en respectant STRICTEMENT ces contraintes :*
> *1. Utilise l'architecture 3 Tiers (Controller, Service, Repository).*
> *2. N'utilise JAMAIS les Entités dans le Contrôleur. Crée des DTOs en utilisant la syntaxe Java `record`.*
> *3. Utilise l'injection de dépendances par Constructeur (interdiction d'utiliser `@Autowired`).*
> *4. Sécurise le DTO d'entrée avec la validation automatique (`@Valid`, `@NotBlank`).*
> *5. Ne mets AUCUN bloc `try/catch` dans le Contrôleur. Utilise les Exceptions que je gérerai dans un `@ControllerAdvice`.*
> *Donne-moi d'abord le Service, je validerai, puis tu me donneras le Contrôleur."*

---

## PHASE 3 : Le Débogage Avancé

Quand vous rencontrez une erreur (écran rouge sur Spring Boot), la pire chose à faire est de dire : *"Ça ne marche pas, corrige."* L'IA va vous inventer des solutions hasardeuses.

Puisque vous utilisez **Antigravity** (qui a accès à votre Mac), voici comment déboguer comme un pro :

**Le Prompt de Débogage parfait :**
> *"J'ai obtenu cette erreur dans ma console : [Copiez-collez l'erreur].*
> *Mon projet se trouve dans le dossier `/Users/pc/.../monprojet`.*
> *Scanne mes fichiers, trouve la cause logique de cette erreur, et explique-la moi au lieu de me donner bêtement le code corrigé."*

---

## Le Dictionnaire Magique du Pilote IA

Ajoutez ces "Mots Magiques" dans vos requêtes pour instantanément forcer l'IA à augmenter le niveau technique de ses réponses :

| Mot Magique | Ce que ça dit à l'IA |
| :--- | :--- |
| **"Architecture RESTful"** | *Fais de jolies URLs (/api/users/1) au lieu d'URLs bizarres (/deleteUser).* |
| **"Stateless" / "JWT"** | *Prépare le système pour qu'il soit sécurisé par Token pour mon application Flutter, sans garder la session en mémoire.* |
| **"Pageable"** | *N'envoie jamais 1000 résultats d'un coup, configure la pagination automatiquement.* |
| **"Principes SOLID"** | *Sépare bien les responsabilités. Un fichier = Une seule mission.* |
| **"DRY (Don't Repeat Yourself)"** | *Si tu vois que je copie-colle du code, crée-moi une méthode privée pour optimiser.* |
| **"Garde la rétrocompatibilité"** | *Je modifie mon API, mais fais attention à ne pas casser mon application Flutter qui tourne déjà !* |
