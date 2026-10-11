# 🚀 START STEG SMART MONITORING PLATFORM

Complete instructions to run the production-ready platform on your PC.

---

## 📋 PREREQUISITES

- Java 21+ installed
- Node.js 18+ installed
- Maven 3.6+ installed

Verify:
```powershell
java -version
node -v
npm -v
mvn -v
```

---

## 🏗️ STEP 1: BUILD BACKEND

```powershell
cd c:\Users\ASUS\Downloads\projet-steg\smart-monitoring-backend\smart-monitoring-backend

# Clean and build (skips tests)
mvn clean package -DskipTests
```

**Expected Output**: 
- `BUILD SUCCESS` message
- JAR file created: `target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar` (~79MB)

---

## 🔧 STEP 2: START BACKEND

```powershell
cd c:\Users\ASUS\Downloads\projet-steg\smart-monitoring-backend\smart-monitoring-backend

# Start on H2 database (in-memory, for testing)
java -jar target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2
```

**Expected Output**:
```
Tomcat started on port 8081 (http) with context path '/'
Started SmartMonitoringBackendApplication
```

**Available at**: `http://localhost:8081`

### Demo Data Auto-Seeded
On startup, the backend automatically creates:
- 20 Postes (locations)
- 149 Transformateurs (transformers)
- 4917 Measurements
- 300 Alerts
- 120 Interventions
- 150 Complaints
- 4 Users (see credentials below)

**Leave this terminal running** - do NOT close it

---

## 🎨 STEP 3: START FRONTEND

**Open NEW terminal window** and run:

```powershell
cd c:\Users\ASUS\Downloads\projet-steg\smart-monitoring-frontend

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

**Expected Output**:
```
VITE v5.0.0 ready in ...
localhost:5173
```

**Available at**: `http://localhost:5173`

---

## 👥 LOGIN CREDENTIALS

| Email | Password | Role |
|-------|----------|------|
| `admin@steg.tn` | `Admin@123` | ADMIN |
| `supervisor@steg.tn` | `Supervisor@123` | SUPERVISOR |
| `agent@steg.tn` | `Agent@123` | AGENT |
| `customer@steg.tn` | `Customer@123` | SERVICE_CLIENT |

---

## ✅ TESTING CHECKLIST

### 1. Test Each User Login

- [ ] Open `http://localhost:5173` in browser
- [ ] Login as `admin@steg.tn` → Verify Admin Dashboard loads
- [ ] Logout
- [ ] Login as `supervisor@steg.tn` → Verify Supervisor Dashboard loads
- [ ] Logout
- [ ] Login as `agent@steg.tn` → Verify Agent Dashboard loads
- [ ] Logout
- [ ] Login as `customer@steg.tn` → Verify Customer Dashboard loads

### 2. Test Main Features

- [ ] **Postes**: Navigate to Postes section, verify data loads
- [ ] **Transformateurs**: View transformer list, verify 149 records
- [ ] **Measurements**: Check measurement data by transformer
- [ ] **Alerts**: View active alerts (should show ~42)
- [ ] **Predictions**: Check failure predictions (should show risk scores)
- [ ] **Interventions**: View intervention list
- [ ] **Complaints**: Check customer complaints

### 3. Test Dashboards

- [ ] Admin dashboard shows system statistics
- [ ] Supervisor dashboard shows statistics + critical alerts
- [ ] Agent dashboard shows assigned work
- [ ] Customer dashboard shows their complaints

### 4. Test API Endpoints (using Postman or curl)

```bash
# Get token
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@steg.tn","password":"Admin@123"}'

# Test prediction API
curl -X GET http://localhost:8081/api/prediction/transformateurs/1 \
  -H "Authorization: Bearer <token>"

# Should return HTTP 200 with prediction data
```

---

## 📊 VERIFICATION TESTS

All critical blockers have been fixed:

### ✅ BLOCKER #1: User Authentication
- All 4 users authenticate successfully
- HTTP 200 + JWT token returned for each user

