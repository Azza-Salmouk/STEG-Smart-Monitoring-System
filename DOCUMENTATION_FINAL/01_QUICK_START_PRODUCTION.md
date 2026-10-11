# 1. QUICK START - Production Deployment

## 5-Minute Setup Guide

### Prerequisites
- Java 17 or higher
- PostgreSQL 14+
- Node.js 18+
- npm or yarn
- Maven 3.8+

### Step 1: Clone & Navigate
```bash
cd c:\Users\ASUS\Downloads\projet-steg
```

### Step 2: Configure Database
```bash
# Create PostgreSQL database
createdb steg_production

# Set environment variables
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "steg_production"
$env:DB_USER = "steg_admin"
$env:DB_PASSWORD = "SecurePass@123"
$env:JWT_SECRET = "your-jwt-secret-key-min-32-chars"
```

### Step 3: Build Backend
```bash
cd smart-monitoring-backend/smart-monitoring-backend
mvn clean package -DskipTests -Dspring.profiles.active=prod
```

### Step 4: Start Backend
```bash
java -jar target/smart-monitoring-backend-1.0.0.jar --spring.profiles.active=prod
# Backend runs on: http://localhost:8081
```

### Step 5: Build Frontend
```bash
cd smart-monitoring-frontend
npm install
npm run build
```

### Step 6: Deploy Frontend
```bash
# Option A: Use npm preview (development)
npm run preview

# Option B: Deploy to web server (production)
# Copy dist/ folder to web server (Nginx, Apache, IIS)
```

### Step 7: Verify Deployment
```bash
# Test backend health
curl http://localhost:8081/api/health

# Test authentication
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@steg.tn","password":"Admin@123"}'

# Access frontend
# Open browser: http://localhost:5173 (dev) or http://your-domain.com (prod)
```

### Quick Test Credentials
- **Admin**: admin@steg.tn / Admin@123
- **Supervisor**: supervisor@steg.tn / Supervisor@123
- **Agent**: agent@steg.tn / Agent@123
- **Client**: client@steg.tn / Client@123

---

## Environment Profiles

### Development (H2 In-Memory)
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=h2"
```

### Development (PostgreSQL)
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Production
```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Port 8081 already in use | Change port: `--server.port=8082` |
| PostgreSQL connection failed | Check credentials in environment variables |
| Frontend can't reach backend | Verify CORS in `application.properties` |
| JWT token invalid | Ensure JWT_SECRET is consistent |
| Demo data not loading | Check database permissions |

---

## Health Checks

```bash
# Backend health endpoint
GET http://localhost:8081/api/health
Expected: { "status": "UP" }

# Database connectivity
GET http://localhost:8081/api/transformateurs?page=0&size=5
Expected: 200 OK with data

# Authentication
POST http://localhost:8081/api/auth/login
Expected: 200 OK with JWT token
```

---

## Next Steps
1. Review DATABASE_MIGRATION_GUIDE.md for data migration
2. Check DEPLOYMENT_CHECKLIST.md before production
3. Read SECURITY_HARDENING.md for security setup
4. See TROUBLESHOOTING_GUIDE.md if issues occur

