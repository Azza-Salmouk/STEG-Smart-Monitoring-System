# Réalisations Techniques Valorisables - STEG Smart Monitoring System

## Vue d'ensemble

Ce document liste les **réalisations techniques vérifiées** dans le code, organisées par catégorie et accompagnées de formulations prêtes pour un CV.

---

## 1. DÉVELOPPEMENT FRONTEND (Vue.js 3 + Vite)

### 1.1 Architecture SPA avec authentification et RBAC

**Description technique :**
- Implémentation d'une Single Page Application complète avec Vue.js 3.3.4 et Vite 4.4.9
- Routeur Vue avec 18 routes protégées et authentification JWT
- Système de garde d'authentification global (`router.beforeEach()`) vérifiant le JWT et les rôles utilisateur
- Redirection intelligente selon le rôle : ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT
- Chaque route dispose d'une métadata `{ requiresAuth, roles }` pour l'autorisation

**Fichiers clés :**
- `src/router/index.js` : Routeur complet avec guard global
- `src/stores/authStore.js` : Gestion JWT et persistance de session

**Technologies :**
- Vue.js 3.3.4
- Vue Router 4.2.4
- JWT parsing et validation côté client

**Formulation CV :**
> Conçu et implémenté l'architecture d'une Single Page Application (SPA) sécurisée avec Vue.js 3, incluant un système complet de routeur protégé avec authentification JWT et contrôle d'accès par rôles (RBAC). Mise en place d'un garde global de navigation validant les permissions utilisateur et redirigeant dynamiquement selon les rôles.

### 1.2 State management avec Pinia (7 stores)

**Description technique :**
- Gestion d'état centralisée avec Pinia 2.1.6
- 7 stores distincts par domaine métier : auth, poste, transformateur, measurement, alert, intervention, complaint
- Chaque store implémente : state, getters (computed), actions (async/sync)
- Actions asynchrones appelant l'API backend avec gestion d'erreurs
- Mutations d'état implicites (réactivité automatique Pinia)

**Fichiers clés :**
- `src/stores/authStore.js` : Gestion JWT, utilisateur, rôle
- `src/stores/posteStore.js` : CRUD + recherche postes
- `src/stores/useAlertStore.js` : Gestion alertes avec reconnaissances/résolutions

**Technologies :**
- Pinia 2.1.6
- Axios 1.5.0 pour l'intégration backend

**Formulation CV :**
> Structuré l'état applicatif avec Pinia en 7 stores modulaires, séparant les domaines métier (authentification, postes, alertes, mesures, interventions, réclamations). Implémenté des actions asynchrones pour la synchronisation avec le backend et des getters (computed) pour la dérivation d'état.

### 1.3 Service API centralisé avec intercepteurs Axios

**Description technique :**
- Centralisateur unique `api.js` pour les 150+ appels API
- Utilisation d'Axios 1.5.0 avec configuration globale
- Intercepteurs : Injection JWT dans les headers, gestion d'erreurs uniforme
- Gestion des réponses d'erreur avec messages personnalisés
- Support pagination automatique

**Fichier clé :**
- `src/services/api.js` : Instance Axios configurée

**Technologies :**
- Axios 1.5.0
- Intercepteurs HTTP

**Formulation CV :**
> Développé un service API centralisé avec Axios exploitant les intercepteurs HTTP pour injecter dynamiquement les tokens JWT, gérer les erreurs de manière uniforme, et supporter la pagination automatique sur 150+ endpoints.

### 1.4 Composants réutilisables modulaires

**Description technique :**
- Architecture basée sur des composants Vue réutilisables
- Composants génériques : DataTable, Button, Toast, DeleteConfirmationModal, SuccessAlert
- Composants métier organisés par domaine : alert/, complaint/, intervention/, measurement/
- Props typées, événements émis pour la communication parent-enfant
- Exemple : AlertTable avec tri, filtrage, pagination ; AlertFormModal avec validation

**Fichiers clés :**
- `src/components/DataTable.vue` : Tableau générique paginé
- `src/components/alert/AlertTable.vue` : Tableau d'alertes avec actions
- `src/components/alert/AlertFormModal.vue` : Formulaire modale pour créer/éditer
- `src/components/measurement/HealthScoreGauge.vue` : Indicateur visuel de santé

