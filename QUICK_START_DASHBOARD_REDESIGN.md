# 🚀 QUICK START - DASHBOARD REDESIGN

**Date**: 19 Septembre 2026  
**Status**: ✅ Ready to Launch

---

## 📋 PRÉ-REQUIS

- Java 11+ (pour backend)
- Node.js 16+ (pour frontend)
- npm ou yarn
- PostgreSQL ou H2 (inclus)

---

## 🏃 DÉMARRAGE RAPIDE (5 minutes)

### Étape 1: Backend

```bash
# Option A: Using PowerShell (Windows)
cd c:\Users\ASUS\Downloads\projet-steg
$env:SPRING_PROFILES_ACTIVE='h2'
$env:JWT_SECRET='dev-secure-secret-key-for-testing-only-change-in-production'
java -jar smart-monitoring-backend/smart-monitoring-backend/target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar --server.port=8081

# Backend should start in ~20 seconds
# You should see: "Started SmartMonitoringBackendApplication"
# Then: "Demo data generation completed successfully!"
```

**Expected Output:**
```
o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8081 (http) with context path '/'
c.s.s.SmartMonitoringBackendApplication  : Started SmartMonitoringBackendApplication in 20.638 seconds
c.s.s.initializer.DataInitializer        : DEFAULT ADMIN CREATED
c.s.s.initializer.DemoDataGenerator      : Created 20 postes
c.s.s.initializer.DemoDataGenerator      : Created 149 transformateurs
c.s.s.initializer.DemoDataGenerator      : Created 4917 measurements
c.s.s.initializer.DemoDataGenerator      : Created 300 alerts
c.s.s.initializer.DemoDataGenerator      : Created 120 interventions
c.s.s.initializer.DemoDataGenerator      : Created 150 complaints
c.s.s.initializer.DemoDataGenerator      : Demo data generation completed successfully!
```

### Étape 2: Frontend (Nouveau Terminal)

```bash
cd c:\Users\ASUS\Downloads\projet-steg\smart-monitoring-frontend
npm run dev

# Frontend should start in ~5 seconds
# You should see: "Local:   http://localhost:5173/"
```

**Expected Output:**
```
  VITE v4.5.14  ready in 5391 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
  ➜  press h to show help
```

### Étape 3: Access Dashboard

1. **Open Browser**: http://localhost:5173
2. **Login Screen**: Should appear
3. **Credentials**:
   - Email: `admin@steg.tn`
   - Password: `Admin@123`

4. **Dashboard**: Should load and display data

---

## ✅ VÉRIFICATION INITIALE

### 1. Frontend Page Loads
```
✓ No blank page
✓ Login form visible
✓ No console errors (F12)
```

### 2. Login Works
```
✓ Enter admin@steg.tn / Admin@123
✓ Click Login button
✓ Page redirects to dashboard
```

### 3. Dashboard Displays
```
✓ "Vue globale de la supervision" header visible
✓ 4 KPI cards visible (Postes, Transformateurs, Health Score, Critical Alerts)
✓ Data loads from API (check Network tab F12)
✓ Graphs render correctly
```

### 4. No Errors
```
✓ Console (F12) has no red errors
✓ Network tab shows successful API calls
✓ All sections render without layout issues
```

---

## 📊 SECTIONS À VÉRIFIER

### Header
- [ ] Title: "Vue globale de la supervision"
- [ ] Subtitle: "Dernière actualisation: ..." (should update with time)
- [ ] Refresh button works

### KPI Cards (4 cards, should show values)
- [ ] **Postes**: 20
- [ ] **Transformateurs**: 149
- [ ] **Health Score**: 0-100%
- [ ] **Critical Alerts**: 0-300

### État Global du Parc
- [ ] Doughnut chart visible
- [ ] Labels: Normal (green), Avertissement (orange), Critique (red)
- [ ] Chart is interactive (hover shows tooltips)

### Indicateurs Critiques (4 colored boxes)
- [ ] Transformateurs Critiques (red)
- [ ] Alertes Actives (orange)
- [ ] Interventions En Cours (blue)
- [ ] Réclamations En Attente (purple)

### Alertes et Anomalies
- [ ] Count boxes: Critiques / Hautes / Moyennes / Infos
- [ ] List of recent alerts visible
- [ ] "Voir toutes les alertes" button

### Transformateurs à Surveiller
- [ ] List of critical transformers (if any)
- [ ] Reference, Health Score, Status visible

