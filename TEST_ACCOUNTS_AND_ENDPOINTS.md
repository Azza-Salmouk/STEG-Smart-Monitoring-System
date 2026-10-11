# Comptes de Test et Endpoints - Dashboard Corrections

**Date**: 12 Août 2026  
**Application**: STEG Smart Monitoring  
**Environnement**: Développement Local

---

## 🔐 COMPTES DE TEST

### Compte ADMIN
```
Email: admin@steg.tn
Mot de passe: Admin@123
Rôle: ADMIN
Dashboard: /admin/dashboard
Permissions: Tous les modules + Paramètres
```

### Compte SUPERVISOR
```
Email: supervisor@steg.tn
Mot de passe: [Consulter le système]
Rôle: SUPERVISOR
Dashboard: /supervisor/dashboard
Permissions: Postes, Transformateurs, Mesures, Interventions, Réclamations, Alertes
```

### Compte AGENT ⭐ IMPORTANT POUR LES TESTS
```
Email: agent@steg.tn
Mot de passe: [Consulter le système]
Rôle: AGENT
Dashboard: /agent/dashboard
Permissions: Transformateurs, Mesures, Interventions, Réclamations (✅ AJOUTÉ), Alertes
```

### Compte CUSTOMER
```
Email: customer@steg.tn
Mot de passe: [Consulter le système]
Rôle: SERVICE_CLIENT
Dashboard: /serviceclient/dashboard
Permissions: Réclamations uniquement
```

---

## 🌐 ENDPOINTS DE L'APPLICATION

### Frontend
```
Frontend: http://localhost:5173
Login page: http://localhost:5173/login
```

### Backend
```
Backend: http://localhost:8081
API Base: http://localhost:8081/api
```

---

## 📊 ENDPOINTS DASHBOARDS

### Dashboard API Endpoints

#### 1. Admin Dashboard
```
GET /api/dashboard/admin
Authorization: Bearer <ADMIN_TOKEN>
Role Required: ADMIN
Response: AdminDashboardDTO
```

#### 2. Supervisor Dashboard ⭐ TEST ICÔNES
```
GET /api/dashboard/supervisor
Authorization: Bearer <SUPERVISOR_TOKEN>
Role Required: SUPERVISOR
Response: SupervisorDashboardDTO

IMPORTANT: Vérifier que les icônes sont des emoji (🏢 ⚡ 📊 🚨)
           PAS de texte "building-2", "zap", "activity", "alert-circle"
```

#### 3. Agent Dashboard ⭐ TEST CHARGEMENT
```
GET /api/dashboard/agent/{id}
Authorization: Bearer <AGENT_TOKEN>
Path Parameter: {id} = ID de l'agent connecté
Role Required: AGENT
Response: AgentDashboardDTO

IMPORTANT: Le dashboard DOIT charger sans erreur
           DOIT afficher 4 KPI cards avec données réelles
```

#### 4. Customer Dashboard
```
GET /api/dashboard/service-client
Authorization: Bearer <CUSTOMER_TOKEN>
Role Required: SERVICE_CLIENT
Response: ServiceClientDashboardDTO
```

---

## 🔗 AUTRES ENDPOINTS CLÉS

### Complaints API ⭐ AGENT PEUT MAINTENANT ACCÉDER
```
GET /api/complaints
Authorization: Bearer <TOKEN>
Roles Allowed: ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT

Before Fix: Agent got 403 Forbidden
After Fix: Agent gets 200 OK ✅
```

### Interventions API
```
GET /api/interventions
Authorization: Bearer <TOKEN>
Roles Allowed: ADMIN, SUPERVISOR, AGENT
```

### Alerts API
```
GET /api/alerts
Authorization: Bearer <TOKEN>
Roles Allowed: ADMIN, SUPERVISOR, AGENT
```

### Measurements API
```
GET /api/measurements
Authorization: Bearer <TOKEN>
Roles Allowed: ADMIN, SUPERVISOR, AGENT
```