**Technologies :**
- Vue.js 3 (Props, Emits, Slots)
- Tailwind CSS pour le styling
- Lucide Vue pour les icônes

**Formulation CV :**
> Conçu et implémenté une bibliothèque de composants Vue réutilisables modulaires, incluant des composants génériques (DataTable, Modal, Toast) et des composants métier spécialisés (AlertTable, InterventionFormModal, HealthScoreGauge) avec props typées et validation.

### 1.5 Visualisation de données avec Chart.js

**Description technique :**
- Intégration Chart.js 4.4.0 via vue-chartjs 5.3.0
- Graphiques dynamiques : Courbes (line), barres (bar), secteurs (pie)
- Actualisation en temps réel des données
- Utilisation dans les tableaux de bord (Admin, Supervisor)
- Exemple : Tendances des mesures, répartition des alertes par niveau

**Fichiers clés :**
- `src/views/ModernAdminDashboard.vue` : Graphiques admin
- `src/components/measurement/` : Chartes des mesures

**Technologies :**
- Chart.js 4.4.0
- vue-chartjs 5.3.0

**Formulation CV :**
> Intégré des graphiques interactifs avec Chart.js et vue-chartjs pour la visualisation dynamique de tendances (courbes, barres, secteurs) dans les tableaux de bord, permettant aux utilisateurs d'analyser les patterns de mesures et d'alertes.

### 1.6 Système de notifications (Toast) et gestion d'erreurs

**Description technique :**
- Composable `useToast()` pour les notifications système
- Toasts de succès, avertissement, erreur avec icônes
- Intégration globale dans les composants via composable
- Affichage automatique lors de requêtes API (succès/erreur)

**Fichiers clés :**
- `src/composables/useToast.js` : Logique de notifications
- `src/components/Toast.vue` : Composant d'affichage
- `src/services/error-handler.js` : Gestion d'erreurs

**Technologies :**
- Vue.js Composables
- CSS animations

**Formulation CV :**
> Développé un système de notifications utilisateur (Toasts) via composables Vue, affichant les réponses des opérations API (succès, erreur, validation) avec icônes visuelles et animations, améliorant le feedback utilisateur.

### 1.7 Interface responsive avec Tailwind CSS

**Description technique :**
- Utilisation de Tailwind CSS 3.3.3 pour le design responsive
- Layout en grille et flexbox
- Thème cohérent à travers l'application
- Composants layout : ModernSidebar (navigation), ModernNavbar (en-tête)
- Responsive design : Mobile-first, breakpoints pour tablette/desktop

**Fichiers clés :**
- `src/components/layout/ModernSidebar.vue` : Navigation latérale
- `src/components/layout/ModernNavbar.vue` : En-tête avec user menu
- Tous les composants : Styling Tailwind

**Technologies :**
- Tailwind CSS 3.3.3
- PostCSS 8.4.30 + Autoprefixer

**Formulation CV :**
> Conçu l'interface utilisateur responsive avec Tailwind CSS, implémentant un design cohérent, une navigation modulaire (sidebar/navbar) et un layout responsive compatible mobile, tablette et desktop.

---

## 2. DÉVELOPPEMENT BACKEND (Spring Boot 3 + Java 21)

### 2.1 Architecture REST API avec 70+ endpoints

**Description technique :**
- Implémentation complète d'une API REST avec Spring Boot 3.5.16
- 10 contrôleurs REST couvrant 8 modules métier
- Endpoints pour CRUD complet, recherche, filtrage, statistiques
- Codes HTTP appropriés : 200 OK, 201 Created, 204 No Content, 400 Bad Request, 401/403 Forbidden, 404 Not Found, 500 Server Error
- Support pagination avec `Pageable` Spring Data
- Exemple : PosteController avec 12+ endpoints

**Fichiers clés :**
- `src/main/java/com/steg/smartmonitoring/*/controller/` : Tous les contrôleurs
- `PosteController.java` : CRUD + recherche + import Excel
- `AlertController.java` : Gestion complète d'alertes

