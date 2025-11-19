📘 location-voitures — Application de gestion de locations automobiles
🚗 Description

Ce projet est une application de gestion de location de véhicules, développée avec Spring Boot et MongoDB.
Il permet de gérer :

Les clients

Les véhicules

Les contrats de location

Les règles métier complexes (pannes, retards, annulations automatiques, etc.)

Ce projet a été réalisé dans le cadre d’un TP visant à concevoir une architecture claire, modulaire et extensible, en respectant de bonnes pratiques de développement.

🏗️ Technologies utilisées

Java 17+

Spring Boot 3

Spring Data MongoDB

MongoDB / MongoDB Atlas

Lombok

Maven

(Optionnel) Docker / Docker Compose

📦 Architecture du projet

Le projet suit une architecture inspirée du Clean Architecture, organisée en plusieurs couches :

src/main/java
└── com.location.location_voitures
    ├── api
    │   ├── controller     → Endpoints REST
    │   ├── dto            → Objets de transfert API
    │   └── mapper         → Conversion Entity ↔ DTO
    │
    ├── model              → Entités métiers (Mongo @Document)
    │
    ├── service            → Logique métier (règles / validations)
    │
    └── repository         → Requêtes MongoDB

📂 Fonctionnalités
👤 Gestion des clients

Création d’un client

Mise à jour

Suppression

Récupération d’un client ou de tous les clients
Règles :

Un client est unique par : nom + prénom + date de naissance

Le numéro de permis doit être unique

🚘 Gestion des véhicules (à implémenter / en cours)

États : DISPONIBLE, EN_LOCATION, EN_PANNE

Impossible de louer un véhicule en panne

Un véhicule ne peut être loué que par un client à la fois

📄 Gestion des contrats (à implémenter / en cours)

Création / Gestion du statut

Mise en retard automatique

Annulation si :

véhicule en panne avant le début du contrat

retard bloquant un autre contrat

▶️ Lancer l’application
🔧 Prérequis

JDK 17+

Maven

MongoDB (local ou Atlas)

🚀 Démarrer l’application localement
1️⃣ Installer les dépendances et compiler :
mvn clean install

2️⃣ Lancer l’application :
mvn spring-boot:run


➡️ L’API démarre sur :
http://localhost:8080

🗄️ Configuration MongoDB

Dans application.properties :

spring.data.mongodb.uri=mongodb://localhost:27017/location


Pour MongoDB Atlas :

spring.data.mongodb.uri=mongodb+srv://<user>:<password>@<cluster>/location