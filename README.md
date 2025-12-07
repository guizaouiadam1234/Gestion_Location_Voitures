# location-voitures — Application de gestion de locations automobiles

Ce dépôt contient une application de démonstration pour la gestion de locations de véhicules, développée avec Spring Boot et MongoDB. Le projet met l'accent sur une séparation claire des responsabilités, des tests et l'application de patterns (Facade, Strategy, Event-driven).

## Vue d'ensemble

Principales entités métiers :
- Client
- Véhicule
- Contrat

Le code met en œuvre des règles métiers importantes :
- Empêcher la création d'un contrat si le véhicule est en panne (`EN_PANNE`).
- Lorsqu'un véhicule passe en `EN_PANNE`, annulation automatique des contrats en `EN_ATTENTE` pour ce véhicule.
- Marquage automatique des contrats en retard (`EN_RETARD`) via une tâche planifiée.

## Structure du projet (par fonctionnalité/layer)

src/main/java/com/location/location_voitures
- api.controller      → contrôleurs REST (Endpoints)
- api.dto             → DTOs exposés par l'API (validation avec Jakarta Validation)
- api.model           → entités persistées (@Document MongoDB)
- api.repository      → interfaces Spring Data MongoDB
- api.service         → logique métier, publication d'événements
- api.events          → classes d'événements et listeners (Spring Events)
- api.service.state   → stratégie pour transitions d'état (Strategy pattern)
- api.scheduler       → tâches planifiées (ex. marquer `EN_RETARD`)

Les tests sont dans `src/test/java` et couvrent tests unitaires et un test d'intégration avec Mongo embarqué.

## Composants et patterns utilisés

- Repository (Spring Data): persist et requêtes MongoDB.
- Service layer: logique métier et transactions applicatives.
- DTOs + validation: toutes les entrées REST utilisent des DTOs avec annotations de validation (`@NotBlank`, `@NotNull`, `@Past`, ...).
- Events (Observer): passage d'un véhicule en panne publie un `VehicleStateChangedEvent`; un listener réagit et délègue aux strategies.
- Strategy pattern: implémentations pour gérer les transitions d'état des contrats (ex. `CancelOnVehiclePanneStrategy`, `MarkLateStrategy`).
- Facade: `RentalFacade` orchestre scénarios complexes (ex. création de client + contrat en une seule opération).
- Scheduler: tâche périodique (`ContractScheduler`) qui applique la stratégie de marquage `EN_RETARD`.

## Tests

- Tests unitaires: JUnit 5 + Mockito couvrent services, controllers et listeners.
- Test d'intégration (embedded Mongo): un test d'intégration utilise MongoDB embarqué (Flapdoodle) pour valider le comportement end-to-end — création client/véhicule/contrat et annulation automatique des contrats lors d'une panne de véhicule.

Note: La dépendance Flapdoodle est déclarée en scope `test` dans le `pom.xml`. Selon votre accès à Maven Central, vous pourriez avoir à ajuster la version utilisée.

## Contrôleurs et validation

- Les contrôleurs REST retournent désormais des `ResponseEntity<T>` pour exposer correctement les codes HTTP (201 Created, 200 OK, 204 No Content, 404 Not Found).
- Les DTOs sont annotés pour la validation et les endpoints acceptent `@Valid` sur les corps de requête. Une gestion d'erreurs centralisée (`RestExceptionHandler`) mappe les erreurs métiers en réponses HTTP appropriées.

## Comment lancer le projet

Prérequis:
- JDK 17+
- Maven
- MongoDB (local) ou une URL MongoDB (Atlas)

Exemples de commandes (PowerShell / Windows):

```powershell
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

Configuration MongoDB (exemple local):

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/location-voitures
```

## Exécuter les tests

Unitaires :

```powershell
mvnw.cmd test -DskipTests=false
```

Integration (avec Mongo embarqué) : la dépendance Flapdoodle est utilisée en scope `test` pour démarrer Mongo en mémoire lors des tests d'intégration.

## Scénarios API (exemples rapides pour Postman)

1) Créer un client (POST `/api/clients`) — retourne 201 et le client créé.
2) Créer un véhicule (POST `/api/vehicules`) — retourne 201.
3) Créer un contrat (POST `/api/contrats`) — si le véhicule est `DISPONIBLE`, contrat créé en `EN_ATTENTE`.
4) Mettre à jour le véhicule en `EN_PANNE` (PUT `/api/vehicules/{id}`) — les contrats `EN_ATTENTE` sont annulés automatiquement.

## Points d'amélioration recommandés

- Remplacer les mappers manuels par MapStruct pour réduire le boilerplate.
- Ajouter des tests d'intégration supplémentaires (contrôleurs via MockMvc ou TestRestTemplate).
- Documenter l'API avec OpenAPI / springdoc.
- Ajouter CI (GitHub Actions) pour exécuter tests et lint à chaque push.

## Fichiers importants

- `src/main/java/.../api/controller` — endpoints REST
- `src/main/java/.../api/service` — logique métier
- `src/main/java/.../api/service/state` — stratégies de transition d'état
- `src/main/java/.../api/events` — événements et listeners
- `src/test/java/.../integration` — test d'intégration avec Mongo embarqué

---
Si vous souhaitez, je peux :
- ajouter une collection Postman complète,
- convertir tous les mappers vers MapStruct,
- ajouter un pipeline CI pour exécuter les tests automatiquement.