**Technologies :**
- Spring Boot 3.5.16
- Spring Web (REST)
- Spring Data JPA (Pageable, Sort)
- Java 21

**Formulation CV :**
> Implémenté une API REST RESTful avec Spring Boot couvrant 70+ endpoints, respectant les conventions HTTP (codes de statut appropriés, pagination, filtrage), organisée en 10 contrôleurs modulaires supportant les opérations CRUD, recherche avancée et statistiques d'agrégation.

### 2.2 Architecture en couches : Repository → Service → Controller

**Description technique :**
- Pattern de conception clairement appliqué
- **Couche Repository** : Interfaces étendant `JpaRepository<Entity, Long>` avec méthodes dérivées
- **Couche Service** : Interfaces définissant les opérations métier, implémentations avec logique
- **Couche Controller** : Endpoints REST appelant les services, validation des requêtes
- Isolation de la logique métier
- Testabilité améliorée (services séparent les préoccupations)

**Fichiers clés :**
- `*Repository.java` : Spring Data JPA
- `*Service.java` (interface) + `*ServiceImpl.java` (implémentation)
- `*Controller.java` : Endpoints REST

**Technologies :**
- Spring Data JPA
- Spring Web (REST)

**Formulation CV :**
> Structuré l'application backend selon le pattern en couches (Repository, Service, Controller), séparant les responsabilités de l'accès aux données, de la logique métier et de la présentation pour une meilleure maintenabilité et testabilité.

### 2.3 Gestion de base de données avec JPA/Hibernate

**Description technique :**
- Mappage entité-relationnel complet avec JPA
- 8 entités principales avec relations complexes
- Annotations JPA : `@Entity`, `@Table`, `@Id`, `@Column`, `@OneToMany`, `@ManyToOne`, `@ForeignKey`
- Énumérations persistées : `@Enumerated(EnumType.STRING)`
- Timestamps automatiques : `@CreationTimestamp`, `@UpdateTimestamp`
- Cascade delete et orphan removal configurés
- Support H2 (dev) et PostgreSQL (prod)

**Fichiers clés :**
- `poste/entity/Poste.java` : Entité avec relations
- `transformateur/entity/Transformateur.java` : Health score, risk level
- `alert/entity/Alert.java` : Entité complexe avec références

**Technologies :**
- Spring Data JPA
- Hibernate ORM 6.6.53
- H2 Database 2.3.232 (dev)
- PostgreSQL driver (prod)

**Formulation CV :**
> Modélisé et implémenté la couche persistence avec JPA/Hibernate, incluant 8 entités interconnectées, relations complexes (One-to-Many, Many-to-One), énumérations persistées et cascades appropriées, supportant à la fois H2 (développement) et PostgreSQL (production).

### 2.4 DTO Pattern avec validation

**Description technique :**
- Séparation Entity ↔ DTO pour sécurité et flexibilité
- Request DTOs : `Create*Request`, `Update*Request` avec validation
- Response DTOs : `*Response`, `*Dto` pour la sérialisation JSON
- Validation via `@Valid` et `@NotNull`, `@NotEmpty`, `@Size` sur les champs
- Mappers : Conversion Entity ↔ DTO

**Fichiers clés :**
- `*Request.java` : DTOs d'entrée avec validation
- `*Response.java` : DTOs de sortie
- `*Mapper.java` : Conversion bidirectionnelle
- Exemple : `CreatePosteRequest`, `PosteResponse`

**Technologies :**
- Spring Validation (`@Valid`, `@NotNull`)
- Jackson (sérialisation JSON)

**Formulation CV :**
> Implémenté le pattern DTO pour isoler les entités de la couche présentation, avec DTOs d'entrée validés (`@Valid`, `@NotNull`, `@Size`) et DTOs de sortie pour la sérialisation JSON, améliorant la sécurité API et la flexibilité d'évolution.

### 2.5 Sécurité avec JWT et Spring Security

