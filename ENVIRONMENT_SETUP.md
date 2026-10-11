# STEG Smart Monitoring - Environment Setup Guide

**Date:** August 12, 2026  
**Purpose:** Guide for setting up environment variables for local development and production deployment

---

## 🔐 SECURITY NOTICE

**CRITICAL:** The following secrets have been **COMPROMISED** and are no longer used:
- Old PostgreSQL username: `steg_admin`
- Old PostgreSQL password: `steg_password`
- Old JWT secret: `4fd86f2c89e0bcb54b4c37a90c2f7d4d5d7a6c8b9e1f0a...` (truncated)

These values were exposed in the Git repository history and **MUST NOT** be reused.

**Action Required:**
1. Database: If using PostgreSQL with these credentials, create a new database user
2. JWT: Generate a new secure JWT secret using a cryptographically secure method
3. Git History: Previous commits containing these secrets should be considered exposed

---

## 📋 Required Environment Variables

### Database Configuration

| Variable | Required | Description | Example |
|----------|----------|-------------|---------|
| `DB_URL` | Yes | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/steg_monitoring` |
| `DB_USERNAME` | Yes | PostgreSQL username | `your_db_user` |
| `DB_PASSWORD` | Yes | PostgreSQL password | `your_secure_password` |

### JWT Configuration

| Variable | Required | Description | Example |
|----------|----------|-------------|---------|
| `JWT_SECRET` | Yes (prod only) | JWT signing secret (minimum 64 chars) | See below |
| `JWT_EXPIRATION` | No | Token expiration in milliseconds | `86400000` (24 hours) |

---

## 🚀 Local Development Setup

### Method 1: Using .env File (Recommended for Development)

1. **Copy the template:**
   ```bash
   cp .env.example .env
   ```

2. **Edit .env with your local values:**
   ```bash
   # Edit with your preferred editor
   nano .env          # Linux/Mac
   type .env          # Windows (view only, use IDE to edit)
   ```

3. **Set environment variables:**

   **Linux/Mac:**
   ```bash
   export $(cat .env | xargs)
   # Or one by one:
   export DB_URL="jdbc:postgresql://localhost:5432/steg_monitoring"
   export DB_USERNAME="postgres"
   export DB_PASSWORD="postgres"
   export JWT_SECRET="dev-secret-key-NOT-FOR-PRODUCTION"
   ```

   **Windows (PowerShell):**
   ```powershell
   # Method 1: Manual export
   $env:DB_URL = "jdbc:postgresql://localhost:5432/steg_monitoring"
   $env:DB_USERNAME = "postgres"
   $env:DB_PASSWORD = "postgres"
   $env:JWT_SECRET = "dev-secret-key-NOT-FOR-PRODUCTION"

   # Method 2: Using a .ps1 script
   # Create Set-Env.ps1 with the above commands
   . .\Set-Env.ps1
   ```

   **Windows (CMD):**
   ```batch
   set DB_URL=jdbc:postgresql://localhost:5432/steg_monitoring
   set DB_USERNAME=postgres
   set DB_PASSWORD=postgres
   set JWT_SECRET=dev-secret-key-NOT-FOR-PRODUCTION
   ```

4. **Verify environment variables are set:**
   ```bash
   echo $DB_URL
   echo $DB_USERNAME
   echo $JWT_SECRET
   ```

5. **Start the backend:**
   ```bash
   cd smart-monitoring-backend/smart-monitoring-backend
   mvn clean test
   mvn spring-boot:run
   ```

### Method 2: IDE Configuration

**IntelliJ IDEA / VS Code:**
1. Right-click the project → Run → Edit Configurations
2. Add environment variables in the "Environment variables" field:
   ```
   DB_URL=jdbc:postgresql://localhost:5432/steg_monitoring;DB_USERNAME=postgres;DB_PASSWORD=postgres;JWT_SECRET=dev-secret
   ```
3. Run the application

---

## 🔑 Generating a Secure JWT Secret

The JWT secret must be a long, random string for production.

### Using OpenSSL (Linux/Mac)

```bash
# Generate a 64-character base64-encoded secret
openssl rand -base64 64

# Output example:
# 7x!A%D*G-JaNdRgUkXp2s5v8y/B?E(H+MbQfTjWnZr4u7x!A%D*G-JaNdRgUkXp2s5v8y/B?E(H+MbQ
```

### Using Python

```bash
python3 -c "import secrets; print(secrets.token_urlsafe(64))"

# Output example:
# eJydUMtuwjAM/BXLZ8QOCTuANEl7mHaZpv0gVuqFVjsmDkJ-fhvQpknbHrb93r19Xq8tJqxL
```

### Using Node.js

```bash
node -e "console.log(require('crypto').randomBytes(64).toString('base64'))"

# Output example:
# 3rK9mL2pQwXxYzAbCdEfGhIjKlMnOpQrStUvWxYzAbCdEfGhIjKlMnOpQrStUvWx
```

### Using Windows PowerShell

```powershell
[Convert]::ToBase64String((1..64 | ForEach-Object { [byte](Get-Random -Maximum 256) }))

# Output example:
# N7K2M3P5Q8R1S4T7U9V0W1X2Y3Z4A5B6C7D8E9F0G1H2I3J4K5L6M7N8O9P0Q1R2
```

**IMPORTANT:** Copy the output and use it as your `JWT_SECRET` environment variable.

---

## 🐳 Docker / Container Deployment

### Using Docker Compose

Create a `.env` file (not in Git):
```env
DB_URL=jdbc:postgresql://db:5432/steg_monitoring
DB_USERNAME=steg_user
DB_PASSWORD=secure_random_password_here
JWT_SECRET=your_secure_jwt_secret_here
```

Then in your `docker-compose.yml`:
```yaml
services:
  backend:
    environment:
      - DB_URL=${DB_URL}
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
    command: 
      - java
      - -jar
      - app.jar
      - --spring.profiles.active=prod