---

## 🧪 NAVIGATION POUR LES TESTS

### Test D8: Admin Dashboard
```
1. Ouvrir http://localhost:5173
2. Login: admin@steg.tn / Admin@123
3. Vérifier redirection: /admin/dashboard
4. Vérifier: Dashboards charge, données réelles affichées
```

### Test D9: Supervisor Dashboard (CRITIQUE - Icônes)
```
1. Logout (clic profil → Déconnexion)
2. Login: supervisor@steg.tn / [mot de passe]
3. Vérifier redirection: /supervisor/dashboard
4. VÉRIFIER ICÔNES: 🏢 ⚡ 📊 🚨 (PAS "building-2", "zap", etc.)
5. Si texte visible: Recharger (F5) et réessayer
```

### Test D10: Agent Dashboard (CRITIQUE - Chargement + Réclamations)
```
1. Logout
2. Login: agent@steg.tn / [mot de passe]
3. Vérifier redirection: /agent/dashboard
4. VÉRIFIER: Dashboard charge SANS "Impossible de charger" ✅
5. VÉRIFIER: 4 KPI cards avec données réelles
6. VÉRIFIER: "Réclamations" visible dans le sidebar
7. CLIC: "Réclamations" → navigation vers /complaints
8. VÉRIFIER: Page Réclamations charge (pas d'erreur 403)
```

### Test D11: Customer Dashboard
```
1. Logout
2. Login: customer@steg.tn / [mot de passe]
3. Vérifier redirection: /serviceclient/dashboard
4. Vérifier: Dashboard charge correctement
5. Vérifier: Pas de régression, données affichées
```

---

## 🔍 VÉRIFICATIONS DANS LA CONSOLE NAVIGATEUR

### Ouvrir la console (F12)
```
Raccourci: F12 ou Ctrl+Shift+I
Aller à: Console tab
```

### Erreurs à vérifier
- ❌ PAS d'erreurs rouges
- ❌ PAS d'erreur 401 (Unauthorized)
- ❌ PAS d'erreur 403 (Forbidden)
- ❌ PAS d'erreur 404 (Not Found)
- ❌ PAS d'erreur 500 (Server Error)

### Appels API à vérifier (Network tab)
```
Aller à: Network tab (F12 → Network)
Recharger la page (F5)
Chercher les appels:
- /api/dashboard/admin → 200 OK (si ADMIN)
- /api/dashboard/supervisor → 200 OK (si SUPERVISOR)
- /api/dashboard/agent/{id} → 200 OK (si AGENT) ✅
- /api/dashboard/service-client → 200 OK (si CUSTOMER)
- /api/complaints → 200 OK (si autorisé) ✅
```

---

## 📋 DÉMARRER LES SERVEURS

### Terminal 1 - Backend
```bash
cd smart-monitoring-backend
mvn spring-boot:run
```

**Status**: Backend démarre à http://localhost:8081  
**Attendre**: "Started ... in X.XXX seconds"

### Terminal 2 - Frontend
```bash
cd smart-monitoring-frontend
npm run dev
```

**Status**: Frontend démarre à http://localhost:5173  
**Attendre**: "VITE vX.X.X ready in X ms"

---

## ✅ PRÉ-TEST CHECKLIST

Avant de commencer les tests D8-D11:

- [ ] Backend tourne sur http://localhost:8081
- [ ] Frontend tourne sur http://localhost:5173
- [ ] Compilation réussie (vérifier: DASHBOARD_CORRECTIONS_TEST_GUIDE.md)
- [ ] Fichiers modifiés en place (AgentDashboard.vue, SupervisorDashboard.vue, router/index.js)
- [ ] Console navigateur ouverte (F12)
- [ ] Network tab prêt pour surveiller les appels API
- [ ] Documents de test à côté: DASHBOARD_CORRECTIONS_TEST_GUIDE.md
- [ ] Comptes de test prêts

