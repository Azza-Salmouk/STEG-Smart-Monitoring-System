# 🏗️ ARCHITECTURE RECOMMANDÉE - MODULES FUTURS

## Structure Actuelle ✅

La base d'authentification est maintenant **production-ready** avec:
- Exception handling centralisé
- Validation robuste
- CORS configuré
- JWT sécurisé
- Patterns SOLID appliqués

## Patterns à Réutiliser

### 1. Layered Architecture (Couches)

```
CONTROLLER → SERVICE → REPOSITORY → ENTITY
```

Chaque module métier (Poste, Mesure, Alerte, etc.) suit le même pattern.

### 2. Structure Recommandée par Module

```
src/main/java/com/steg/smartmonitoring/{MODULE}/
├── controller/
│   └── {Module}Controller.java
├── dto/
│   ├── Create{Module}Request.java
│   ├── Update{Module}Request.java
│   └── {Module}Response.java
├── entity/
│   └── {Module}.java
├── repository/
│   └── {Module}Repository.java
├── service/
│   ├── {Module}Service.java (interface)
│   └── impl/
│       └── {Module}ServiceImpl.java
└── mapper/
    └── {Module}Mapper.java (optional)
```

### 3. DTOs Standardisées

```java
// REQUEST pour CREATE
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CreatePosteRequest {
    @NotBlank String name;
    @NotBlank String location;
    // autres champs...
}

// REQUEST pour UPDATE
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdatePosteRequest {
    String name;  // nullable pour update partiel
    String location;
}

// RESPONSE
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PosteResponse {
    Long id;
    String name;
    String location;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
```

### 4. Controller Pattern

```java
@RestController
@RequestMapping("/api/postes")
@RequiredArgsConstructor
public class PosteController {

    private final PosteService posteService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<PosteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(posteService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PosteResponse> create(@Valid @RequestBody CreatePosteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(posteService.create(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PosteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePosteRequest req) {
        return ResponseEntity.ok(posteService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        posteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 5. Service Interface Pattern

```java
public interface PosteService {
    PosteResponse findById(Long id) throws ResourceNotFoundException;
    List<PosteResponse> findAll();
    PosteResponse create(CreatePosteRequest request);
    PosteResponse update(Long id, UpdatePosteRequest request) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
}
```

### 6. Service Implementation Pattern

```java
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PosteServiceImpl implements PosteService {

    private final PosteRepository posteRepository;
    private final PosteMapper posteMapper;  // optional, MapStruct

    @Override
    @Transactional(readOnly = true)
    public PosteResponse findById(Long id) {
        return posteRepository.findById(id)
                .map(posteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Poste avec ID " + id + " non trouvé"
                ));
    }

    @Override
    public PosteResponse create(CreatePosteRequest request) {
        Poste poste = posteMapper.toEntity(request);
        Poste saved = posteRepository.save(poste);
        return posteMapper.toResponse(saved);
    }

    @Override
    public PosteResponse update(Long id, UpdatePosteRequest request) {
        Poste poste = posteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poste non trouvé"));
        
        posteMapper.updateEntity(request, poste);
        Poste updated = posteRepository.save(poste);
        return posteMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!posteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Poste non trouvé");
        }
        posteRepository.deleteById(id);
    }
}
```

### 7. Repository Pattern

```java
@Repository
public interface PosteRepository extends JpaRepository<Poste, Long> {
    List<Poste> findByNameContainingIgnoreCase(String name);
    Optional<Poste> findByCode(String code);
    boolean existsByCode(String code);
}
```

### 8. Entity Base Pattern

```java
@MappedSuperclass
@Getter @Setter
public abstract class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(nullable = false)
    protected LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

