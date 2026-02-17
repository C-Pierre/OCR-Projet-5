# Revue technique

Projet : MDD – Monde de Dév  Version : MVP Date : 2026-02-16
Rédacteur : Pierre - Développeur Full-Stack Cadre : Revue technique interne

## Analyse globale
Le projet repose sur une architecture moderne, conforme aux contraintes imposées :
    • séparation front-end / back-end,
    • API REST sécurisée, respect des principes SOLID.


## Analyse par couche

### Front-end
    • Structure modulaire Angular.
    • Services dédiés aux appels API.
    • Composants réutilisables.
    • Gestion centralisée des erreurs.

#### Axes d’amélioration
    • Factorisation de certains composants.
    • Utilisation / pagination du trie sur les thèmes.

### Back-end
    • Couche service bien définie.
    • Repositories JPA clairs et cohérents.
    • Sécurité intégrée dès la conception.

#### Axes d’amélioration
    • Pagination généralisée.
    • Optimisation des requêtes complexes.
    • Centralisation complète des validations.
    • Utilisation / pagination du trie sur les thèmes.

## Dette technique
    • Dette volontairement limitée pour respecter les délais MVP.
    • Refactorisations possibles sans remise en cause de l’architecture.

## Conclusion
Le projet est techniquement sain, maintenable, et évolutif.
Les choix réalisés sont cohérents avec les contraintes et les objectifs fixés.

## Signature
Pierre Cistac