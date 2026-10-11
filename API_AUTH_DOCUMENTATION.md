# 🔐 Documentation API - Authentification

## Vue d'ensemble

API REST d'authentification JWT pour Smart Monitoring Platform. Tous les endpoints retournent du JSON.

**Base URL:** `http://localhost:8081/api/auth`  
**Content-Type:** `application/json`

---

## Endpoints

### 1. Register - Créer un nouvel utilisateur

**Endpoint:** `POST /api/auth/register`

**Description:** Enregistrer un nouvel utilisateur dans le système

**Request:**
```json
{
    "username": "John Doe",
    "email": "john.doe@steg.tn",
    "password": "SecurePass@123",
    "role": "AGENT"
}
```

**Validation:**
- `username`: Non vide (requis)
- `email`: Format email valide, unique en DB
- `password`: Au moins 8 caractères, 1 majuscule, 1 minuscule, 1 chiffre, 1 caractère spécial (@$!%*?&)
- `role`: ADMIN, SUPERVISOR, ou AGENT

**Response - Success (201 Created):**
```json
{
    "id": 2,
    "username": "John Doe",
    "email": "john.doe@steg.tn",
    "role": "AGENT",
    "token": null
}
```

**Response - Email existe (409 Conflict):**
```json
{
    "status": 409,
    "message": "Un utilisateur avec cet email existe déjà",
    "error": "CONFLICT",
    "path": "/api/auth/register",
    "timestamp": "2024-12-10T10:30:00"
}
```

**Response - Validation échouée (400 Bad Request):**
```json
{
    "status": 400,
    "message": "Erreur de validation",
    "error": "VALIDATION_ERROR",
    "path": "/api/auth/register",
    "timestamp": "2024-12-10T10:30:00",
    "details": {
        "password": "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial",
        "email": "L'email doit être valide"
    }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "John Doe",
    "email": "john.doe@steg.tn",
    "password": "SecurePass@123",
    "role": "AGENT"
  }'
```

---

### 2. Login - Authentifier un utilisateur

**Endpoint:** `POST /api/auth/login`

**Description:** Authentifier un utilisateur et obtenir un JWT token

**Request:**
```json
{
    "email": "admin@steg.tn",
    "password": "Admin@123"
}
```

**Validation:**
- `email`: Format email valide
- `password`: Non vide

**Response - Success (200 OK):**
```json
{
    "id": 1,
    "username": "Administrator",
    "email": "admin@steg.tn",
    "role": "ADMIN",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBzdGVnLnRuIiwiaWF0IjoxNzMzODI2NjAwLCJleHAiOjE3MzM5MTMwMDB9.a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
}
```

**Token Info:**
- **Type:** JWT (JSON Web Token)
- **Algorithm:** HS256 (HMAC SHA-256)
- **Expiration:** 24h (86400 secondes) après émission
- **Subject:** Email de l'utilisateur

**Response - Credentials invalides (401 Unauthorized):**
```json
{
    "status": 401,
    "message": "Email ou mot de passe incorrect",
    "error": "UNAUTHORIZED",
    "path": "/api/auth/login",
    "timestamp": "2024-12-10T10:30:00"
}
```

**Response - Validation échouée (400 Bad Request):**
```json
{
    "status": 400,
    "message": "Erreur de validation",
    "error": "VALIDATION_ERROR",
    "path": "/api/auth/login",
    "timestamp": "2024-12-10T10:30:00",
    "details": {
        "email": "L'email est requis",
        "password": "Le mot de passe est requis"
    }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@steg.tn",
    "password": "Admin@123"
  }'
```

---

## Utiliser le Token JWT

### Header Authorization

Toutes les requêtes protégées doivent inclure le header:

```
Authorization: Bearer <token>
```

**Exemple:**
```bash
curl -X GET http://localhost:8081/api/protected/resource \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Validation du Token

Le serveur va:
1. Extraire le token du header `Authorization: Bearer`
2. Vérifier la signature (HS256)
3. Vérifier que le token n'est pas expiré
4. Charger l'utilisateur depuis la DB
5. Définir les rôles dans le contexte Spring Security

### Erreurs Token

**Token manquant ou malformé (401 Unauthorized):**
```json
{
    "status": 401,
    "message": "Token invalide ou expiré",
    "error": "UNAUTHORIZED",
    "path": "/api/protected/resource",
    "timestamp": "2024-12-10T10:30:00"
}
```

**Token expiré (401 Unauthorized):**
```json
{
    "status": 401,
    "message": "Token invalide ou expiré",
    "error": "UNAUTHORIZED",
    "path": "/api/protected/resource",
    "timestamp": "2024-12-10T10:30:00"
}
```

---

## Rôles & Permissions

### Rôles disponibles

| Rôle | Description |
|------|-------------|
| **ADMIN** | Accès complet au système, gestion utilisateurs |
| **SUPERVISOR** | Gestion postes/transformateurs assignés |
| **AGENT** | Lecture seule, mises à jour de terrain |

### Utilisation dans les controllers

```java
@GetMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")  // Seulement ADMIN
public ResponseEntity<...> get(@PathVariable Long id) { ... }

@PostMapping
@PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")  // ADMIN ou SUPERVISOR
public ResponseEntity<...> create(@RequestBody ...) { ... }