**Description technique :**
- Implémentation JWT complète (voir détail dans l'audit technique)
- Authentification sans sessions (stateless)
- Filtre JWT custom : `JwtAuthenticationFilter`
- Authorization avec `@PreAuthorize` sur les endpoints
- 4 rôles avec permissions granulaires
- Configuration CORS pour le frontend

**Fichiers clés :**
- `auth/jwt/JwtService.java` : Génération/validation JWT
- `config/SecurityConfig.java` : Configuration complète
- `auth/filter/JwtAuthenticationFilter.java` : Filtre JWT
- Annotations `@PreAuthorize` partout

**Technologies :**
- Spring Security 3.5.16
- jjwt 0.12.7 (JWT)
- BCrypt (hachage)

**Formulation CV :**
> Sécurisé l'application avec JWT (HS256) et Spring Security, implémentant une architecture stateless avec filtre JWT personnalisé, contrôle d'accès granulaire par rôles (RBAC) via `@PreAuthorize`, et authentification BCrypt, garantissant la protection des endpoints sensibles.

### 2.6 Recherche et filtrage avancés

**Description technique :**
- Endpoints de recherche multiples par paramètres
- Exemple : Postes par nom, région, état, type, recherche multi-critères
- Support pagination avec `Page<Entity>`
- Filtrage JPQL ou critères Hibernate
- Endpoints distincts pour chaque type de recherche ou endpoint unique `/search/advanced` polymorphe

**Fichiers clés :**
- `*Controller.java` : Routes `/search/*`
- `*Repository.java` : Méthodes dérivées Spring Data
- `*ServiceImpl.java` : Logique de filtrage

**Technologies :**
- Spring Data JPA (Pageable, Page)
- JPQL

**Formulation CV :**
> Implémenté des systèmes de recherche et filtrage avancés avec pagination, permettant aux utilisateurs de filtrer par multiples critères (nom, région, état, statut), avec retour paginé optimisé pour les listes voluminuses.

### 2.7 Import/Export Excel

**Description technique :**
- Utilisation Apache POI 5.2.5 pour la manipulation de fichiers Excel
- Importateurs spécialisés : `PosteExcelImporter`, `TransformateurExcelImporter`
- Classe générique : `ExcelImportResult` avec résultats et erreurs
- Parsing de fichiers `.xlsx` avec validation de colonnes
- Génération de rapport d'importation (réussites, erreurs)

**Fichiers clés :**
- `poste/excel/PosteExcelImporter.java`
- `transformateur/excel/TransformateurExcelImporter.java`
- `common/excel/ExcelImportResult.java`
- Endpoint : `POST /api/postes/import` (multipart form-data)

**Technologies :**
- Apache POI 5.2.5

**Formulation CV :**
> Développé des mécanismes d'import/export Excel pour les postes et transformateurs utilisant Apache POI, permettant aux administrateurs d'importer massivement des données avec validation et génération de rapports détaillés.

### 2.8 Statistiques et agrégations

**Description technique :**
- Endpoints de statistiques par module
- DTOs spécialisés : `PosteStatisticsResponse`, `AlertStatisticsResponse`, `InterventionStatisticsResponse`
- Agrégations : Comptages, répartitions par catégorie, tendances
- Exemple : Nombre d'alertes par niveau, transformateurs par état, interventions par priorité
- Utilisation de requêtes JPA avec GROUP BY/COUNT

**Fichiers clés :**
- `*Service.java` : Méthodes `getStatistics()`
- `*StatisticsResponse.java` : DTOs de réponse
- `*Controller.java` : Endpoints `/statistics`

**Technologies :**
- Spring Data JPA
- JPQL

**Formulation CV :**
> Conçu des endpoints de statistiques et d'agrégation pour chaque module, fournissant des données analytiques (comptages, répartitions par catégorie, tendances) utilisées pour alimenter les tableaux de bord exécutifs.

### 2.9 Gestion d'erreurs centralisée

**Description technique :**
- Exception handler global : `GlobalExceptionHandler.java`
- Annotation `@RestControllerAdvice`
- Capture de 8 types d'exceptions avec HTTP status approprié
- Réponse d'erreur structurée : `ApiErrorResponse` (status, message, error, path, timestamp)
- Handling des exceptions métier et framework

**Fichiers clés :**
- `common/exception/GlobalExceptionHandler.java`
- `common/exception/ApiErrorResponse.java`
- Exceptions métier : `ResourceNotFoundException`, `ResourceAlreadyExistsException`, `JwtException`

**Technologies :**
- Spring Web (`@RestControllerAdvice`, `@ExceptionHandler`)

**Formulation CV :**
> Implémenté un gestionnaire d'erreurs centralisé avec Spring `@RestControllerAdvice`, captant et transformant les exceptions en réponses JSON structurées avec HTTP status appropriés, améliorant la robustesse et l'expérience client.

### 2.10 Initialisation de données de démo

**Description technique :**
- Classe `DataInitializer.java` : Création d'utilisateur admin par défaut
- Classe `DemoDataGenerator.java` : Génération automatique de données de test
- Données générées : 20 postes, 149 transformateurs, 500+ mesures, 300 alertes, 120 interventions, 150 réclamations
- Exécutée au démarrage via `@PostConstruct`

**Fichiers clés :**
- `initializer/DataInitializer.java`
- `initializer/DemoDataGenerator.java`

**Technologies :**
- Spring `@PostConstruct`

**Formulation CV :**
> Implémenté des initialiseurs de données pour la génération automatique de jeu de données de démonstration au démarrage, facilitant les tests et les présentations sans besoin d'import manuel.

---

## 3. MODÉLISATION ET GESTION DES DONNÉES

### 3.1 Design de schéma entité-relationnel

**Description technique :**
- Modélisation complète : 8 entités, 14 énumérations
- Relations clairement définies : 1-N (Poste-Transformateur), N-1 (Transformateur-Poste)
- Contraintes d'intégrité : Unicité (code poste, reference transformateur), Non-nullable
- Timestamps : Auditing automatique (createdAt, updatedAt)
- Normalized design : Pas de redondance

**Fichiers clés :**
- `*/entity/*.java` : 8 entités JPA

**Technologies :**
- JPA/Hibernate
- Annotations `@Entity`, `@Column`, `@Unique`, `@NotNull`

**Formulation CV :**
> Conçu le schéma relationnel de la base de données avec 8 entités et relations complexes, appliquant les principes de normalisation, d'intégrité référentielle et d'auditing automatique pour une gestion fiable des données.

### 3.2 Health score et indicateurs de santé

**Description technique :**
- Calcul dynamique du health score (0-100) pour les transformateurs
- Agrégation des mesures IoT : température, tension, courant, etc.
- Risk level automatique : LOW, MEDIUM, HIGH, CRITICAL basé sur health score
- Classe `HealthScoreCalculator.java` : Logique de calcul
- Mise à jour automatique lors de chaque nouvelle mesure

**Fichiers clés :**
- `measurement/calculator/HealthScoreCalculator.java`
- `transformateur/entity/Transformateur.java` : Champs healthScore, riskLevel
- `measurement/analyzer/MeasurementAnalyzer.java` : Analyse des mesures

**Technologies :**
- Logique métier Java

**Formulation CV :**
> Développé un système de scoring automatique (health score 0-100) pour les transformateurs basé sur l'agrégation des mesures IoT, dérivant des niveaux de risque (LOW/MEDIUM/HIGH/CRITICAL) pour l'aide à la décision opérationnelle.

### 3.3 Support dual-database (H2 + PostgreSQL)

**Description technique :**
- Configuration développement : H2 en mémoire (`application.properties`)
- Configuration production : PostgreSQL (`application-prod.properties`)
- Spring Data JPA abstracts les différences de dialecte SQL
- Migrations : Hibernate `ddl-auto=create-drop` (dev), `validate` (prod)
- Sans changement de code source

**Fichiers clés :**
- `src/main/resources/application.properties`
- `src/main/resources/application-prod.properties`

**Technologies :**
- Spring Data JPA (dialecte abstrait)
- H2 + PostgreSQL

**Formulation CV :**
> Configuré le support dual-database (H2 pour développement, PostgreSQL pour production) via Spring profiles, permettant le développement rapide sans infrastructure lourde et le déploiement sécurisé en production.

---

## 4. SÉCURITÉ APPLICATIVE ET GESTION DES RÔLES

### 4.1 Authentification JWT stateless

**Description technique :**
- JWT HS256, 24h d'expiration
- Pas de sessions serveur (stateless)
- Token extrait du header `Authorization: Bearer <token>`
- Validation signature + expiration
- Claim `subject` = email utilisateur

**Fichiers clés :**
- `auth/jwt/JwtService.java` : Toute la logique JWT
- `auth/filter/JwtAuthenticationFilter.java` : Extraction/validation

**Technologies :**
- jjwt 0.12.7

**Formulation CV :**
> Implémenté l'authentification JWT stateless (HS256, 24h) remplaçant les sessions traditionnelles, réduisant la charge serveur et permettant la scalabilité horizontale pour les déploiements cloud.

### 4.2 Contrôle d'accès basé sur les rôles (RBAC)

**Description technique :**
- 4 rôles distincts : ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT
- Annotations `@PreAuthorize` pour la protection des endpoints
- Patterns : `hasRole()`, `hasAnyRole()`, `isAuthenticated()`, `permitAll()`
- Exemples :
  - `@PreAuthorize("hasRole('ADMIN')")` : GET /api/users
  - `@PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")` : PUT /api/postes/{id}
  - `@PreAuthorize("isAuthenticated()")` : Tous les endpoints sensibles

**Fichiers clés :**
- `config/SecurityConfig.java` : Configuration RBAC
- `*/controller/*.java` : Annotations sur chaque endpoint
- `user/entity/User.java` : Énumération Role

**Technologies :**
- Spring Security `@PreAuthorize`

**Formulation CV :**
> Sécurisé les endpoints avec un contrôle d'accès granulaire basé sur 4 rôles (ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT) utilisant les annotations Spring Security `@PreAuthorize`, garantissant que chaque utilisateur n'accède qu'aux ressources autorisées.

### 4.3 Hachage BCrypt des mots de passe

**Description technique :**
- Algorithme BCrypt avec 10 rounds
- Hachage automatique lors de la création d'utilisateur
- Validation automatique lors de l'authentification
- Impossibilité de récupérer le mot de passe original

**Fichiers clés :**
- `user/service/impl/UserServiceImpl.java` : Hachage
- `config/SecurityConfig.java` : PasswordEncoder configuration

**Technologies :**
- Spring Security BCryptPasswordEncoder

**Formulation CV :**
> Sécurisé les mots de passe utilisateur avec BCrypt (10 rounds), garantissant qu'aucun mot de passe n'est stocké en clair et que les données sensibles sont protégées même en cas de compromission de la base de données.

### 4.4 CORS configuré pour le frontend

**Description technique :**
- Whitelist des origins : localhost:3000, 4200, 5173, 5174, 5175
- Méthodes autorisées : GET, POST, PUT, DELETE, PATCH, OPTIONS
- Headers autorisés : * (tous)
- Credentials : true (cookies/auth headers autorisés)
- Max-Age : 3600 secondes

**Fichiers clés :**
- `config/SecurityConfig.java` : `corsConfigurationSource()`

**Technologies :**
- Spring Security CORS

**Formulation CV :**
> Configuré CORS correctement limitant l'accès à des origins spécifiques (localhost dev ports) tout en permettant les requêtes cross-origin légitimes du frontend, renforçant la sécurité sans bloquer le développement.

---

## 5. VISUALISATION DE DONNÉES ET TABLEAUX DE BORD

### 5.1 Tableaux de bord RBAC personnalisés

**Description technique :**
- 4 dashboards distincts selon le rôle
- Admin : Vue complète du système
- Supervisor : Données supervisées
- Agent : Données personnelles
- Service Client : Réclamations
- Chaque dashboard appelle `/api/dashboard/{role}` spécifique

**Fichiers clés :**
- `src/views/ModernAdminDashboard.vue`
- `src/views/SupervisorDashboard.vue`
- `src/views/AgentDashboard.vue`
- `src/views/ServiceClientDashboard.vue`

**Technologies :**
- Vue.js 3
- Chart.js
- Spring Boot dashboard endpoints

**Formulation CV :**
> Conçu et implémenté 4 tableaux de bord personnalisés par rôle (ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT), chacun affichant les données pertinentes et les indicateurs critiques, améliorant la visibilité opérationnelle selon le contexte utilisateur.

### 5.2 Graphiques dynamiques avec Chart.js

**Description technique :**
- Graphiques interactifs : Courbes (ligne), barres, secteurs
- Données chargées dynamiquement depuis le backend
- Mise à jour en temps réel
- Légendes, labels, tooltips

**Fichiers clés :**
- Dashboard vues avec `<canvas>` et composants Chart.js

**Technologies :**
- Chart.js 4.4.0
- vue-chartjs 5.3.0

**Formulation CV :**
> Intégré des visualisations de données interactives avec Chart.js, permettant aux utilisateurs d'analyser graphiquement les tendances de mesures, répartitions d'alertes et progression d'interventions pour une meilleure prise de décision.

### 5.3 Tableaux de données avec pagination

**Description technique :**
- Composant DataTable générique
- Pagination côté serveur
- Tri sur les colonnes
- Filtrage intégré
- Actions inline : Voir, Éditer, Supprimer

**Fichiers clés :**
- `src/components/DataTable.vue` : Composant générique
- Pages utilisant DataTable : AlertsManagement, MeasurementsManagement, etc.

**Technologies :**
- Vue.js 3
- Tailwind CSS

**Formulation CV :**
> Implémenté des tableaux de données sophistiqués avec pagination serveur, tri, filtrage et actions inline, facilitant la navigation et l'exploration de larges ensembles de données pour les utilisateurs.

---

## 6. INTÉGRATION D'INTELLIGENCE ARTIFICIELLE

### 6.1 Chatbot avec Ollama et Qwen2.5:3b

**Description technique :**
- Intégration du modèle Qwen2.5:3b (3 milliards de paramètres)
- API Ollama `http://localhost:11434`
- Endpoint `/api/ai/chatbot/message` pour l'interaction
- Réponses en français via prompt système
- Contexte utilisateur injecté automatiquement

**Fichiers clés :**
- `ai/controller/ChatbotController.java` : Endpoint
- `ai/service/ChatbotService.java` : Logique
- `ai/service/OllamaService.java` : Communication Ollama
- Frontend : `src/views/ChatbotInterface.vue`

**Technologies :**
- Ollama (model serving)
- Qwen2.5:3b (LLM)
- HTTP/REST API

**Formulation CV :**
> Intégré un assistant conversationnel basé sur Ollama et le modèle Qwen2.5:3b, permettant aux utilisateurs d'interagir en langage naturel (français) avec l'application pour obtenir des insights sur les données métier.

### 6.2 Construction de contexte RBAC pour l'IA

**Description technique :**
- Le ChatbotService construit dynamiquement un prompt système
- Contexte métier adapté au rôle de l'utilisateur
- ADMIN : Données complètes du système
- SUPERVISOR : Données supervisées
- AGENT : Données personnelles
- SERVICE_CLIENT : Réclamations
- Confidentialité garantie par filtrage du contexte

**Fichiers clés :**
- `ai/service/ChatbotService.java` : Méthode `buildContextForRole(role)`

**Technologies :**
- Java (construction de prompts)
- RBAC backend

**Formulation CV :**
> Conçu un système de contexte intelligent pour l'assistant IA, construisant dynamiquement les prompts selon le rôle utilisateur, garantissant que chaque utilisateur ne reçoit que les insights pertinents et autorisés (RBAC appliqué à l'IA).

