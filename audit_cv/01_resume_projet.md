# STEG Smart Monitoring System - Résumé du Projet

## Vue d'ensemble

**STEG Smart Monitoring System** est une application web complète de supervision et de monitoring d'infrastructure électrique. Le système assure le suivi en temps réel de postes électriques et de transformateurs, détecte les anomalies via des alertes automatiques, gère les interventions techniques et les réclamations clients. Un assistant conversationnel basé sur l'IA (Ollama) fournit des insights intelligents selon le rôle de l'utilisateur.

**Objectif métier :** Automatiser la supervision d'une infrastructure électrique distribuée, réduire les temps de réaction aux pannes, optimiser la gestion des ressources d'intervention, et fournir un service client amélioré.

---

## Architecture générale

### Séparation frontend/backend

```
projet-steg/
├── smart-monitoring-backend/        → Spring Boot 3.5.16 (Java 21)
│   └── smart-monitoring-backend/
│       └── src/main/java/com/steg/smartmonitoring/
├── smart-monitoring-frontend/       → Vue.js 3.3.4 + Vite 4.4.9
│   └── src/
```

### Stack technique

| Couche | Technologies |
|-------|--------------|
| **Frontend** | Vue.js 3.3.4, Vite 4.4.9, Pinia 2.1.6, Axios 1.5.0, Tailwind CSS 3.3.3, Chart.js 4.4.0 |
| **Backend** | Spring Boot 3.5.16, Java 21, Spring Data JPA, Spring Security, Maven |
| **Base de données** | H2 (développement/tests), PostgreSQL (production) |
| **Authentification** | JWT (HS256, 24h d'expiration), BCrypt (hachage des mots de passe) |
| **IA** | Ollama (localhost:11434), modèle Qwen2.5:3b |
| **Import/Export** | Apache POI 5.2.5 (Excel) |

---

## Architecture générale du backend

### Patterns utilisés

- **Repository Pattern** : `JpaRepository` pour l'accès aux données
- **Service Pattern** : Couche métier séparant la logique des contrôleurs
- **DTO Pattern** : Transfert de données entre frontend et backend
- **Entity Pattern** : Mapping automatique JPA avec Hibernate
- **Security Pattern** : JWT + Spring Security avec annotations `@PreAuthorize`

### Modules métier principaux

1. **Authentication** : Gestion des utilisateurs, génération JWT, BCrypt
2. **Postes** : CRUD complet avec statistics, import Excel
3. **Transformateurs** : CRUD avec health score (0-100) et risk level
4. **Measurements** : Données IoT (15+ paramètres), health score aggregation
5. **Alerts** : Détection automatique, statut (ACTIVE, ACKNOWLEDGED, RESOLVED)
6. **Interventions** : Gestion des interventions techniques avec priorités
7. **Complaints** : Gestion des réclamations clients
8. **Dashboard** : Endpoints distincts par rôle (ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT)
9. **Chatbot AI** : Intégration Ollama avec contexte RBAC

### Base de données

**8 entités JPA principales :**
- User
- Poste
- Transformateur
- Measurement
- Alert
- Intervention
- Complaint
- (Entités de support pour IA/prediction)

**Relations :**
- Poste (1) ← → (N) Transformateur
- Transformateur (1) ← → (N) Measurement
- Transformateur (1) ← → (N) Alert
- Transformateur (1) ← → (N) Intervention
- Poste (1) ← → (N) Complaint

---

## Architecture générale du frontend

### Routeur Vue.js

**18 routes configurées** avec authentification et autorisation par rôle :

```
Routes publiques :
  /login              → LoginView
  /unauthorized       → UnauthorizedView
  /:pathMatch(.*)     → NotFoundView

Routes protégées (authentification requise) :
  /admin/dashboard              → ADMIN uniquement
  /supervisor/dashboard         → SUPERVISOR uniquement
  /agent/dashboard              → AGENT uniquement
  /serviceclient/dashboard      → SERVICE_CLIENT uniquement
  /users                        → ADMIN, SUPERVISOR
  /postes                       → ADMIN, SUPERVISOR
  /transformateurs              → ADMIN, SUPERVISOR, AGENT
  /measurements                 → ADMIN, SUPERVISOR, AGENT
  /interventions                → ADMIN, SUPERVISOR, AGENT
  /alerts                       → ADMIN, SUPERVISOR, AGENT
  /complaints                   → ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT
  /chatbot                      → ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT
  /settings                     → Tous les rôles authentifiés
  /profile                      → Tous les rôles authentifiés
```

**Guard global** : Redirection automatique en cas de rôle insuffisant ou d'expiration JWT

### State Management (Pinia)

**7 stores d'état centralisé :**
1. `authStore` - Token JWT, utilisateur connecté, rôle
2. `posteStore` - Gestion des postes
3. `transformateurStore` - Gestion des transformateurs
4. `useMeasurementStore` - Gestion des mesures
5. `useAlertStore` - Gestion des alertes
6. `useInterventionStore` - Gestion des interventions
7. `useComplaintStore` - Gestion des réclamations

### Composants réutilisables

Structure modulaire par domaine métier :
- `components/alert/` - AlertTable, AlertFormModal, AlertDetailsModal
- `components/complaint/` - ComplaintTable, ComplaintFormModal, ComplaintBadge
- `components/intervention/` - InterventionTable, InterventionFormModal
- `components/measurement/` - MeasurementTable, HealthScoreGauge, ParameterIndicator
- `components/layout/` - ModernSidebar, ModernNavbar
- `composables/` - useToast (notifications système)

---

## Principales fonctionnalités implémentées

### 1. Authentification et contrôle d'accès

- **Login** : Email/mot de passe → JWT 24h
- **RBAC** : 4 rôles avec permissions distinctes
- **Endpoints protégés** : Vérification JWT à chaque requête
- **Logout** : Suppression du token côté client

### 2. Gestion des postes électriques

- **CRUD complet** : Créer, lire, modifier, supprimer
- **Recherche avancée** : Par nom, région, état, type
- **Import Excel** : Importation massive de postes
- **Statistiques** : Nombre de postes, répartition par état/région

### 3. Gestion des transformateurs

- **CRUD complet** : Avec calcul automatique du health score et risk level
- **Health score** : Agrégation des mesures (0-100)
- **Risk levels** : LOW, MEDIUM, HIGH, CRITICAL
- **Statistiques** : Par état, criticalité
- **Alertes visuelles** : Transformateurs en défaut mis en évidence

### 4. Mesures IoT

- **15+ paramètres capturés** : Température, tension, courant, puissance, fréquence, humidité, health score, etc.
- **Statut de mesure** : NORMAL, WARNING, CRITICAL, OFFLINE
- **Historique** : Consultation des mesures passées par transformateur
- **Simulation** : Génération de données de démo pour tests
- **Graphiques** : Visualisation des tendances avec Chart.js

### 5. Système d'alertes

- **Types d'alertes** : TEMPERATURE, SURCHARGE, SURCONSOMMATION, PANNE, CHUTE_TENSION, etc.
- **Niveaux** : INFO, WARNING, CRITICAL, DANGER
- **Statut** : ACTIVE, ACKNOWLEDGED (reconnaître l'alerte), RESOLVED (résoudre)
- **Gestion** : Créer, modifier, reconnaître, résoudre, archiver
- **Filtrage** : Par type, niveau, statut, date, transformateur

### 6. Gestion des interventions

- **Création** : Automatique ou manuelle en réaction aux alertes
- **Assignation** : Aux agents techniques
- **Priorités** : LOW, MEDIUM, HIGH, URGENT
- **Statuts** : PLANIFIEE, EN_COURS, COMPLETED, CANCELLED
- **Suivi** : Date de création, dates prévue/réelle, durée, coûts

### 7. Gestion des réclamations clients

- **Création** : Les clients (SERVICE_CLIENT) créent des réclamations
- **Catégories** : COUPURE, SURCHARGE, TENSION_BASSE, TENSION_HAUTE, BRUIT, FACTURE, AUTRE
- **Assignation** : Aux superviseurs pour gestion
- **Priorités** : LOW, MEDIUM, HIGH
- **Statuts** : NOUVELLE, EN_ANALYSE, ASSIGNEE, EN_TRAITEMENT, RESOLUE, FERMEE
- **Suivi** : Progression de la réclamation

### 8. Tableaux de bord RBAC

Chaque rôle dispose d'un dashboard personnalisé :

- **ADMIN** : Vue complète du système, toutes les statistiques
- **SUPERVISOR** : Interventions/réclamations assignées, équipe managée
- **AGENT** : Interventions personnelles, alertes pertinentes
- **SERVICE_CLIENT** : Statut des réclamations, contact supervisor

### 9. Assistant conversationnel IA

- **Interface** : Chatbot en français
- **Contexte intelligent** : Données métier selon le rôle utilisateur
- **Modèle** : Ollama Qwen2.5:3b (3B paramètres)
- **Sécurité** : RBAC appliqué au contexte transmis au modèle

---

## Opérations disponibles par module

### Module Postes
- `GET /api/postes` - Lister (paginé)
- `GET /api/postes/{id}` - Détail
- `POST /api/postes` - Créer (ADMIN)
- `PUT /api/postes/{id}` - Modifier (ADMIN, SUPERVISOR)
- `DELETE /api/postes/{id}` - Supprimer (ADMIN)
- `GET /api/postes/statistics` - Statistiques
- `POST /api/postes/import` - Import Excel (ADMIN, SUPERVISOR)
- Recherches avancées par nom, région, état, type

### Module Transformateurs
- Opérations identiques aux postes
- `GET /api/transformateurs/critical` - Transformateurs critiques
- Calcul automatique du health score et risk level

### Module Mesures
- `GET /api/measurements` - Lister (paginé)
- `GET /api/measurements/{id}` - Détail
- `POST /api/measurements` - Créer
- `GET /api/measurements/by-transformateur/{id}` - Historique
- `GET /api/measurements/latest` - Dernières mesures
- `GET /api/measurements/critical` - Mesures critiques
- `GET /api/measurements/statistics` - Statistiques

### Module Alertes
- CRUD complet
- `POST /api/alerts/{id}/acknowledge` - Reconnaître
- `POST /api/alerts/{id}/resolve/{userId}` - Résoudre
- Multiples filtres de recherche (statut, niveau, type, date)

### Module Interventions
- CRUD complet
- `GET /api/interventions/by-agent/{agentId}` - Par agent
- `GET /api/interventions/by-transformateur/{transId}` - Par transformateur
- Filtrage par statut, priorité, date

### Module Réclamations
- CRUD complet
- `GET /api/complaints/by-supervisor/{supervisorId}` - Par supervisor
- `GET /api/complaints/by-poste/{posteId}` - Par poste
- Filtrage par catégorie, priorité, statut

### Module Utilisateurs
- `GET /api/users` - Lister (ADMIN, SUPERVISOR)
- `POST /api/users` - Créer (ADMIN)
- `PUT /api/users/{id}` - Modifier (ADMIN, SUPERVISOR)
- `DELETE /api/users/{id}` - Supprimer (ADMIN)

### Module Authentification
- `POST /api/auth/login` - Connexion (public)

### Module ChatBot IA
- `POST /api/ai/chatbot/message` - Envoyer message (authentification requise)
- `GET /api/ai/chatbot/health` - Vérifier le service (public)
- `GET /api/ai/chatbot/debug/ollama-status` - Statut Ollama (authentification requise)

---

## Configuration d'environnement

### Backend
- **Port** : 8081
- **DB (dev)** : H2 en mémoire
- **DB (prod)** : PostgreSQL localhost:5432
- **JWT secret** : Variable d'environnement `JWT_SECRET`
- **JWT expiration** : 24 heures (configurable via `JWT_EXPIRATION`)

### Frontend
- **Port (dev)** : 5173
- **Backend API** : http://localhost:8081

### IA
- **Service** : Ollama localhost:11434
- **Modèle** : qwen2.5:3b
- **Timeout** : 120 secondes

---

## Déploiement et compilation

### Backend
```bash
# Compilation
mvn clean package -DskipTests

# Exécution
java -jar target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
# Installation des dépendances
npm install

# Développement
npm run dev

# Production
npm run build
```

---

## État actuel du projet

✅ **Compilation** : Succès (Maven, Spring Boot 3.5.16)
✅ **Déploiement** : En développement/test
✅ **Base de données** : H2 en mémoire pour tests, PostgreSQL ready
✅ **Données de démo** : Générées automatiquement au démarrage
✅ **CORS configuré** : localhost:3000, 4200, 5173, 5174, 5175
✅ **Architecture stable** : Finalisation confirmée dans la documentation

---

## Documents de référence

- Backend: `smart-monitoring-backend/smart-monitoring-backend/pom.xml`
- Frontend: `smart-monitoring-frontend/package.json`
- Router: `smart-monitoring-frontend/src/router/index.js`
- Stores: `smart-monitoring-frontend/src/stores/`
- Security Config: `smart-monitoring-backend/.../config/SecurityConfig.java`
- JWT Service: `smart-monitoring-backend/.../auth/jwt/JwtService.java`