// Utilisation dans métier:
@Entity @Table(name = "postes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Poste extends BaseEntity {
    private String name;
    private String location;
    private String code;
    // ...
}
```

## Modules à Développer

### 1. Module Postes (Posts)

**Responsabilités:** CRUD postes, association transformateurs  
**Endpoints:**
```
GET    /api/postes                    (liste)
GET    /api/postes/{id}               (détail)
POST   /api/postes                    (créer)
PUT    /api/postes/{id}               (modifier)
DELETE /api/postes/{id}               (supprimer)
```

### 2. Module Transformateurs (Transformers)

**Responsabilités:** CRUD transformateurs, association postes/mesures  
**Endpoints:**
```
GET    /api/transformateurs           (liste)
GET    /api/transformateurs/{id}      (détail)
POST   /api/transformateurs           (créer)
PUT    /api/transformateurs/{id}      (modifier)
DELETE /api/transformateurs/{id}      (supprimer)
GET    /api/transformateurs/{id}/mesures (mesures récentes)
```

### 3. Module Mesures (Measurements)

**Responsabilités:** Ingestion données IoT, stockage, requêtes  
**Endpoints:**
```
POST   /api/mesures/push              (ESP32 push)
GET    /api/mesures?transformateur_id=1&date_start=...&date_end=... (requête)
GET    /api/mesures/{id}              (détail)
```

### 4. Module Alertes (Alerts)

**Responsabilités:** Règles d'alerte, notification, historique  
**Endpoints:**
```
GET    /api/alertes                   (alertes actives)
GET    /api/alertes/historique        (historique)
POST   /api/alertes                   (créer règle)
PUT    /api/alertes/{id}              (modifier règle)
DELETE /api/alertes/{id}              (supprimer règle)
```

### 5. Module Interventions

**Responsabilités:** CRUD interventions, assignation techniciens  
**Endpoints:**
```
GET    /api/interventions             (liste)
POST   /api/interventions             (créer)
PUT    /api/interventions/{id}        (modifier)
PATCH  /api/interventions/{id}/status (changer status)
```

### 6. Module Dashboard

**Responsabilités:** KPIs, statistiques, données agrégées  
**Endpoints:**
```
GET    /api/dashboard/metrics         (KPIs actuels)
GET    /api/dashboard/tendances       (tendances temps)
GET    /api/dashboard/alertes-top     (alertes les plus fréquentes)
```

### 7. Module AI/Predictions

**Responsabilités:** Appels Python ML, cache résultats  
**Endpoints:**
```
POST   /api/ai/predict                (prédiction anomalie)
GET    /api/ai/model/status           (status du modèle)
POST   /api/ai/train                  (retrain modèle)
```

## Intégrations Externes

### Données IoT (ESP32)

```
ESP32 → POST /api/mesures/push (X-API-Key: secret)
Payload: {
    "device_id": "ESP32_001",
    "transformateur_id": 5,
    "timestamp": "2024-12-10T10:30:00Z",
    "tension": 380.5,
    "courant": 120.3,
    "temperature": 65.2,
    "frequency": 50.0
}
```

**Sécurité:** X-API-Key simple (pas JWT, ESP32 n'a pas de user)

### Power BI

```
Power BI → GET /api/dashboard/export?format=json
Retourne: Données structurées pour visualisations
```

**Auth:** Bearer token (même utilisateur frontend)

### Python ML Server

```
Backend → POST http://ml-server:5000/predict
Request: {mesures: [...]}
Response: {anomaly_score: 0.85, prediction: "WARNING"}
```

**Communication:** REST simple, pas JWT (service-to-service)

## Sécurité par Niveau

### Public (Pas d'auth)
- Health check: GET /actuator/health
- Login/Register: POST /api/auth/login, /api/auth/register

### Authentifié (tous les rôles)
- GET profil utilisateur
- GET données publiques

### ADMIN
- CRUD utilisateurs
- CRUD postes, transformateurs
- Configuration alertes
- Accès tous les logs

### SUPERVISOR
- CRUD transformateurs (assigned)
- CRUD mesures (assigned)
- CRUD interventions
- Rapport dashboard

### AGENT
- Lecture transformateurs
- Lecture mesures (assigned)
- Update interventions (assigned)

## Recommandations Finales

1. **Utilisez le même GlobalExceptionHandler** pour tous les modules
2. **Réutilisez les DTOs** (CreateRequest, UpdateRequest, Response pattern)
3. **Utilisez `@Transactional`** pour services critiques (create, update, delete)
4. **Utilisez `@PreAuthorize`** pour security au niveau controller
5. **Loggez les opérations** critiques (create, delete, updates)
6. **Validez les inputs** avec Jakarta Validation
7. **Testez avec JUnit + MockMvc** (à implémenter)
8. **Documentez avec Swagger/Springdoc** (optionnel mais recommandé)
9. **Utilisez Pagination** pour GET lists (Spring Data JpaRepository)
10. **Cachéz les requêtes** fréquentes avec @Cacheable

---

**Architecture PRÊTE pour extensibilité! 🚀**
