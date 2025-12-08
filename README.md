 location-voitures — Application de gestion de locations automobiles

Ce dépôt contient une application de démonstration pour la gestion des locations de véhicules. Le but ici est pédagogique et industriel : proposer une architecture modulaire, testée, et appliquant plusieurs patterns (Facade, Strategy, Event-driven) pour decoupler règles métiers et effets secondaires.

**Résumé fonctionnel**
- Gestion des clients, véhicules et contrats de location.
- Règles métiers principales implémentées :
	- Impossible de créer un contrat si le véhicule est en panne (`EN_PANNE`).
	- Lorsqu'un véhicule passe en `EN_PANNE`, tous les contrats `EN_ATTENTE` pour ce véhicule sont annulés automatiquement.
	- Une tâche planifiée marque les contrats dont la date de début est dépassée comme `EN_RETARD`.

**Technologies**
- Java
- Spring Boot 3.x
- Spring Data MongoDB
- Jakarta Validation (spring-boot-starter-validation)
- Lombok
- JUnit 5, Mockito
- Maven

**Organisation du code (couches et dossiers)**

src/main/java/com/location/location_voitures
- `api.controller`  — contrôleurs REST
- `api.dto`         — DTO exposés par l'API (avec validation `@NotBlank`, `@NotNull`, ...)
- `api.model`       — entités MongoDB (`@Document`)
- `api.repository`  — interfaces Spring Data (p.ex. `ClientRepository`)
- `api.service`     — logique métier et orchestration (publie des événements)
- `api.events`      — événements et listeners (Spring Events)
- `api.service.state` — stratégies de transition d'état (Strategy pattern)
- `api.scheduler`   — tâches planifiées (ex : marquage `EN_RETARD`)

Chaque dossier regroupe la responsabilité principale d'une couche : les controllers exposent les endpoints, les services encapsulent la logique métier et font appel aux repositories pour persister.

**Modèle de données (principaux éléments)**
- `Client` : nom, prénom, dateNaissance, numeroPermis, adresse
- `Vehicle` : marque, modele, immatriculation (index unique), dateAcquisition, etat (enum `VehicleState` : DISPONIBLE, EN_LOCATION, EN_PANNE)
- `Contract` : clientId, vehicleId, dateDebut, dateFin, etat (enum `ContractState` : EN_ATTENTE, EN_COURS, TERMINE, EN_RETARD, ANNULE)

**Patterns Architecturaux et choix de conception**

- Repository (Spring Data) — abstraction d'accès aux données, facilite les requêtes et la persistance.
- Service layer — centralise la logique métier et évite la logique dans les controllers.
- DTOs + Validation — séparation entre entités persistées et payloads HTTP ; validation avec Jakarta Validation (`@Valid`).
- Event-driven (Spring Application Events) — découplage entre déclencheurs (p.ex. changement d'état véhicule) et effets (annulation de contrats). Exemple : `VehicleStateChangedEvent` publié depuis `VehicleService`, consommé par `ContractEventListener`.
- Strategy pattern — règles de transition d'état des contrats encapsulées en stratégies (`ContractStateTransitionStrategy` et implémentations comme `CancelOnVehiclePanneStrategy`, `MarkLateStrategy`). Permet d'ajouter/retirer règles sans modifier le pipeline.
- Facade — `RentalFacade` pour orchestrer scénarios complexes (création client + contrat, validations transverses).
- Scheduler — `ContractScheduler` exécute périodiquement le marquage des contrats en retard.
- Mapper (manuel aujourd'hui) — conversion `Entity <-> DTO`. Recommandation : remplacer par MapStruct pour réduire boilerplate.

**Règles métiers importantes (implémentées)**

- Création de contrat : refuse la création si le véhicule est `EN_PANNE`.
- Transition véhicule → `EN_PANNE` : publication d'un événement ; l'auditeur lance les stratégies qui annulent les contrats `EN_ATTENTE` pour ce véhicule.
- Tâche planifiée : marque les contrats arrivant en retard (`EN_RETARD`) — stratégie dédiée.

**Contrôleurs (exemples d'API)**

- `POST /api/clients` : crée un client — retourne `201 Created` + payload `ClientDTO`.
- `GET  /api/clients` : liste les clients — retourne `200 OK`.
- `POST /api/vehicules` : crée un véhicule — retourne `201 Created`.
- `PUT  /api/vehicules/{id}` : met à jour un véhicule ; si `etat` passe à `EN_PANNE` un événement est émis.
- `POST /api/contrats` : crée un contrat (DTO validé avec `@Valid`) — `201 Created` si OK.
- `POST /api/rentals` : endpoint façade pour créer client + contrat en une seule opération.

Les controllers renvoient des `ResponseEntity<T>` pour exposer explicitement les codes HTTP (201, 200, 204, 404, etc.).

**Validation & gestion d'erreurs**

- Les DTOs sont annotés (`@NotBlank`, `@NotNull`, `@Past`), et les contrôleurs acceptent `@Valid` sur les corps.
- `RestExceptionHandler` centralise la transformation des exceptions métiers en réponses HTTP structurées (400, 404, 500), et peut être étendu pour renvoyer des Problem Details (RFC 7807).

**Tests**

1) Tests unitaires
- Outils : JUnit 5, Mockito
- Couverture : services, controllers, event listeners, strategies.
- Exemples de tests :
	- `ClientServiceUnitTest` — validation des règles de création client et gestion des doublons.
	- `VehicleServiceUnitTest` — création/mise à jour de véhicule, publication d'événements.
	- `ContractServiceUnitTest` — création de contrat (interdit si véhicule en panne), mises à jour d'état.
	- `ContractEventListenerTest` — vérifie que le listener délègue au `StateTransitionService`.

2) Test d'intégration
- Type : test Spring Boot full context (`@SpringBootTest`) qui exécute la pile entière et touche le dépôt Mongo.
- Localisation : `src/test/java/com/location/location_voitures/api/integration/EmbeddedMongoIntegrationTest.java`.
- Ce test vérifie un scénario end-to-end : création client → création véhicule → création contrat → passage du véhicule à `EN_PANNE` → vérification que le contrat `EN_ATTENTE` est annulé.
- Configuration d'exécution : par défaut les tests d'intégration utilisent la propriété de test `spring.data.mongodb.uri=mongodb://localhost:27017/location-voitures-test`.

Remarques sur l'exécution des tests
- Pour exécuter tous les tests :
```powershell
.\mvnw.cmd test -DskipTests=false
```
- Pour exécuter uniquement le test d'intégration créé :
```powershell
.\mvnw.cmd -Dtest=EmbeddedMongoIntegrationTest test
```
- Avant d'exécuter le test d'intégration, assurez-vous qu'une instance MongoDB est disponible sur `localhost:27017`, ou adaptez la propriété `spring.data.mongodb.uri` (via `application-test.properties` ou la variable d'environnement `SPRING_DATA_MONGODB_URI`).

**Exemples de commandes utiles (PowerShell)**

Installer et compiler :
```powershell
.\mvnw.cmd clean install
```

Lancer uniquement les tests unitaires (par ex. avec un filtre) :
```powershell
.\mvnw.cmd -Dtest="*UnitTest" test
```

Lancer le test d'intégration spécifique :
```powershell
.\mvnw.cmd -Dtest=EmbeddedMongoIntegrationTest test
```