@GetMapping
@PreAuthorize("isAuthenticated()")  // N'importe quel utilisateur authentifié
public ResponseEntity<...> list() { ... }
```

---

## Données d'Exemple

### Admin par défaut (créé au démarrage)

```
Email: admin@steg.tn
Password: Admin@123
Role: ADMIN
```

**Créer d'autres utilisateurs via Register endpoint**

---

## Exemples d'Utilisation

### Python Requests

```python
import requests
import json

BASE_URL = "http://localhost:8081/api/auth"

# Register
register_data = {
    "username": "Ahmed",
    "email": "ahmed@steg.tn",
    "password": "Secure@Pass123",
    "role": "SUPERVISOR"
}

response = requests.post(f"{BASE_URL}/register", json=register_data)
print(response.json())

# Login
login_data = {
    "email": "ahmed@steg.tn",
    "password": "Secure@Pass123"
}

response = requests.post(f"{BASE_URL}/login", json=login_data)
token = response.json()["token"]

# Use token
headers = {"Authorization": f"Bearer {token}"}
response = requests.get("http://localhost:8081/api/protected/resource", headers=headers)
print(response.json())
```

### JavaScript/Fetch

```javascript
const BASE_URL = "http://localhost:8081/api/auth";

// Register
async function register() {
    const response = await fetch(`${BASE_URL}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            username: "Sarah",
            email: "sarah@steg.tn",
            password: "Secure@Pass123",
            role: "AGENT"
        })
    });
    return await response.json();
}

// Login
async function login() {
    const response = await fetch(`${BASE_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            email: "admin@steg.tn",
            password: "Admin@123"
        })
    });
    return await response.json();
}

// Use token
async function getProtectedData() {
    const loginResponse = await login();
    const token = loginResponse.token;

    const response = await fetch("http://localhost:8081/api/protected/resource", {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });
    return await response.json();
}
```

### Postman Collection

```json
{
  "info": {
    "name": "Smart Monitoring Auth API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Register",
      "request": {
        "method": "POST",
        "url": "http://localhost:8081/api/auth/register",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\"username\":\"John\",\"email\":\"john@steg.tn\",\"password\":\"Pass@123\",\"role\":\"AGENT\"}"
        }
      }
    },
    {
      "name": "Login",
      "request": {
        "method": "POST",
        "url": "http://localhost:8081/api/auth/login",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\"email\":\"admin@steg.tn\",\"password\":\"Admin@123\"}"
        }
      }
    }
  ]
}
```

---

## Statuts HTTP

| Code | Signification | Exemple |
|------|---------------|---------|
| **200** | OK - Requête réussie | Login OK, GET resource |
| **201** | Created - Ressource créée | Register OK |
| **400** | Bad Request - Validation échouée | Email invalide, password trop court |
| **401** | Unauthorized - Auth échouée ou token invalide | Credentials faux, token expiré |
| **409** | Conflict - Email déjà enregistré | Register avec email existant |
| **500** | Internal Server Error - Erreur serveur | Erreur DB, Exception non catchée |

---

## Configuration

### application.properties

```properties
# JWT
jwt.secret=4fd86f2c89e0bcb54b4c37a90c2f7d4d5d7a6c8b9e1f0a123456789abcdef123456789abcdef123456789abcdef12
jwt.expiration=86400000  # 24h en millisecondes

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/steg_monitoring
spring.datasource.username=steg_admin
spring.datasource.password=steg_password
spring.jpa.hibernate.ddl-auto=update

# Server
server.port=8081
```

### CORS Configuration

Frontend autorisé sur:
- http://localhost:3000 (React)
- http://localhost:4200 (Angular)
- http://localhost:5173 (Vite)

---

## Dépannage

### "Email ou mot de passe incorrect"

- Vérifier que l'email existe en DB
- Vérifier que le mot de passe est correct (sensible à la casse)
- Password stockée en hash, pas comparée en plaintext

### "Token invalide ou expiré"

- Vérifier que le token commence par `Bearer `
- Vérifier que le token n'a pas 24h+ d'âge
- Vérifier que la signature JWT est correcte (secret en production)
- Re-login pour obtenir un nouveau token

### "Un utilisateur avec cet email existe déjà"

- Email unique en DB
- Créer un nouvel utilisateur avec un email différent
- Ou utiliser Login avec l'email existant

### Le mot de passe n'est pas accepté

- Minimum 8 caractères
- Au moins 1 majuscule (A-Z)
- Au moins 1 minuscule (a-z)
- Au moins 1 chiffre (0-9)
- Au moins 1 caractère spécial (@$!%*?&)

Exemple valide: `SecurePass@123`  
Exemple invalide: `password123` (pas de majuscule, pas de caractère spécial)

---

## Sécurité

✅ **Implémentée:**
- Password BCrypt avec 4 rounds
- JWT HS256 signé
- Stateless session (scalable)
- CSRF disabled pour API
- Email unique
- Token 24h expiration

⚠️ **À implémenter:**
- Refresh token (optionnel)
- Rate limiting login
- Email verification
- 2FA (optionnel)

---

## Support

Pour toute question ou bug, contacter l'équipe de développement.