### 6.3 Gestion d'erreurs et disponibilité du service IA

**Description technique :**
- Endpoint de health check : `GET /api/ai/chatbot/health`
- Endpoint de diagnostic : `GET /api/ai/chatbot/debug/ollama-status`
- Gestion des timeouts (120s)
- Réponses d'erreur structurées
- Messages informatifs si Ollama indisponible

**Fichiers clés :**
- `ai/controller/ChatbotController.java` : Endpoints de diagnostic
- `ai/service/ChatbotService.java` : Gestion d'erreurs

**Technologies :**
- Spring Web Exception Handling

**Formulation CV :**
> Implémenté des mécanismes de health check et de gestion d'erreurs robustes pour le service IA, incluant des endpoints de diagnostic et des timeouts configurés, garantissant la résilience de l'application en cas d'indisponibilité d'Ollama.

---

## 7. TESTS, DÉBOGAGE ET INTÉGRATION

### 7.1 Compilation et packaging Maven

**Description technique :**
- Build Maven complète : `mvn clean package -DskipTests`
- Génération d'exécutable JAR Spring Boot
- Fichier résultant : `smart-monitoring-backend-0.0.1-SNAPSHOT.jar`
- Déploiement standalone

**Fichiers clés :**
- `pom.xml`

**Technologies :**
- Maven 3.x
- Spring Boot packaging