```

### Using Docker Secrets (Kubernetes/Swarm)

```bash
# Create secrets
docker secret create db_password - < /dev/stdin
docker secret create jwt_secret - < /dev/stdin

# Reference in service
docker service create \
  --secret db_password \
  --secret jwt_secret \
  -e DB_PASSWORD_FILE=/run/secrets/db_password \
  -e JWT_SECRET_FILE=/run/secrets/jwt_secret \
  steg-backend:latest
```

---

## ☁️ Cloud Platform Deployment

### AWS (Elastic Beanstalk / ECS)

1. **Via Environment Variables:**
   ```bash
   # AWS Console → Environment → Configuration → Software → Environment Properties
   DB_URL=jdbc:postgresql://prod-db.rds.amazonaws.com:5432/steg_monitoring
   DB_USERNAME=steg_user
   DB_PASSWORD=your_password
   JWT_SECRET=your_secret
   ```

2. **Via AWS Secrets Manager:**
   ```bash
   # Create secret
   aws secretsmanager create-secret --name steg/db-password --secret-string "your_password"
   
   # Reference in application.yml
   spring.datasource.password: ${aws:steg/db-password}
   ```

### Azure (App Service)

1. **Via Application Settings:**
   ```
   Portal → Configuration → Application Settings
   DB_URL
   DB_USERNAME
   DB_PASSWORD
   JWT_SECRET
   ```

2. **Via Azure Key Vault:**
   ```bash
   # Store secret
   az keyvault secret set --vault-name "steg-vault" --name "JwtSecret" --value "your_secret"
   ```

### Google Cloud (Cloud Run)

```bash
gcloud run deploy steg-backend \
  --set-env-vars="DB_URL=jdbc:postgresql://...,DB_USERNAME=user,DB_PASSWORD=pwd,JWT_SECRET=secret" \
  --image gcr.io/project/steg-backend:latest
```

---

## 🧪 Testing the Configuration

### Test Backend Startup

```bash
# Set environment variables first, then:
cd smart-monitoring-backend/smart-monitoring-backend

# Run tests
mvn clean test

# Expected output:
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS

# Package application
mvn clean package

# Run application
mvn spring-boot:run

# Expected output:
# ... Started SmartMonitoringBackendApplication in X seconds ...
# Spring Boot application is running on http://localhost:8081
```

### Test JWT Authentication

```bash
# Start backend and PostgreSQL first

# Test login endpoint
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# Expected response:
# {"accessToken": "eyJhbGciOiJIUzI1NiJ9...", "tokenType": "Bearer", ...}

# Test protected endpoint with token
BEARER_TOKEN="eyJhbGciOiJIUzI1NiJ9..."

curl -X GET http://localhost:8081/api/users \
  -H "Authorization: Bearer $BEARER_TOKEN"

# Expected: List of users (not 401 Unauthorized)
```

### Test Database Connection

```bash
# Check database connectivity
# Backend logs should show:
# HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@...
# HikariPool-1 - Start completed

# If connection fails, verify:
# 1. PostgreSQL is running
# 2. DB_URL is correct
# 3. DB_USERNAME and DB_PASSWORD are correct
```

---

## ⚠️ Troubleshooting

### Problem: "No value provided for parameter 'DB_URL'"
**Solution:** Environment variables not set. Run:
```bash
export DB_URL="jdbc:postgresql://..."
```

### Problem: "Unable to connect to PostgreSQL"
**Solution:** Check:
1. PostgreSQL is running: `psql -U postgres`
2. Database exists: `psql -l`
3. Connection string is correct
4. Credentials are correct

### Problem: "JWT secret not configured"
**Solution:** Set JWT_SECRET environment variable:
```bash
export JWT_SECRET="your-secret-here"
```

### Problem: "401 Unauthorized" on protected endpoints
**Solution:** 
1. Verify JWT token is in Authorization header: `Authorization: Bearer <token>`
2. Check JWT secret is correct (must be same as server)
3. Check token hasn't expired

### Problem: Frontend shows "Network Error"
**Solution:**
1. Verify backend is running: `curl http://localhost:8081`
2. Check CORS configuration in `SecurityConfig.java`
3. Verify frontend URL is in allowed origins

---

## 📝 Summary Checklist

- [ ] Copy `.env.example` to `.env`
- [ ] Update DB credentials in `.env`
- [ ] Generate secure JWT_SECRET
- [ ] Set environment variables
- [ ] Run `mvn clean test` ✅ (0 failures)
- [ ] Run `mvn spring-boot:run` ✅ (starts on 8081)
- [ ] Test login endpoint ✅ (returns JWT)
- [ ] Test protected endpoint ✅ (works with token)
- [ ] Frontend can access backend ✅
- [ ] Application is ready ✅

---

## 🔒 Security Checklist for Production

- [ ] JWT_SECRET is a truly random, long string (minimum 64 characters)
- [ ] Database password is strong and unique
- [ ] Environment variables are managed by secure secret storage
- [ ] .env file is NOT committed to Git
- [ ] .gitignore includes .env, *.env, *secrets*
- [ ] HTTPS is enabled on all endpoints
- [ ] Database backups are configured
- [ ] Application logs do not contain sensitive data
- [ ] Regular security audits are scheduled

---

**Need Help?** Refer to:
- `.env.example` - Environment variable template
- `application.properties` - Local dev configuration
- `application-prod.properties` - Production configuration template

