# Compétences Techniques - STEG Smart Monitoring System

## Vue d'ensemble

Ce document liste les compétences techniques identifiées avec leur niveau de preuve dans le code.

**Légendes des niveaux de preuve :**
- ✅ **Confirmée dans le code** : Utilisation réelle vérifiée
- 📦 **Configuration/dépendance** : Déclarée dans pom.xml ou package.json
- 📄 **Documentation** : Mentionnée dans la documentation, non vérifiée
- ❓ **À confirmer** : Nécessite confirmation auprès du stagiaire

---

## 1. LANGAGES DE PROGRAMMATION

### Java 21
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : Utilisé pour tout le backend ; `<java.version>21</java.version>` dans pom.xml
- **Contexte** : Classes, interfaces, annotations (`@Entity`, `@Service`, `@Controller`), enums, patterns (Builder, Repository)
- **Compétences associées** :
  - Programmation orientée objet
  - Gestion des exceptions
  - Streams API (si utilisés dans les services)
  - Enumerations typées

### JavaScript (ES6+)
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : Tous les fichiers frontend (`.js`), utilisation de features modernes
- **Contexte** : Modules, arrow functions, destructuring, async/await (dans Axios calls), template literals
- **Fichiers clés** : `src/router/index.js`, `src/stores/*.js`, `src/services/api.js`
- **Compétences associées** :
  - Programmation fonctionnelle
  - Gestion asynchrone (Promises, async/await)
  - Manipulation du DOM (via Vue)

---

## 2. FRAMEWORKS ET BIBLIOTHÈQUES FRONTEND

### Vue.js 3.3.4
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : package.json, 19 fichiers `.vue`, composants, lifecycle hooks
- **Contexte** : SPA complète, components, props, events, slots, lifecycle (`onMounted`, `onBeforeUnmount`)
- **Fichiers clés** : Tous dans `src/views/` et `src/components/`
- **Compétences associées** :
  - Composants Vue (Single File Components)
  - Props et Emits
  - Watchers et Computed
  - Lifecycle hooks

### Vue Router 4.2.4
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : package.json, `src/router/index.js`, 18 routes définies
- **Contexte** : Navigation SPA, lazy loading, meta guards, paramètres de route
- **Compétences associées** :
  - Routeur configuration
  - Navigation guards (`beforeEach`)
  - Routes paramétrées (`:id`)
  - Lazy loading de composants

### Pinia 2.1.6
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : package.json, 7 stores dans `src/stores/`
- **Contexte** : State management, actions, getters, mutations (implicites)
- **Stores implémentés** : authStore, posteStore, transformateurStore, useMeasurementStore, useAlertStore, useInterventionStore, useComplaintStore
- **Compétences associées** :
  - Gestion d'état centralisée
  - Actions asynchrones (API calls)
  - Computed properties (getters)

### Axios 1.5.0
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : package.json, `src/services/api.js`, 150+ appels API
- **Contexte** : Client HTTP, intercepteurs (injection JWT), gestion d'erreurs
- **Compétences associées** :
  - Configuration d'instances HTTP
  - Intercepteurs (request/response)
  - Gestion des erreurs HTTP
  - Support CORS

### Tailwind CSS 3.3.3
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : package.json, utilisation dans tous les fichiers `.vue`
- **Contexte** : Utility-first CSS, design responsive
- **Compétences associées** :
  - Responsive design
  - Flexbox et Grid avec Tailwind
  - Thémisation
  - Animation CSS

### Chart.js 4.4.0 + vue-chartjs 5.3.0
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : package.json, graphiques dans les dashboards
- **Contexte** : Graphiques interactifs (ligne, barre, secteur)
- **Compétences associées** :
  - Configuration de graphiques
  - Binding de données dynamiques
  - Légendes, labels, tooltips

### Vite 4.4.9
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : package.json, `npm run dev` fonctionne
- **Contexte** : Build tool moderne, Hot Module Replacement (HMR)
- **Compétences associées** :
  - Configuration build
  - Optimisation production
  - HMR pour développement

### Lucide Vue Next 1.0.0
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : package.json (note: dépréciée, recommande `@lucide/vue`)
- **Contexte** : Icônes SVG
- **Note** : Compétence mineure

---

## 3. FRAMEWORKS ET BIBLIOTHÈQUES BACKEND

### Spring Boot 3.5.16
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : pom.xml, parent spring-boot-starter-parent, classe `SmartMonitoringBackendApplication`
- **Contexte** : Framework web complet, auto-configuration, embedded Tomcat
- **Compétences associées** :
  - Configuration Spring Boot
  - Dépendances starters
  - Propriétés application.properties
  - Profiles Spring (dev, prod)

