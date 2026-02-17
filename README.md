# README.md


## Initialisation et lancement des applications
Il faut récupérer au préalable le dépôt Git pour utiliser le MVP :
-       https://github.com/C-Pierre/OCR-Project-5


### Base de données
Lancer les commandes suivantes pour créer la DB et ses tables via le script fournit :
-       mysql -u root -p
        CREATE DATABASE IF NOT EXISTS mdd;
        CREATE USER IF NOT EXISTS 'myUser'@'localhost' IDENTIFIED BY 'MotDePasse';
        GRANT ALL PRIVILEGES ON mdd.* TO 'myUser'@'localhost';
        FLUSH PRIVILEGES;

Un script a été mit en place pour charger des fausses données dans le cadre du développement et des tests.
Se script est automatiquement exécuté à chaque lancement de l'API pour simplifier les différents tests. 


### API / Back-end
Compléter le fichier ./src/main/resources/application.properties avec les éléments suivants :
-       # Application
        spring.application.name=mdd-api
        spring.profiles.active=dev
        app.token.secret=TOkenBIDONs39SSosBnrhFcNivCLgvrq4EONxpvcIgR3F7Q7cdB0MryMiB1IoRiNusplHcE8jC4c5KMMTtuVm5Fc478845877a94d8d5e952f27ca51d3348ee779136b93ad259c533d54c5cbba5c
        app.frontUrl=http://localhost:4200
        app.token.expiration=86400000
        app.documentation.uri=/v3/api-docs
        app.swahher.uri=/swagger-ui
        app.security.public.paths=/api/auth/login,/api/auth/register,/api/subjects,/swagger-ui/**,/v3/api-docs/**

        # Server
        server.port=8080
        server.servlet.context-path=/

        # JPA / Hibernate
        spring.jpa.open-in-view=false
        spring.jpa.hibernate.ddl-auto=create-drop
        spring.jpa.show-sql=true
        spring.jpa.properties.hibernate.format_sql=true
        spring.sql.init.mode=always

        # Datasource
        spring.datasource.url=jdbc:postgresql://localhost:5432/mdd
        spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
        spring.datasource.username=myUser
        spring.datasource.password=MotDePasse
        spring.datasource.driver-class-name=org.postgresql.Driver
        spring.main.allow-bean-definition-overriding=true

Il faut ensuite se placer à la racine du projet dans le dossier `./back` et lancer la commande suivante :
-       mvn spring-boot:run

### Web-app / Front-end
Se placer à la racine du projet dans le dossier `./front`.
Installer l'application avec la commande :
-       npm install
Lancer ensuite l'application avec la commande :
-       npm run start || ng serve


## Utilisation
Une fois le front et le back initialisés et installés, l'application est disponible aux endpoints suivants :
- http://localhost:4200 (application : web-app + API)
- http://localhost:3001 (API)


## Documentation
Une documentation pour la partie back-enda été mise en place avec OpenApi.
Celle-ci est accessible sur les endpoints suivants une fois l'API lancée :
- http://localhost:3001/swagger-ui/index.html
- http://localhost:3001/v3/api-docs



## Fonctionnement

### Authentification
La quasi-totalité des routes nécessite d'être authentifié.

Depuis Postman ou équivalent, après le login et la récupération du token, il faut que celui-ci soit joint au Header, au niveau de l'Authorization, et ce pour chaque requête :
- Auth : Bearer Token
- Token : monSuperTokenBienLongJusteIci
- Prefix: Bearer
Le token est valide durant 24 heures.


### Body

Les requêtes POST et PUT ont un body sous forme de JSON : Content-type : application/json.


## Développement
L'API a été réalisée en Java 21, Spring 3 et Maven 3.

La documentation OpenAPI 3 est générée via les annotations placées dans les controllers.

Un système de cache a été mis en place dans les différents services principaux (User, Article, Themes). Il est possible de le vérifier depuis les logs du fait de la présence de logging.level.org.springframework.cache=TRACE dans application.properties.


## Arhitecture de l'API / back-end

-       /src
        ├── /main
        │   ├── /java/com/openclassrooms/mddapi
        │   │   ├── /api
        │   │   │   ├── /auth
        │   │   │   │   ├── /controller
        │   │   │   │   ├── /request
        │   │   │   │   └── /response
        │   │   │   ├── /comment
        │   │   │   │   ├── /controller
        │   │   │   │   └── /request
        │   │   │   ├── /common
        │   │   │   │   └── /response
        │   │   │   ├── /post
        │   │   │   │   ├── /controller
        │   │   │   │   └── /request
        │   │   │   ├── /subject
        │   │   │   │   └── /controller
        │   │   │   ├── /subscription
        │   │   │   │   ├── /controller
        │   │   │   │   └── /request
        │   │   │   └── /user
        │   │   │       ├── /controller
        │   │   │       └── /request
        │   │   ├── /application
        │   │   │   ├── /auth
        │   │   │   │   └── /service
        │   │   │   ├── /comment
        │   │   │   │   ├── /dto
        │   │   │   │   └── /service
        │   │   │   ├── /post
        │   │   │   │   ├── /dto
        │   │   │   │   └── /service
        │   │   │   ├── /subject
        │   │   │   │   ├── /dto
        │   │   │   │   └── /service
        │   │   │   ├── /subscription
        │   │   │   │   ├── /dto
        │   │   │   │   └── /service
        │   │   │   └── /user
        │   │   │       ├── /dto
        │   │   │       └── /service
        │   │   ├── /domain
        │   │   │   ├── /comment
        │   │   │   │   ├── /entity
        │   │   │   │   └── /repository
        │   │   │   ├── /post
        │   │   │   │   ├── /entity
        │   │   │   │   └── /repository
        │   │   │   ├── /subject
        │   │   │   │   ├── /entity
        │   │   │   │   └── /repository
        │   │   │   ├── /subscription
        │   │   │   │   ├── /entity
        │   │   │   │   └── /repository
        │   │   │   └── /user
        │   │   │       ├── /entity
        │   │   │       └── /repository
        │   │   └── /infrastructure
        │   │       ├── /auth
        │   │       │   ├── /jwt
        │   │       │   └── /validator
        │   │       ├── /comment
        │   │       │   ├── /mapper
        │   │       │   └── /repository
        │   │       │       ├── /adapter
        │   │       │       └── /port
        │   │       ├── /common
        │   │       ├── /configuration
        │   │       ├── /post
        │   │       │   ├── /mapper
        │   │       │   └── /repository
        │   │       │       ├── /adapter
        │   │       │       └── /port
        │   │       ├── /subject
        │   │       │   ├── /mapper
        │   │       │   └── /repository
        │   │       │       ├── /adapter
        │   │       │       └── /port
        │   │       ├── /subscription
        │   │       │   ├── /authorization
        │   │       │   └── /repository
        │   │       │       ├── /adapter
        │   │       │       └── /port
        │   │       └── /user
        │   │           ├── /authorization
        │   │           ├── /mapper
        │   │           └── /repository
        │   │               ├── /adapter
        │   │               └── /port
        │   │       └── MddApiApplication.java
        │   └── /resources
        │       ├── application.properties
        │       └── sql/init.sql
        └── /test
            └── /java/com/openclassrooms/mddapi

L’architecture du back-end a été conçue selon une approche en couches inspirée du modèle hexagonal (ou architecture ports & adapters), combinée à une séparation claire API / Application / Domain / Infrastructure. Cette organisation permet de découpler la logique métier des détails techniques (accès à la base, sécurité, mapping, etc.) et de rendre le code plus maintenable et testable.

La couche domain contient les entités et les repositories principaux, représentant le cœur métier de l’application.

La couche application centralise la logique métier, les services et les DTO, ce qui permet aux tests de se concentrer sur le comportement sans dépendre de la couche infrastructure.

La couche api gère les contrôleurs REST et les objets de requête/réponse, isolant la communication avec le client.

La couche infrastructure implémente les détails techniques : persistance via JPA, mappers, sécurité JWT, adapters pour Testcontainers, etc.

Cette organisation facilite également l’évolution et la réutilisation des composants, car chaque couche a des responsabilités bien définies. Elle permet de tester chaque partie de manière isolée, notamment la logique métier, et de changer des technologies externes (base de données, librairies, framework) sans impacter le cœur de l’application.

## Architecture de la web-app / front-end

Le front-end a été organisé en suivant une approche inspirée de l’Atomic Design, afin de structurer les composants de manière claire et réutilisable. La hiérarchie principale est la suivante :
-       /src/app
        ├── /pages
        │   └── Contient les pages complètes de l’application, correspondant aux vues principales accessibles via le routing.
        ├── /components
        │   ├── /elements
        │   │   └── Composants les plus atomiques (boutons, champs de formulaire, icônes), servant de blocs de base.
        │   ├── /parts
        │   │   └── Composants intermédiaires, composés de plusieurs éléments (cartes, listes, formulaires simples).
        │   └── /sections
        │       └── Composants plus complexes, combinant plusieurs parts pour constituer des sections d’une page (ex : en-tête de tableau, sections de profil).
        └── /core
            └── Contient la logique centrale de l’application, services, modèles de données et utilitaires partagés.

Cette organisation vise à favoriser la réutilisabilité et la maintenabilité du code, tout en facilitant l’évolution des composants et des pages au fur et à mesure des besoins de l’application.


## Dépendances de l'API / back-end

Présentation des dépendances / librairies utilisées dans l’API.

### Spring Boot Starters
- spring-boot-starter-web : Fournit les fonctionnalités Spring pour créer des API REST et servir des pages web.
- spring-boot-starter-data-jpa : Fournit JPA/Hibernate pour la gestion des entités, ORM et accès à la base de données.
- spring-boot-starter-validation : Permet l’utilisation des annotations de validation (@NotNull, @Size, etc.) via Jakarta Bean Validation.
- spring-boot-starter-security : Ajoute Spring Security pour l’authentification et l’autorisation.
- spring-boot-starter-test : Fournit des outils pour tester Spring Boot (unitaires, intégration, MockMvc).

### Base de données
- postgresql : Driver JDBC pour PostgreSQL, nécessaire pour se connecter à la base de données en runtime.
- h2 (scope test) : Base de données en mémoire pour les tests unitaires.

### MapStruct
- mapstruct : Permet de générer automatiquement du code de mapping entre objets (DTO ↔ Entity).
- mapstruct-processor (via annotationProcessor) : Génère le code MapStruct au moment de la compilation.

### JWT
- jjwt : Fournit les classes nécessaires pour créer, signer et parser des JWT (JSON Web Tokens).

### Testcontainers
- testcontainers-bom : Gestion centralisée des versions des modules Testcontainers.
- org.testcontainers:junit-jupiter : Fournit l’intégration avec JUnit 5 pour les conteneurs Docker pendant les tests.
- org.testcontainers:postgresql : Permet de démarrer une instance PostgreSQL dans un conteneur pour les tests.

### Spring Security Test
- spring-security-test : Fournit des utilitaires pour tester l’authentification et l’autorisation dans Spring Security.

### Swagger/OpenAPI
- springdoc-openapi-starter-webmvc-ui : Génère automatiquement la documentation OpenAPI (Swagger UI) pour les endpoints REST.

### Plugins Maven
- spring-boot-maven-plugin : Permet de packager l’application Spring Boot en jar/war exécutable.
- maven-compiler-plugin : Configure le compilateur Java (source/target), active les annotation processors (MapStruct).
- jacoco-maven-plugin : Permet de générer des rapports de couverture de code et de définir des seuils minimums pour les tests.

### Lombok
- Dans ce projet, Lombok n’a pas été utilisé afin de conserver une lisibilité complète du code et un contrôle explicite sur les méthodes générées.
- Les entités JPA définissent donc manuellement leurs getters et setters, ce qui permet de suivre clairement le comportement des objets et facilite le débogage.
- Pour les DTO, nous avons choisi d’utiliser les record Java, offrant une syntaxe concise et immutable tout en conservant la simplicité pour les tests.
- Cette approche a été privilégiée pour faciliter l’écriture et la maintenance des tests unitaires et d’intégration, en rendant le comportement des objets plus explicite et prévisible.

## Couverture de tests

### Back-end
100% de coverage
Le rapport de tests est disponible ici:
- ./back/target/site/jacoco/index.html
Les tests sont exécutables à la racine du projet depuis le dossier `./back` avec la commande suivante :
-       mvn clean test

### Front-end
100% de coverage
Le rapport de tests est disponible ici:
- ./front/coverage/lcov-report/index.html
Pour faciliter la lecture du rapport tout le dossier /coverage a été versionné.
Les tests sont exécutables à la racine du projet depuis le dossier `./front` avec les commandes suivantes :
-       npm run test
-       npm run test:coverage

### EndToEnd
Les différentes interfaces et actions utilisateurs on été testés.
Le rapport de tests est disponible ici :
- ./front/cypress/TODO
Les tests sont exécutables à la racine du projet depuis le dossier `./front` avec les commandes suivantes :
-       npm run e2e
-       npm run cypress:report