### Activité Récente (3 columns)
- [ ] Interventions recent list
- [ ] Complaints recent list
- [ ] Network statistics (Mesures, Taux résolution, MTTR)

### Distribution Charts
- [ ] Bar chart: Interventions by status
- [ ] Doughnut chart: Alerts by level

---

## 🔗 NAVIGATION TESTS

### Test Buttons
```bash
# In dashboard, click these buttons:
1. "Voir toutes les alertes" → Should go to /alerts page
2. "Voir les interventions" → Should go to /interventions page
3. "Voir les réclamations" → Should go to /complaints page
4. Click on sidebar items → Should work

# Try going back to dashboard → Should work
```

---

## 🧪 OTHER DASHBOARDS TEST

### Test Other Roles

```bash
# Logout (top right menu or /login)

# Login with SUPERVISOR
Email: supervisor@steg.tn
→ Check supervisor dashboard loads

# Login with AGENT
Email: agent@steg.tn
→ Check agent dashboard loads

# Login with CUSTOMER
Email: customer@steg.tn
→ Check customer dashboard loads

# All should have their specific dashboards
# No errors should appear
```

---

## 🐛 TROUBLESHOOTING

### Frontend Shows Blank Page
```
1. Check console (F12 → Console tab)
2. Look for errors
3. Check Network tab → API calls should be 200
4. If JWT error: Try logout and login again
```

### API Error 500
```
✓ This is EXPECTED without JWT
✓ Frontend automatically handles authentication
✓ Login will add JWT token to all requests
```

### Backend Won't Start
```
1. Check port 8081 is free: netstat -ano | findstr 8081
2. Kill existing process if needed
3. Make sure JAR exists: 
   smart-monitoring-backend/smart-monitoring-backend/target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar
4. Java 11+ must be installed: java -version
```

### Frontend Dev Server Issues
```
1. Port 5173 might be in use: netstat -ano | findstr 5173
2. Try: npm run dev -- --port 5174
3. Clear node_modules: rm -r node_modules && npm install
4. Make sure npm 8+: npm -v
```

### No Data Shows in Dashboard
```
1. Check you're logged in as admin
2. Check API call in Network tab (F12)
3. Check backend logs for errors
4. Try refresh button in dashboard
5. Check backend logs show demo data created
```

---

## 📱 RESPONSIVE DESIGN TEST

### Desktop (1920x1080)
```
✓ 4 KPI cards in one row
✓ 2 charts side by side
✓ 3 columns in activity section
✓ Good spacing
```

### Tablet (768x1024)
```
✓ 2 KPI cards per row
✓ Charts stack vertically
✓ 2 columns in activity section
```

### Mobile (375x667)
```
✓ 1 KPI card per row
✓ Charts full width
✓ 1 column in activity section
✓ Buttons clickable
✓ Text readable
```

---

## 🎨 COLOR VERIFICATION

### Alert Colors
- 🔴 **Critique** (Red): #EF4444
- 🟠 **Haute** (Orange): #F97316  
- 🟡 **Moyenne** (Yellow): #EAB308
- 🔵 **Basse** (Blue): #3B82F6

### KPI Card Borders (left side)
- **Postes**: Blue
- **Transformateurs**: Yellow
- **Health Score**: Green
- **Critical Alerts**: Red

### Status Box Colors
- **Critiques**: Red background (bg-red-50)
- **Actives**: Orange background (bg-orange-50)
- **En Cours**: Blue background (bg-blue-50)
- **En Attente**: Purple background (bg-purple-50)

---

## 📊 DATA VERIFICATION

### Expected Data Counts
```
Total: 5,656 records

Postes: 20
Transformateurs: 149
Measurements: 4,917 (24h)
Alerts: 300
Interventions: 120
Complaints: 150
Users: 4
```

### Check in H2 Console
```
URL: http://localhost:8081/h2-console

Driver: org.h2.Driver
JDBC URL: jdbc:h2:mem:steg_monitoring
Username: sa
Password: (empty)

Queries to verify:
SELECT COUNT(*) FROM poste;        -- 20
SELECT COUNT(*) FROM transformateur; -- 149
SELECT COUNT(*) FROM measurement;  -- 4917
SELECT COUNT(*) FROM alert;        -- 300
SELECT COUNT(*) FROM intervention; -- 120
SELECT COUNT(*) FROM complaint;    -- 150
SELECT COUNT(*) FROM "user";       -- 4
```

