# 2. DATABASE MIGRATION GUIDE

## Overview
Comprehensive guide for migrating from H2 (testing) to PostgreSQL (production).

---

## Phase 1: Pre-Migration Checklist

- [ ] Backup existing H2 database
- [ ] Verify PostgreSQL is running
- [ ] Create PostgreSQL database
- [ ] Create PostgreSQL user with permissions
- [ ] Test PostgreSQL connection
- [ ] Plan maintenance window
- [ ] Notify users of downtime

---

## Phase 2: PostgreSQL Setup

### Step 1: Create Database
```sql
CREATE DATABASE steg_production OWNER postgres;

-- Connect to the database
\c steg_production

-- Create schema
CREATE SCHEMA public;

-- Create user with permissions
CREATE USER steg_admin WITH PASSWORD 'SecurePass@123';

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE steg_production TO steg_admin;
GRANT ALL PRIVILEGES ON SCHEMA public TO steg_admin;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO steg_admin;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO steg_admin;
```

### Step 2: Create Tables via JPA
```bash
# Update application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/steg_production
spring.datasource.username=steg_admin
spring.datasource.password=SecurePass@123
spring.jpa.hibernate.ddl-auto=create

# Run Spring Boot to create tables
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Wait for application to start and create schema
# Then stop the application (Ctrl+C)
```

### Step 3: Switch to Validate Mode
```properties
# Update application.properties
spring.jpa.hibernate.ddl-auto=validate
```

---

## Phase 3: Data Migration

### Step 3.1: Export H2 Data
```bash
# Start application with H2 profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=h2"

# Keep running in background, use API to export data
# Or use database tools to export tables
```

### Step 3.2: Create Migration Script
```sql
-- Migration script: h2_to_postgres_migration.sql

-- Disable foreign key constraints temporarily
SET session_replication_role = REPLICA;

-- Migrate Users
INSERT INTO users (id, email, password, role, created_at, updated_at)
SELECT id, email, password, role, created_at, updated_at FROM h2_export.users;

-- Migrate Postes
INSERT INTO postes (id, name, code, location, region, created_at, updated_at)
SELECT id, name, code, location, region, created_at, updated_at FROM h2_export.postes;

-- Migrate Transformateurs
INSERT INTO transformateurs (id, reference, power, voltage, status, health_score, 
                              installation_date, poste_id, created_at, updated_at)
SELECT id, reference, power, voltage, status, health_score, 
       installation_date, poste_id, created_at, updated_at FROM h2_export.transformateurs;

-- Similar INSERT statements for other tables...
-- measurements, alerts, interventions, complaints

-- Re-enable foreign key constraints
SET session_replication_role = DEFAULT;

-- Update sequence values
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users) + 1);
SELECT setval('postes_id_seq', (SELECT MAX(id) FROM postes) + 1);
SELECT setval('transformateurs_id_seq', (SELECT MAX(id) FROM transformateurs) + 1);
-- etc. for all sequences
```

### Step 3.3: Execute Migration
```bash
# Via psql
psql -U steg_admin -d steg_production -f h2_to_postgres_migration.sql

# Or via REST API if available
# POST /api/migration/import-data with export file
```

---

## Phase 4: Validation & Testing

### Step 4.1: Data Validation Queries
```sql
-- Verify record counts
SELECT 'users' AS table_name, COUNT(*) AS count FROM users
UNION ALL
SELECT 'postes', COUNT(*) FROM postes
UNION ALL
SELECT 'transformateurs', COUNT(*) FROM transformateurs
UNION ALL
SELECT 'measurements', COUNT(*) FROM measurements
UNION ALL
SELECT 'alerts', COUNT(*) FROM alerts
UNION ALL
SELECT 'interventions', COUNT(*) FROM interventions
UNION ALL
SELECT 'complaints', COUNT(*) FROM complaints;

-- Verify data integrity
-- Check for orphaned records
SELECT * FROM transformateurs WHERE poste_id NOT IN (SELECT id FROM postes);
SELECT * FROM measurements WHERE transformateur_id NOT IN (SELECT id FROM transformateurs);

-- Check for NULL values in required fields
SELECT * FROM users WHERE email IS NULL OR password IS NULL;
SELECT * FROM transformateurs WHERE reference IS NULL;
```

### Step 4.2: Application Testing
```bash
# Start application with PostgreSQL profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Run test requests
# 1. Login with each role
# 2. View dashboards
# 3. Create new record
# 4. Update existing record
# 5. Delete record
# 6. Check health scores calculated correctly
# 7. Verify alerts generated properly
```

### Step 4.3: Performance Testing
```bash
# Load testing with 1000 concurrent users
# Test response times for:
# - Dashboard queries
# - Search operations
# - Report generation
# - Chart data loading
```

---

## Phase 5: Backup & Rollback

### Before Migration
```bash
# Backup PostgreSQL
pg_dump -U steg_admin steg_production > backup_pre_migration.sql

# Backup H2
# Copy h2.mv.db file to safe location
```

### After Migration (if needed)
```bash
# Restore PostgreSQL
psql -U steg_admin steg_production < backup_pre_migration.sql

# Or restore from backup file
```

---

## Phase 6: Production Cutover

### Maintenance Window Procedure
1. Stop all API requests (load balancer)
2. Stop application
3. Back up both databases
4. Run final migration verification
5. Start application with PostgreSQL
6. Run smoke tests
7. Re-enable API requests
8. Monitor closely for 24 hours

### Rollback Procedure (if issues)
1. Stop application
2. Restore H2 database from backup
3. Start application with H2 profile
4. Notify team
5. Investigate issues

---

## Connection Strings Reference

### Development (PostgreSQL)
```
jdbc:postgresql://localhost:5432/steg_dev
User: steg_dev
Password: dev_password
```

### Production
```
jdbc:postgresql://prod-db.example.com:5432/steg_production
User: steg_prod_user
Password: [from environment variable]
```

### Connection Pooling (HikariCP)
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000
```

---

## Monitoring Post-Migration

### Key Metrics to Track
- Database connection pool utilization
- Query execution times
- Transaction counts
- Error rates
- Disk space usage
- Memory usage
- CPU usage

### Alert Thresholds
- Connection pool: Alert if >80% utilized
- Query time: Alert if >5s
- Error rate: Alert if >1%
- Disk space: Alert if <10% free

---

## Common Migration Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| Foreign key constraint failed | Orphaned records | Clean data before migration |
| Sequence out of sync | Manual INSERT statements | Run setval() to reset |
| Character encoding issues | H2 vs PostgreSQL differences | Set UTF-8 encoding |
| Data type mismatch | Different type definitions | Verify schemas match |
| Performance degradation | Missing indexes | Run ANALYZE VACUUM |

---

## Database Configuration Files

### application-prod.properties
```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/steg_production}
spring.datasource.username=${DB_USER:steg_admin}
spring.datasource.password=${DB_PASSWORD:}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL10Dialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.generate_statistics=false
```

---

## Next Steps
1. Complete all migration phases
2. Perform final testing
3. Document any customizations
4. Update monitoring dashboards
5. Train support team on new database