**Formulation CV :**
> Configuré le build Maven complet avec Spring Boot, produisant un JAR exécutable standalone permettant le déploiement simplifié en différents environnements sans serveur d'application.

### 7.2 Hot reload frontend (Vite + HMR)

**Description technique :**
- Vite avec Hot Module Replacement (HMR)
- Développement rapide avec rafraîchissement instantané
- Server Vite sur localhost:5173
- Aucun rebuild nécessaire pour les modifications CSS/JS

**Fichiers clés :**
- `package.json` : Script `npm run dev`
- Vite config (implicite)

**Technologies :**
- Vite 4.4.9
- Vue.js 3 HMR

**Formulation CV :**
> Configuré Vite avec Hot Module Replacement pour le développement frontend, réduisant le temps de feedback lors des modifications de code (rafraîchissement instantané sans rebuild).

### 7.3 Persistance de session frontend

**Description technique :**
- Stockage JWT dans localStorage
- Restauration de session au rechargement de la page
- `authStore.restoreSession()` appelé au démarrage
- Persistent login (utilisateur reste connecté après rafraîchissement)

**Fichiers clés :**
- `src/stores/authStore.js` : Méthodes `setToken()`, `restoreSession()`

**Technologies :**
- localStorage API
- Vue.js lifecycle