---

## 🔐 SECURITY CHECKLIST

- [ ] No credentials in frontend code
- [ ] No token visible in localStorage (should be in httpOnly cookie)
- [ ] No sensitive data in console logs
- [ ] CORS properly configured
- [ ] JWT validation working
- [ ] RBAC system intact

---

## 📈 PERFORMANCE CHECKLIST

- [ ] Dashboard loads in < 3 seconds
- [ ] No lag when scrolling
- [ ] Refresh button works smoothly
- [ ] Charts render quickly
- [ ] No memory leaks (keep browser open 5 mins, check memory)
- [ ] No 404 errors in Network tab

---

## 📝 LOGS TO CHECK

### Backend Logs (should contain)
```
✓ "DEFAULT ADMIN CREATED"
✓ "Created 20 postes"
✓ "Created 149 transformateurs"
✓ "Created 4917 measurements"
✓ "Created 300 alerts"
✓ "Created 120 interventions"
✓ "Created 150 complaints"
✓ "Demo data generation completed successfully!"
✓ No error messages
```

### Frontend Console (should be clean)
```
✓ No red error messages
✓ No warnings about missing components
✓ Network requests should be 200 OK
✓ May have blue info messages (Vue dev)
```

---

## 🎬 PRODUCTION BUILD (Optional)

```bash
# Build optimized version
cd smart-monitoring-frontend
npm run build

# Output in dist/ folder
# Files ready for deployment

# Preview build locally (optional)
npm run preview
# Access: http://localhost:4173/
```

---

## 📞 QUICK COMMANDS REFERENCE

```bash
# START BACKEND
$env:SPRING_PROFILES_ACTIVE='h2'
java -jar smart-monitoring-backend/smart-monitoring-backend/target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar --server.port=8081

# START FRONTEND
cd smart-monitoring-frontend && npm run dev

# BUILD FRONTEND
cd smart-monitoring-frontend && npm run build

# LINT FRONTEND
cd smart-monitoring-frontend && npm run lint

# Check ports in use
netstat -ano | findstr 8081  # Backend
netstat -ano | findstr 5173  # Frontend

# Kill process on port
Stop-Process -Id <PID> -Force
```

---

## 📍 IMPORTANT URLS

| Service | URL | Purpose |
|---------|-----|---------|
| Frontend | http://localhost:5173 | Main app |
| Backend API | http://localhost:8081/api | API calls |
| H2 Console | http://localhost:8081/h2-console | Database viewer |
| Login | http://localhost:5173/login | User login |
| Admin Dashboard | http://localhost:5173/dashboard | Main dashboard |
| Alerts Module | http://localhost:5173/alerts | Alerts management |
| Interventions | http://localhost:5173/interventions | Interventions |
| Complaints | http://localhost:5173/complaints | Complaints |

---

## 👤 TEST CREDENTIALS

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@steg.tn | Admin@123 |
| Supervisor | supervisor@steg.tn | (auto-generated) |
| Agent | agent@steg.tn | (auto-generated) |
| Customer | customer@steg.tn | (auto-generated) |

---

## ✨ WHAT'S NEW IN THIS DASHBOARD

### Before (Old Dashboard)
- ❌ Showed user login info (email, role)
- ❌ Displayed JWT token
- ❌ Only simple stats (user count, etc)
- ❌ No professional visualization
- ❌ No supervision focus

### After (New Dashboard)
- ✅ Professional supervision view
- ✅ 4 important KPIs with live data
- ✅ 3 interactive charts (Doughnut, Bar)
- ✅ Alert tracking by severity
- ✅ Critical transformers monitoring
- ✅ Recent activity tracking (interventions, complaints)
- ✅ Network statistics (resolution rate, MTTR)
- ✅ Modern responsive design
- ✅ Tailwind CSS styling
- ✅ Real-time data from API

---

## 🎯 SUCCESS CRITERIA

✅ You've succeeded when:
1. Dashboard loads without errors
2. 4 KPI cards display data (20 postes, 149 transformateurs, Health %, Alerts)
3. 3 charts render (Doughnut, Bar, Doughnut)
4. Buttons navigate to other pages
5. Other dashboards still work
6. No console errors
7. Mobile responsive works

---

**Ready to Launch?** ✅

Next Step: Open http://localhost:5173 in your browser!