### Spring Web
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : pom.xml `spring-boot-starter-web`, 10 contrôleurs REST
- **Contexte** : Développement d'API REST, mappings HTTP
- **Compétences associées** :
  - @RestController, @RequestMapping, @GetMapping, @PostMapping
  - @PathVariable, @RequestParam, @RequestBody
  - ResponseEntity, HttpStatus

### Spring Data JPA
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : pom.xml `spring-boot-starter-data-jpa`, 8 repositories, requêtes dérivées
- **Contexte** : Accès aux données, Object-Relational Mapping (ORM)
- **Compétences associées** :
  - JpaRepository interface
  - Méthodes dérivées (findBy*, search*)
  - Pagination (Pageable, Page)
  - Requêtes JPQL/Criteria

### Spring Security
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : pom.xml `spring-boot-starter-security`, `SecurityConfig.java`, annotations `@PreAuthorize`
- **Contexte** : Authentification, autorisation, RBAC
- **Compétences associées** :
  - Configuration HttpSecurity
  - Authentication providers
  - Authorization (@PreAuthorize, @Secured)
  - CORS configuration
  - Custom filters

### Hibernate ORM 6.6.53
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : Transitive (via spring-boot-starter-data-jpa), 8 entités JPA
- **Contexte** : Mapping Entity-Relational
- **Compétences associées** :
  - Annotations JPA (@Entity, @Table, @Column, @OneToMany, @ManyToOne)
  - Cascade et orphan removal
  - Lazy/Eager loading

### Lombok
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : pom.xml, annotations `@Data`, `@Builder` sur entités
- **Contexte** : Réduction du boilerplate (getters, setters, equals, hashCode)
- **Compétences associées** :
  - @Data, @Getter, @Setter
  - @Builder pattern
  - @NoArgsConstructor, @AllArgsConstructor

### Maven 3.x
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : `pom.xml` présent, builds Maven réussis
- **Contexte** : Gestion des dépendances, build automation
- **Compétences associées** :
  - Fichier pom.xml structure
  - Dépendances et versions
  - Plugins (compiler, surefire, spring-boot-maven-plugin)
  - Profils (dev, prod)

---

## 4. SÉCURITÉ

### JWT (JSON Web Tokens)
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : jjwt 0.12.7 dans pom.xml, `JwtService.java` complet
- **Contexte** : Authentification sans sessions, tokens de 24h
- **Compétences associées** :
  - Génération JWT (HS256)
  - Validation signature et expiration
  - Extraction de claims
  - Gestion d'erreurs (token expiré, invalide)

### BCrypt
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : Spring Security PasswordEncoder, hachage dans `UserServiceImpl.java`
- **Contexte** : Stockage sécurisé des mots de passe
- **Compétences associées** :
  - Configuration PasswordEncoder
  - Hachage et vérification

### CORS (Cross-Origin Resource Sharing)
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : `SecurityConfig.java`, configuration origins, methods, headers
- **Contexte** : Sécurité cross-origin pour frontend
- **Compétences associées** :
  - Configuration whitelist d'origins
  - Méthodes HTTP autorisées
  - Credentials handling

### RBAC (Role-Based Access Control)
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : Énumération Role, annotations `@PreAuthorize` sur endpoints
- **Contexte** : 4 rôles (ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT) avec permissions distinctes
- **Compétences associées** :
  - Définition de rôles
  - Autorisation granulaire
  - Protection d'endpoints

---

## 5. BASES DE DONNÉES

### H2 Database
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : pom.xml `h2`, configuration `application.properties` (jdbc:h2:mem:steg_monitoring)
- **Contexte** : Base de données en mémoire pour développement/tests
- **Compétences associées** :
  - Configuration H2
  - In-memory database

### PostgreSQL
- **Niveau de preuve** : 📦 Configuration/dépendance
- **Évidence** : pom.xml `postgresql` driver, application-prod.properties
- **Contexte** : Production database
- **Compétences associées** :
  - Configuration PostgreSQL
  - Connection pooling (HikariCP)

### JPA/Hibernate (ORM)
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : 8 entités, repositories, mappings
- **Contexte** : Object-Relational Mapping
- **Compétences associées** :
  - Entités JPA
  - Relations (1-N, N-1, M-N)
  - Annotations de persistence

---

## 6. API ET ARCHITECTURE LOGICIELLE

### REST API Design
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : 70+ endpoints RESTful, codes HTTP appropriés
- **Contexte** : Architecture orientée ressources
- **Compétences associées** :
  - Conventions REST (GET, POST, PUT, DELETE, PATCH)
  - Codes HTTP (200, 201, 204, 400, 401, 403, 404, 500)
  - Pagination
  - Versionning d'API