**Formulation CV :**
> Implémenté la persistance de session utilisateur avec localStorage, permettant aux utilisateurs de rester connectés après un rechargement de page, améliorant l'expérience utilisateur.

---

## Résumé des réalisations par force

| Catégorie | Réalisations clés |
|-----------|------------------|
| **Frontend** | SPA Vue.js sécurisée, Pinia 7 stores, API centralisée, composants réutilisables, Chart.js, Tailwind |
| **Backend** | API 70+ endpoints, architecture 3 couches, JWT+RBAC, JPA/Hibernate, statistiques, Excel import |
| **Données** | 8 entités complexes, health scoring, dual-database (H2+PostgreSQL), design normalisé |
| **Sécurité** | JWT HS256, BCrypt, RBAC 4 rôles, CORS, exception handling centralisé |
| **Dashboards** | 4 dashboards RBAC, Chart.js, tableaux paginés, indicateurs |
| **IA** | Ollama intégré, Qwen2.5:3b, contexte RBAC, gestion d'erreurs |
| **DevOps** | Maven build, Vite HMR, H2+PostgreSQL, profiles Spring |

---

## Formulations prêtes pour CV

### Format concis (1-2 lignes)

> Développé une application web full-stack (Vue.js 3 + Spring Boot 3) de supervision électrique avec 70+ endpoints REST, RBAC 4 rôles, JWT, et assistant IA intégré (Ollama).

### Format détaillé (3-5 puces)

- Conçu et implémenté l'architecture complète d'une SPA Vue.js 3 sécurisée avec routeur protégé, Pinia (7 stores) et API centralisée Axios.
- Développé une API REST RESTful avec Spring Boot 3 (70+ endpoints), architecture 3 couches, RBAC fine-grained et JWT stateless.
- Sécurisé l'application avec authentification JWT (HS256), hachage BCrypt, contrôle d'accès par 4 rôles et CORS configuré.
- Intégré un modèle de langage Ollama (Qwen2.5:3b) pour un assistant conversationnel intelligent avec contexte utilisateur filtré par RBAC.
- Implémenté des tableaux de bord personnalisés par rôle avec visualisations Chart.js, pagination serveur et statistiques d'agrégation.

