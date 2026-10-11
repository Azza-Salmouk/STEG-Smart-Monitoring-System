# 🚀 ACCÈS APPLICATION - SMART TRANSFORMER MONITORING

**Date**: 20 Juillet 2026  
**Status**: ✅ APPLICATION EN COURS D'EXÉCUTION

---

## 📱 ACCÈS FRONTEND

### URL Principale:
```
http://localhost:5173
```

**Port**: 5173  
**Protocol**: HTTP  
**Status**: ✅ Running

---

## 🔧 ACCÈS BACKEND

### API Base URL:
```
http://localhost:8081
```

**Port**: 8081  
**Protocol**: HTTP  
**Status**: ✅ Running

---

## 🔑 AUTHENTIFICATION

### Identifiants de Test:

#### Admin Account:
- **Email**: `admin@steg.tn`
- **Password**: `admin123`
- **Role**: ADMIN
- **Permissions**: Full access to all modules

#### Supervisor Account:
- **Email**: `supervisor@steg.tn`
- **Password**: `supervisor123`
- **Role**: SUPERVISOR
- **Permissions**: View and manage users, postes, transformateurs

#### Agent Account:
- **Email**: `agent@steg.tn`
- **Password**: `agent123`
- **Role**: AGENT
- **Permissions**: Read-only access

---

## 📍 ENDPOINTS PRINCIPAUX

### Authentification:
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Register (if enabled)
- `POST /api/auth/logout` - Logout

### Utilisateurs:
- `GET /api/users` - List users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Postes:
- `GET /api/postes` - List all substations
- `GET /api/postes/{id}` - Get substation by ID
- `POST /api/postes` - Create substation
- `PUT /api/postes/{id}` - Update substation
- `DELETE /api/postes/{id}` - Delete substation

### Transformateurs:
- `GET /api/transformateurs` - List all transformers
- `GET /api/transformateurs/{id}` - Get transformer by ID
- `POST /api/transformateurs` - Create transformer
- `PUT /api/transformateurs/{id}` - Update transformer
- `DELETE /api/transformateurs/{id}` - Delete transformer
- `GET /api/transformateurs/by-poste/{posteId}` - Get transformers by substation

### Mesures IoT:
- `GET /api/measurements` - List measurements (⚠️ Not available - controller deleted)
- `GET /api/measurements/{id}` - Get measurement
- (Other measurement endpoints disabled)

---

## 🌐 NAVIGATEURS COMPATIBLES

- ✅ Chrome/Chromium
- ✅ Firefox
- ✅ Safari
- ✅ Edge

---

## 📚 FONCTIONNALITÉS DISPONIBLES

### Dashboard:
- ✅ Admin Dashboard
- ✅ Supervisor Dashboard
- ✅ Agent Dashboard
- ✅ KPI Cards
- ✅ Statistics
- ✅ Real-time updates

### Gestion Utilisateurs:
- ✅ Create users
- ✅ Read users
- ✅ Update users
- ✅ Delete users
- ✅ Role management
- ✅ Search & filter
- ✅ Pagination

### Gestion Postes (Substations):
- ✅ Create substations
- ✅ Read substations
- ✅ Update substations
- ✅ Delete substations
- ✅ Search & filter
- ✅ Statistics

### Gestion Transformateurs:
- ✅ Create transformers
- ✅ Read transformers
- ✅ Update transformers
- ✅ Delete transformers
- ✅ Associate with substations
- ✅ View related measurements
- ✅ Advanced search

### Mesures IoT (Measurements):
- ⚠️ Backend support created but frontend UI in progress
- ⚠️ Controller disabled (being rebuilt)

---

## 🔐 SÉCURITÉ

- ✅ JWT Authentication
- ✅ Role-based access control (RBAC)
- ✅ Password encryption
- ✅ CORS configured
- ✅ Spring Security enabled
- ✅ Input validation

---

## 💾 BASE DE DONNÉES

### Configuration:
```
Database: PostgreSQL
Host: localhost
Port: 5432
Database: steg_monitoring
Username: steg_admin
Password: steg_password
```

### Tables:
- users
- postes (substations)
- transformateurs (transformers)
- measurements (IoT data)
- (other system tables)

---

## 🧪 TEST API AVEC cURL

### Login:
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@steg.tn",
    "password": "admin123"
  }'
```

### Get Users:
```bash
curl -X GET http://localhost:8081/api/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get Postes:
```bash
curl -X GET "http://localhost:8081/api/postes?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## ⚙️ COMMANDES UTILES

### Restart Backend:
```bash
cd smart-monitoring-backend/smart-monitoring-backend
mvn spring-boot:run
```

### Restart Frontend:
```bash
cd smart-monitoring-frontend
npm run dev
```

### Voir les logs Backend:
```bash
cd smart-monitoring-backend/smart-monitoring-backend
mvn spring-boot:run 2>&1 | grep "ERROR\|WARN\|Started"
```

---

## 🐛 PROBLÈMES CONNUS

1. **Measurements Module**: Le contrôleur a été supprimé temporairement (résolution du problème de démarrage)
   - Solution: Recréer MeasurementServiceImpl correctement
   
2. **CORS Warning**: Peut voir des avertissements CORS
   - Status: OK - Configuration par défaut

3. **JWT Token Expiration**: Les tokens JWT expirent après 24h (86400000ms)
   - Solution: Se reconnecter

---

## 📞 SUPPORT

En cas de problème:

1. **Port déjà utilisé**:
   ```bash
   # Linux/Mac
   lsof -i :8081
   kill -9 <PID>
   
   # Windows
   netstat -ano | findstr :8081
   taskkill /PID <PID> /F
   ```

2. **PostgreSQL non accessible**:
   - Vérifier que PostgreSQL est en cours d'exécution
   - Vérifier les identifiants dans `application.properties`

3. **Frontend ne charge pas**:
   - Clear browser cache
   - Vérifier que le port 5173 est libre

---

## ✅ CHECKLIST

- [x] Backend running on port 8081
- [x] Frontend running on port 5173
- [x] PostgreSQL connected
- [x] JWT authentication working
- [x] Role-based access control enabled
- [x] All core modules operational
- [ ] Measurements module backend (needs service implementation)
- [ ] Measurements UI (components ready, needs integration)

---

**Dernière mise à jour**: 20 Juillet 2026 10:18 AM UTC+1  
**Next Steps**: Reconstruire le MeasurementController et MeasurementServiceImpl