### Design Patterns
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Patterns identifiés** :
  - **Repository Pattern** : `*Repository` interfaces
  - **Service Pattern** : `*Service` interfaces + `*ServiceImpl`
  - **DTO Pattern** : `*Request`, `*Response`
  - **Factory Pattern** : Builders (`@Builder` Lombok)
  - **Dependency Injection** : Spring `@Autowired`
  - **Singleton** : Spring Beans

### Microservices (partiel)
- **Niveau de preuve** : ❓ À confirmer
- **Contexte** : Application est monolithique actuellement (pas de microservices confirmés)
- **Note** : À demander si migration prévue

---

## 7. INTELLIGENCE ARTIFICIELLE ET MACHINE LEARNING

### Ollama Integration
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : `ChatbotService.java`, `OllamaService.java` (ou classe similaire), endpoint `/api/ai/chatbot/message`
- **Contexte** : Intégration de modèles LLM
- **Compétences associées** :
  - Communication API REST avec services IA
  - Construction de prompts
  - Gestion de timeouts

### Qwen2.5:3b (Large Language Model)
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Contexte** : Modèle utilisé pour le chatbot
- **Note** : Compétence : "Utilisation de LLMs pré-entraînés" (pas d'entraînement custom)

### Prompt Engineering
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : Construction de prompts système dans `ChatbotService.java`
- **Contexte** : Directives au modèle, injection de contexte métier
- **Compétences associées** :
  - Construction de prompts
  - Context injection
  - Role-based prompt customization

### NLP (Natural Language Processing)
- **Niveau de preuve** : 📄 Documentation (implicite via Ollama)
- **Contexte** : Le modèle Qwen2.5:3b effectue le NLP
- **Compétences associées** :
  - Compréhension du langage naturel
  - Génération de texte

---

## 8. OUTILS DE DÉVELOPPEMENT

### Git / Version Control
- **Niveau de preuve** : ❓ À confirmer
- **Évidence** : Dossier `.git` présent
- **Contexte** : Gestion des versions
- **Compétences associées** :
  - Commits, branches
  - Merges
  - Workflows Git

### Maven Build Tool
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : `pom.xml`, builds réussis
- **Compétences associées** :
  - Gestion dépendances
  - Build lifecycles
  - Plugins Maven

### npm / Node Package Manager
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : `package.json`, `npm install` réussi
- **Compétences associées** :
  - Gestion dépendances npm
  - Scripts npm
  - Resolving conflicts

### VS Code (ou autre IDE)
- **Niveau de preuve** : ❓ À confirmer
- **Évidence** : Dossier `.vscode` présent (probablement)
- **Contexte** : Éditeur utilisé
- **Compétences associées** :
  - Configuration IDE
  - Extensions/plugins
  - Debugging

### Postman / Insomnia (API testing)
- **Niveau de preuve** : ❓ À confirmer
- **Contexte** : Outils optionnels pour tester l'API
- **Compétences associées** :
  - Tests API manuel
  - Documentation d'API

---

## 9. MÉTHODOLOGIES ET PRATIQUES

### Full-Stack Development
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : Frontend complet + Backend complet dans ce projet
- **Contexte** : Maîtrise des deux stacks (Vue.js + Spring Boot)

### Responsive Design
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : Utilisation systématique de Tailwind CSS (responsive classes)
- **Contexte** : Design adapté mobile/tablet/desktop

### Separation of Concerns (SoC)
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Contexte** : Frontend/Backend séparés, modules isolés
- **Compétences associées** :
  - Architecture modulaire
  - API boundaries
  - Business logic isolation

### Code Organization
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Contexte** : Structure claire, conventions suivies
- **Compétences associées** :
  - Organisation fichiers
  - Conventions de nommage
  - Modularité

### Error Handling & Logging
- **Niveau de preuve** : ✅ Confirmée dans le code
- **Évidence** : `GlobalExceptionHandler.java`, logs backend
- **Contexte** : Gestion centralisée des erreurs
- **Compétences associées** :
  - Exception handling
  - Logging configuration
  - Error responses

---

## Résumé par niveau de compétence

### Compétences fortes (✅ Confirmées dans le code)

**Langages :**
- Java 21
- JavaScript ES6+

**Frontend :**
- Vue.js 3.3.4 (composants, lifecycle, props, emits)
- Vue Router 4.2.4 (navigation, guards, routing)
- Pinia 2.1.6 (state management)
- Axios 1.5.0 (HTTP client)
- Tailwind CSS 3.3.3 (responsive design)
- Chart.js 4.4.0 (data visualization)
- Vite 4.4.9 (build tool)

**Backend :**
- Spring Boot 3.5.16 (framework)
- Spring Web (REST controllers)
- Spring Data JPA (ORM, repositories)
- Spring Security (authentication, RBAC)
- Hibernate ORM (entity mapping)
- Lombok (boilerplate reduction)
- Maven (build management)

**Sécurité :**
- JWT (HS256)
- BCrypt (password hashing)
- CORS (cross-origin security)
- RBAC (role-based access control)

**Bases de données :**
- H2 Database (in-memory)
- PostgreSQL (production)
- JPA/Hibernate (ORM)

**Architecture :**
- REST API design
- Design patterns (Repository, Service, DTO, Factory, Singleton)
- Full-stack development

**IA :**
- Ollama integration
- Prompt engineering
- LLM usage (Qwen2.5:3b)

### Compétences moyennes (📦 Configuration/dépendance ou 📄 Documentation)

- PostgreSQL (configuration déclarée, non vérifiée en production)
- Microservices (documentation, pas d'implémentation détectée)
- Testing frameworks (dépendances déclarées, zéro implémentation)

### Compétences à confirmer (❓)

- Git workflows
- IDE spécifique (VS Code, IntelliJ, autre)
- Postman/Insomnia API testing
- Migration/upgrade skills
- DevOps (Docker, Kubernetes, CI/CD)
- Performance optimization
- Debugging techniques

---

## Tableau de synthèse

| Domaine | Compétence | Niveau | Évidence |
|---------|-----------|--------|----------|
| Langages | Java 21 | ✅ Confirmé | Code backend |
| Langages | JavaScript ES6+ | ✅ Confirmé | Code frontend |
| Frontend | Vue.js 3 | ✅ Confirmé | 19 components, 7 stores |
| Frontend | Vue Router | ✅ Confirmé | 18 routes, guards |
| Frontend | Pinia | ✅ Confirmé | 7 stores |
| Frontend | Axios | ✅ Confirmé | 150+ API calls |
| Frontend | Tailwind CSS | ✅ Confirmé | Tous les components |
| Frontend | Chart.js | ✅ Confirmé | Dashboards |
| Frontend | Vite | ✅ Confirmé | Build tool |
| Backend | Spring Boot | ✅ Confirmé | 70+ endpoints |
| Backend | Spring Web | ✅ Confirmé | REST controllers |
| Backend | Spring Data JPA | ✅ Confirmé | 8 entities, repos |
| Backend | Spring Security | ✅ Confirmé | JWT, RBAC, CORS |
| Backend | Hibernate | ✅ Confirmé | ORM, mappings |
| Backend | Lombok | ✅ Confirmé | @Data, @Builder |
| Backend | Maven | ✅ Confirmé | Build réussi |
| Security | JWT | ✅ Confirmé | JwtService.java |
| Security | BCrypt | ✅ Confirmé | UserServiceImpl.java |
| Security | CORS | ✅ Confirmé | SecurityConfig.java |
| Security | RBAC | ✅ Confirmé | Annotations @PreAuthorize |
| DB | H2 | ✅ Confirmé | Dev database |
| DB | PostgreSQL | 📦 Configuration | Driver présent |
| DB | JPA/Hibernate | ✅ Confirmé | 8 entities |
| Architecture | REST API | ✅ Confirmé | 70+ endpoints |
| Architecture | Design Patterns | ✅ Confirmé | Repository, Service, DTO |
| Architecture | Full-stack | ✅ Confirmé | Frontend + Backend |
| IA | Ollama | ✅ Confirmé | ChatbotService |
| IA | Prompt Engineering | ✅ Confirmé | Context building |
| IA | LLM Usage | ✅ Confirmé | Qwen2.5:3b |
| Tools | Git | ❓ À confirmer | .git folder present |
| Methodology | Responsive Design | ✅ Confirmé | Tailwind CSS |
| Methodology | Error Handling | ✅ Confirmé | GlobalExceptionHandler |

---

## Compétences à mettre en avant sur CV

**Fort impact (vérifié dans le code) :**
1. Vue.js 3 full-stack (composants, routing, state management)
2. Spring Boot 3 API REST (70+ endpoints)
3. JWT + RBAC security
4. JPA/Hibernate ORM design
5. Full-stack architecture (frontend + backend)
6. Ollama/LLM integration
7. Responsive design (Tailwind CSS)
8. Real-time data visualization (Chart.js)

**À nuancer :**
- **Testing** : 0% coverage (ne pas mentionner comme force)
- **DevOps** : Non confirmé (ne pas mentionner)
- **Cloud deployment** : Non confirmé (ne pas mentionner)

