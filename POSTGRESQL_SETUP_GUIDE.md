# PostgreSQL Setup Guide for STEG Smart Monitoring

## Overview

This guide explains how to set up PostgreSQL for the STEG Smart Monitoring system. The application supports three database configurations:

- **H2 (In-Memory)**: For testing and development (`application-h2.properties`)
- **PostgreSQL Dev**: For local development (`application-dev.properties`)
- **PostgreSQL Production**: For production deployments (`application-prod.properties`)

---

## 1. PostgreSQL Installation

### Windows

#### Using PostgreSQL Official Installer:
1. Download from https://www.postgresql.org/download/windows/
2. Run the installer and follow the setup wizard
3. Default port: 5432
4. Create a superuser account (default: `postgres`)
5. Remember the password

#### Using Chocolatey:
```powershell
choco install postgresql
```

#### Using Windows Subsystem for Linux (WSL):
```bash
sudo apt-get update
sudo apt-get install postgresql postgresql-contrib
sudo service postgresql start
```

### macOS

```bash
# Using Homebrew
brew install postgresql@15

# Start PostgreSQL service
brew services start postgresql@15

# Or start manually
pg_ctl -D /usr/local/var/postgres start
```

### Linux (Ubuntu/Debian)

```bash
sudo apt-get update
sudo apt-get install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

---

## 2. Create Database and User

Once PostgreSQL is installed and running:

```bash
# Connect to PostgreSQL as default user
psql -U postgres

# Inside psql, create database and user:
CREATE DATABASE steg_monitoring ENCODING 'UTF8' LC_COLLATE 'en_US.UTF-8' LC_CTYPE 'en_US.UTF-8';

CREATE USER steg_user WITH PASSWORD 'steg_password_123';

GRANT ALL PRIVILEGES ON DATABASE steg_monitoring TO steg_user;

ALTER ROLE steg_user CREATEDB;

\q
```

---

## 3. Verify Database Connection

```bash
# Test connection as the new user
psql -U steg_user -d steg_monitoring -h localhost
```

Should connect without errors.

---

## 4. Spring Boot Configuration

### For Local Development (PostgreSQL)

Run the backend with the `dev` profile:

```bash
# Using Maven
cd smart-monitoring-backend/smart-monitoring-backend
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Or build and run JAR
mvn clean package -DskipTests
java -jar target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --server.port=8081
```

### For Local Development (H2)

Run with `h2` profile instead:

```bash
java -jar target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2 --server.port=8081
```

### For Production

Set environment variables and use `prod` profile:

```bash
export DB_URL="jdbc:postgresql://prod-server:5432/steg_monitoring"
export DB_USERNAME="steg_user"
export DB_PASSWORD="your_secure_password"
export JWT_SECRET="your_long_random_jwt_secret_key"

java -jar target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --server.port=8081
```

---

## 5. Demo Data Migration

The application automatically creates tables via Hibernate (`spring.jpa.hibernate.ddl-auto=create-drop` for dev, `update` for prod).

Demo data is generated automatically on startup by `DataInitializer` class:
- 20 Postes
- 149 Transformateurs
- 4917 Measurements
- 300 Alerts
- 120 Interventions
- 150 Complaints
- Demo Users (ADMIN, SUPERVISOR, AGENT1, AGENT2, CLIENT)

### Verify Demo Data Created

```bash
psql -U steg_user -d steg_monitoring

SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM transformateurs;
SELECT COUNT(*) FROM measurements;
SELECT COUNT(*) FROM alerts;

\q
```

---

## 6. Database Tables Schema

The application uses JPA/Hibernate to auto-generate the following tables:

### Core Tables:
- `users` - User accounts with roles
- `postes` - Electrical substations
- `transformateurs` - Transformers
- `measurements` - Sensor measurements (temperature, voltage, current)
- `alerts` - Generated alerts
- `interventions` - Maintenance interventions
- `complaints` - Client complaints

### Relationships:
- Transformateurs ← Postes (many-to-one)
- Measurements ← Transformateurs (many-to-one)
- Alerts ← Transformateurs (many-to-one)
- Interventions ← Transformateurs (many-to-one)
- Complaints ← Transformateurs (many-to-one)

---

## 7. Backup and Restore

### Backup Database

```bash
pg_dump -U steg_user -d steg_monitoring -f backup_steg.sql
```

### Restore Database

```bash
createdb -U steg_user steg_monitoring_restored
psql -U steg_user -d steg_monitoring_restored -f backup_steg.sql
```

---

## 8. Common Issues and Troubleshooting

### Issue: "Connection refused" (localhost:5432)

**Solution:**
- Verify PostgreSQL is running: `sudo systemctl status postgresql`
- Start PostgreSQL: `sudo systemctl start postgresql`
- Check port: `sudo netstat -an | grep 5432`

### Issue: "Password authentication failed"

**Solution:**
- Verify credentials in `application-dev.properties`
- Reset user password:
  ```bash
  psql -U postgres -d postgres
  ALTER USER steg_user WITH PASSWORD 'new_password';
  \q
  ```

### Issue: Database does not exist

**Solution:**
- Create database first (see section 2)
- Verify database name matches in connection URL

### Issue: Slow queries on large datasets (4917+ measurements)

**Solution:**
- Create indexes:
  ```sql
  CREATE INDEX idx_measurements_transformateur ON measurements(transformateur_id);
  CREATE INDEX idx_measurements_date ON measurements(measurement_date);
  CREATE INDEX idx_alerts_transformateur ON alerts(transformateur_id);
  ```

---

## 9. Performance Tuning (Optional)

### For Development:
```sql
-- Analyze table statistics
ANALYZE measurements;
ANALYZE transformateurs;
ANALYZE alerts;
```

### For Large Datasets:
Adjust Spring JPA properties in `application-prod.properties`:

```properties
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.jdbc.fetch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
```

---

## 10. Docker Alternative

Run PostgreSQL in Docker:

```bash
docker run --name steg-postgres \
  -e POSTGRES_USER=steg_user \
  -e POSTGRES_PASSWORD=steg_password_123 \
  -e POSTGRES_DB=steg_monitoring \
  -p 5432:5432 \
  -d postgres:15
```

---

## 11. Connection URL Reference

| Environment | URL | Driver |
|---|---|---|
| Local Dev | `jdbc:postgresql://localhost:5432/steg_monitoring` | org.postgresql.Driver |
| Production | `jdbc:postgresql://{host}:{port}/{database}` | org.postgresql.Driver |
| H2 In-Memory | `jdbc:h2:mem:steg_monitoring` | org.h2.Driver |

---

## 12. Running Tests

H2 is still available for automated tests:

```bash
# Tests use H2 profile
mvn test

# Or with Maven Failsafe for integration tests
mvn verify
```

---

## Summary

✅ PostgreSQL is configured as the **default** database  
✅ H2 remains available for testing  
✅ Three profile configurations: dev (PostgreSQL), prod (PostgreSQL), h2 (H2)  
✅ Auto-schema creation via Hibernate  
✅ Demo data generated on startup  
✅ Production-ready with environment variable support  

For production deployment, ensure:
- PostgreSQL 12+ is installed
- `steg_monitoring` database exists
- Environment variables set (DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET)
- Prod profile activated: `--spring.profiles.active=prod`