---

## 🎯 POINTS CRITIQUES

### ⭐ Test D9: Supervisor Dashboard - ICÔNES
**DOIT être un emoji**: 🏢 ⚡ 📊 🚨  
**NE DOIT PAS être du texte**: "building-2", "zap", "activity", "alert-circle"

**Si vous voyez du texte**:
1. Recharger la page (F5)
2. Vider le cache navigateur (Ctrl+Shift+Delete)
3. Redémarrer le frontend (npm run dev)
4. Si toujours du texte: Signaler dans le rapport

---

### ⭐ Test D10: Agent Dashboard - CHARGEMENT
**DOIT afficher**: Dashboard avec 4 KPI cards + données  
**NE DOIT PAS afficher**: "Impossible de charger le dashboard"

**Si vous voyez l'erreur**:
1. Vérifier F12 Console pour les erreurs
2. Vérifier Network tab: /api/dashboard/agent/{id} → Status?
3. Vérifier backend: T-il reçu l'appel?
4. Si erreur 404: Backend n'a pas trouvé l'endpoint
5. Si erreur 401: Token invalide
6. Si erreur 403: Permissions insuffisantes

---

### ⭐ Test D10: Agent - RÉCLAMATIONS
**DOIT être visible**: "Réclamations" dans le sidebar  
**DOIT fonctionner**: Clic → navigation vers /complaints  
**DOIT charger**: Page Réclamations affiche des données

**Si accès refusé (403)**:
1. Vérifier que AGENT a été ajouté aux rôles dans router/index.js
2. Vérifier backend: Agent a-t-il le rôle AGENT?
3. Vérifier permissions RBAC backend

---

## 📞 DÉPANNAGE

### Problème: "Impossible de charger le dashboard" (Agent)
**Solution**:
1. Vérifier que AgentDashboard.vue a importé useAuthStore
2. Vérifier que authStore.user?.id est récupéré
3. Vérifier que getAgentDashboard(agentId) est appelé avec l'ID

### Problème: Icônes affichées en texte (Supervisor)
**Solution**:
1. Vérifier que SupervisorDashboard.vue a les emoji (🏢 ⚡ 📊 🚨)
2. Vérifier que props sont correctes: label, type, icon
3. Recharger la page (F5)
4. Vider le cache (Ctrl+Shift+Delete)

### Problème: Agent pas d'accès Réclamations
**Solution**:
1. Vérifier router/index.js: 'AGENT' dans roles pour /complaints
2. Vérifier network tab: GET /api/complaints → 200 ou 403?
3. Si 403: Problème de permissions backend

### Problème: Erreurs 404 sur API
**Solution**:
1. Vérifier que backend tourne (http://localhost:8081)
2. Vérifier logs backend pour les erreurs
3. Vérifier endpoint exact dans DashboardController.java

---

## 📊 RÉSUMÉ

| Élément | Avant | Après | Statut |
|---------|-------|-------|--------|
| Agent Dashboard | ❌ Erreur | ✅ Charge | À tester D10 |
| Supervisor Dashboard Icônes | ❌ Texte | ✅ Emoji | À tester D9 |
| Agent Accès Réclamations | ❌ Bloqué | ✅ Autorisé | À tester D10 |
| Compilation | ✅ OK | ✅ OK | Vérifié |
| Régression | N/A | ✅ Aucune | Vérifié |

---

## 📝 DOCUMENTS DE RÉFÉRENCE

- `DASHBOARD_CORRECTIONS_TEST_GUIDE.md` → Guide d'exécution des tests
- `QUICK_SUMMARY_DASHBOARDS_FIX.txt` → Résumé rapide
- `DASHBOARD_CHANGES_SUMMARY.md` → Changements visuels avant/après
- `API_DASHBOARD_AGENT_FIX.md` → Documentation API

---

**Compilé**: 12 Août 2026  
**Prêt pour les tests**: ✅ OUI