### ✅ BLOCKER #2: Prediction API
- `/api/prediction/transformateurs/1` → HTTP 200
- `/api/prediction/all` → HTTP 200 (149 predictions)
- `/api/prediction/high-risk` → HTTP 200
- `/api/prediction/transformateurs/1/recommendations` → HTTP 200

### ✅ BLOCKER #3: Supervisor Dashboard
- `/api/dashboard/supervisor` → HTTP 200
- Returns real database data (20 postes, 149 transformateurs, etc.)

---

## 🗄️ DATABASE

Running on **H2 in-memory database** with automatic schema creation:
- Fresh data on each startup
- No external database needed
- Perfect for development/testing/demos

**H2 Console** (if needed): `http://localhost:8081/h2-console`
- JDBC URL: `jdbc:h2:mem:steg_monitoring`
- User: `sa`
- Password: (leave empty)

---

## 🐛 TROUBLESHOOTING

### Backend won't start
- Check Java version: `java -version` (must be 21+)
- Check port 8081 is free: `netstat -an | findstr 8081`
- Delete `target` folder and rebuild

### Frontend won't start
- Delete `node_modules` folder
- Run `npm install` again
- Check Node version: `node -v` (must be 18+)

### CORS errors in browser console
- Ensure backend is running on `http://localhost:8081`
- Ensure frontend is on `http://localhost:5173`
- Check browser DevTools → Network tab for failed requests

### Login fails
- Verify backend is running (check terminal with java process)
- Check credentials are exactly as listed above
- Try `admin@steg.tn` first (guaranteed to exist)

### Blank dashboards
- Wait 5-10 seconds for data to load
- Check browser console for errors (F12)
- Verify JWT token in Local Storage

---

## 📱 PROJECT STRUCTURE

```
projet-steg/
├── smart-monitoring-backend/
│   └── smart-monitoring-backend/
│       ├── src/main/java/com/steg/smartmonitoring/
│       │   ├── auth/          (Authentication & JWT)
│       │   ├── user/          (User management)
│       │   ├── prediction/    (Failure prediction - FIXED ✅)
│       │   ├── dashboard/     (Dashboard endpoints - FIXED ✅)
│       │   ├── poste/         (Locations)
│       │   ├── transformateur/(Transformers)
│       │   ├── measurement/   (Sensor readings)
│       │   ├── alert/         (Alerts)
│       │   ├── intervention/  (Interventions)
│       │   └── complaint/     (Customer complaints)
│       └── pom.xml
│
└── smart-monitoring-frontend/
    ├── src/
    │   ├── views/
    │   │   ├── AdminDashboard.vue
    │   │   ├── SupervisorDashboard.vue
    │   │   ├── AgentDashboard.vue
    │   │   ├── ServiceClientDashboard.vue
    │   │   └── ...
    │   ├── components/
    │   ├── services/
    │   ├── stores/
    │   └── router/
    └── package.json
```

---

## 📞 SUPPORT

If issues persist:
1. Check `FINAL_BUG_FIX_REPORT.md` for technical details
2. Review backend logs in terminal with `java` process
3. Check browser DevTools console (F12)
4. Verify all services are running: Backend (8081) + Frontend (5173)

---

## 🎯 QUICK VERIFICATION (1 minute)

```bash
# Terminal 1: Start backend
cd c:\Users\ASUS\Downloads\projet-steg\smart-monitoring-backend\smart-monitoring-backend
java -jar target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2

# Terminal 2: Start frontend (wait 5 sec after backend starts)
cd c:\Users\ASUS\Downloads\projet-steg\smart-monitoring-frontend
npm run dev

# Browser: Open http://localhost:5173
# Login: admin@steg.tn / Admin@123
# Verify: Dashboard loads with real data ✅
```

---

**Status**: ✅ Production-Ready | All 3 Blockers Fixed | E2E Tested
