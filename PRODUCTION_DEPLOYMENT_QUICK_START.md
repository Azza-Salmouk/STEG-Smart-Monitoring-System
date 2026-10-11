# Production Deployment Quick Start Guide
**STEG Smart Monitoring - PostgreSQL Edition**

---

## Prerequisites

- Java 21 or higher
- PostgreSQL 12+ server running and accessible
- Environment variables configured (see below)

---

## Required Environment Variables

Before starting the application, set these environment variables in your production environment:

```bash
# PostgreSQL Database Configuration
export DB_URL="jdbc:postgresql://your-postgres-host:5432/steg_monitoring"
export DB_USERNAME="postgres_user"
export DB_PASSWORD="secure_password_here"

# JWT Configuration
export JWT_SECRET="your_strong_random_secret_min_32_chars"

# Optional (defaults provided)
export JWT_EXPIRATION="86400000"  # 24 hours in milliseconds
```

### On Windows (PowerShell):
```powershell
$env:DB_URL = "jdbc:postgresql://your-postgres-host:5432/steg_monitoring"
$env:DB_USERNAME = "postgres_user"
$env:DB_PASSWORD = "secure_password_here"
$env:JWT_SECRET = "your_strong_random_secret_min_32_chars"
```

### On Windows (CMD):
```cmd
set DB_URL=jdbc:postgresql://your-postgres-host:5432/steg_monitoring
set DB_USERNAME=postgres_user
set DB_PASSWORD=secure_password_here
set JWT_SECRET=your_strong_random_secret_min_32_chars
```

---

## Starting the Backend

### Option 1: Using Java Command (Recommended for Production)

```bash
java -jar smart-monitoring-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

**Server will start on port 8081** (configurable via environment)

### Option 2: Using Spring Boot Maven Plugin

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

---

## Database Initialization

The application will automatically validate the schema on startup. Ensure your PostgreSQL database exists:

```sql
-- Create database (if not exists)
CREATE DATABASE steg_monitoring;

-- Connect to database and run migrations (if applicable)
\c steg_monitoring
-- Application will auto-validate schema with ddl-auto=validate
```

---

## Deploying Frontend

1. Build the frontend application:
```bash
cd smart-monitoring-frontend
npm run build  # or yarn build / pnpm build
```

2. Deploy the `dist` folder to your web server:
```bash
# Copy dist folder to web server
# For Nginx, Apache, or cloud hosting (S3, Netlify, etc.)
```

3. Configure frontend to point to backend:
   - Update API base URL to point to your production backend
   - Ensure CORS is properly configured on backend for your frontend domain

---

## Health Check

After starting, verify the application is running:

```bash
# Without authentication
curl http://localhost:8081/api/health

# Login and get JWT token
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@steg.tn",
    "password": "Admin@123"
  }'

# Response should include JWT token:
# {"token": "eyJhbGc...", "user": {...}}
```

---

## Production Security Checklist

- [ ] HTTPS/SSL configured on backend (reverse proxy or load balancer)
- [ ] Firewall rules restrict access to backend (only from frontend/API clients)
- [ ] Database backups automated and tested
- [ ] JWT_SECRET is strong (minimum 32 characters, high entropy)
- [ ] Database user has limited permissions (not superuser)
- [ ] Logging configured and monitored
- [ ] Application monitoring enabled (APM, alerts)
- [ ] Rate limiting configured on API endpoints
- [ ] Audit logging enabled for sensitive operations

---

## Testing the Application

### Login Test
```bash
# Admin user
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@steg.tn", "password": "Admin@123"}'

# Supervisor user
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "supervisor@steg.tn", "password": "Supervisor@123"}'

# Agent user
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "agent@steg.tn", "password": "Agent@123"}'

# Customer user
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "customer@steg.tn", "password": "Customer@123"}'
```

### Verify Database Connection
```bash
# Get postes (should return list of power plants)
curl http://localhost:8081/api/postes \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Troubleshooting

### Database Connection Error
```
Error: Connection refused
Solution: 
1. Verify PostgreSQL is running
2. Check DB_URL, DB_USERNAME, DB_PASSWORD are correct
3. Ensure database exists: CREATE DATABASE steg_monitoring;
```

### JWT Secret Not Set
```
Error: JWT_SECRET not configured
Solution:
1. Set JWT_SECRET environment variable
2. Restart application
3. Verify: echo $JWT_SECRET (Linux/Mac) or echo %JWT_SECRET% (Windows)
```

### Port 8081 Already in Use
```
Solution:
java -jar smart-monitoring-backend-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --server.port=8082  # Use different port
```

---

## Logs Location

Application logs are output to console by default. For file logging, configure in `application-prod.properties`:

```properties
logging.file.name=./logs/application.log
logging.file.max-size=10MB
logging.file.max-history=30
```

---

## Stopping the Application

To gracefully stop the application:

```bash
# Send Ctrl+C in terminal (SIGTERM signal)
# Or use process manager to stop

# Find process:
lsof -i :8081  # Linux/Mac
netstat -ano | findstr :8081  # Windows

# Kill process (graceful):
kill -15 <PID>  # Linux/Mac
taskkill /PID <PID> /F  # Windows
```

---

## Performance Tuning

For production, consider:

1. **JVM Memory:** Allocate appropriate heap size
```bash
java -Xmx1g -Xms1g -jar smart-monitoring-backend-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

2. **Database Connection Pool:**
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

3. **Load Balancing:** Deploy multiple backend instances behind a load balancer

---

## Support & Monitoring

Monitor these metrics:
- API response times
- Database query performance
- JWT token validation latency
- Error rates by endpoint
- Database connection pool usage

---

**Deployment Status:** ✅ READY  
**Last Updated:** September 18, 2026  
**Application Version:** 0.0.1-SNAPSHOT  
**Database:** PostgreSQL (Production)
