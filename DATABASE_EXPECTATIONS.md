# 📊 DATABASE EXPECTATIONS - STEG Smart Monitoring

**Based on entity definitions and DemoDataGenerator code**

---

## Table Definitions (Expected)

### 1. Users Table
```sql
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(100) NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL,  -- ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

-- Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- Expected demo data:
-- 4 users (1 ADMIN, 1 SUPERVISOR, 1 AGENT, 1 SERVICE_CLIENT)
```

### 2. Postes Table
```sql
CREATE TABLE postes (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(50) UNIQUE NOT NULL,
  nom VARCHAR(200) NOT NULL,
  region VARCHAR(100),
  adresse VARCHAR(300),
  latitude DECIMAL(10, 8),
  longitude DECIMAL(11, 8),
  type_poste VARCHAR(50),
  etat VARCHAR(20),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

-- Indexes
CREATE INDEX idx_postes_code ON postes(code);
CREATE INDEX idx_postes_nom ON postes(nom);
CREATE INDEX idx_postes_region ON postes(region);

-- Expected demo data:
-- 3 postes (Zone Nord, Zone Sud, Zone Est)
```

### 3. Transformateurs Table
```sql
CREATE TABLE transformateurs (
  id BIGSERIAL PRIMARY KEY,
  reference VARCHAR(50) UNIQUE NOT NULL,
  serial_number VARCHAR(100),
  manufacturer VARCHAR(100),
  model VARCHAR(100),
  poste_id BIGINT NOT NULL,
  capacity_kva DECIMAL(10, 2),
  voltage_kv DECIMAL(10, 2),
  current_a DECIMAL(10, 2),
  health_score INTEGER CHECK (health_score >= 0 AND health_score <= 100),
  statut VARCHAR(20),  -- OPERATIONAL, MAINTENANCE, OFFLINE
  risk_level VARCHAR(20),  -- LOW, MEDIUM, HIGH, CRITICAL
  critical BOOLEAN DEFAULT FALSE,
  installation_date DATE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  
  CONSTRAINT fk_transformateurs_postes
    FOREIGN KEY (poste_id) REFERENCES postes(id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_transformateurs_reference ON transformateurs(reference);
CREATE INDEX idx_transformateurs_poste_id ON transformateurs(poste_id);
CREATE INDEX idx_transformateurs_health_score ON transformateurs(health_score);
CREATE INDEX idx_transformateurs_status ON transformateurs(statut);

-- Expected demo data:
-- 10 transformateurs (distributed across postes)
```

### 4. Measurements Table
```sql
CREATE TABLE measurements (
  id BIGSERIAL PRIMARY KEY,
  transformateur_id BIGINT NOT NULL,
  temperature DECIMAL(8, 2),  -- °C
  voltage DECIMAL(12, 2),  -- Volts
  current DECIMAL(8, 2),  -- Amperes
  power DECIMAL(10, 2),  -- kW
  energy_consumption DECIMAL(12, 2),  -- kWh
  frequency DECIMAL(5, 2),  -- Hz
  humidity DECIMAL(5, 2),  -- %
  health_score INTEGER CHECK (health_score >= 0 AND health_score <= 100),
  status VARCHAR(20),  -- NORMAL, WARNING, CRITICAL, OFFLINE
  measurement_date TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  
  CONSTRAINT fk_measurements_transformateurs
    FOREIGN KEY (transformateur_id) REFERENCES transformateurs(id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_measurements_transformateur_id ON measurements(transformateur_id);
CREATE INDEX idx_measurements_measurement_date ON measurements(measurement_date DESC);
CREATE INDEX idx_measurements_status ON measurements(status);
CREATE INDEX idx_measurements_health_score ON measurements(health_score);
CREATE INDEX idx_measurements_temperature ON measurements(temperature);

-- Expected demo data:
-- 50+ measurements (5+ per transformateur)
-- Statuses: ~65% NORMAL, ~20% WARNING, ~10% CRITICAL, ~5% OFFLINE
-- Health scores: Random between 20-95
```

### 5. Alerts Table
```sql
CREATE TABLE alerts (
  id BIGSERIAL PRIMARY KEY,
  reference VARCHAR(50) UNIQUE,
  titre VARCHAR(255) NOT NULL,
  description TEXT,
  type VARCHAR(50),  -- MEASUREMENT_ANOMALY, MAINTENANCE_DUE, etc.
  niveau VARCHAR(20),  -- CRITICAL, WARNING, INFO
  statut VARCHAR(20),  -- ACTIVE, ACKNOWLEDGED, RESOLVED, ARCHIVED
  transformateur_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  
  CONSTRAINT fk_alerts_transformateurs
    FOREIGN KEY (transformateur_id) REFERENCES transformateurs(id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_alerts_transformateur_id ON alerts(transformateur_id);
CREATE INDEX idx_alerts_statut ON alerts(statut);
CREATE INDEX idx_alerts_niveau ON alerts(niveau);
CREATE INDEX idx_alerts_created_at ON alerts(created_at DESC);

-- Expected demo data:
-- 10-20 alerts (auto-generated from CRITICAL/WARNING measurements)
-- Statuses: ~50% ACTIVE, ~30% ACKNOWLEDGED, ~20% RESOLVED
```

### 6. Interventions Table
```sql
CREATE TABLE interventions (
  id BIGSERIAL PRIMARY KEY,
  reference VARCHAR(50) UNIQUE,
  type VARCHAR(50),
  description TEXT,
  statut VARCHAR(20),  -- PLANIFIEE, EN_COURS, COMPLETEE, ANNULEE
  priorite VARCHAR(20),  -- CRITICAL, HIGH, MEDIUM, LOW
  agent_id BIGINT,
  transformateur_id BIGINT NOT NULL,
  date_planifiee DATE,
  date_debut TIMESTAMP,
  date_fin TIMESTAMP,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  
  CONSTRAINT fk_interventions_users
    FOREIGN KEY (agent_id) REFERENCES users(id) ON DELETE SET NULL,
  CONSTRAINT fk_interventions_transformateurs
    FOREIGN KEY (transformateur_id) REFERENCES transformateurs(id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_interventions_agent_id ON interventions(agent_id);
CREATE INDEX idx_interventions_transformateur_id ON interventions(transformateur_id);
CREATE INDEX idx_interventions_statut ON interventions(statut);
CREATE INDEX idx_interventions_date_planifiee ON interventions(date_planifiee);

-- Expected demo data:
-- 15-20 interventions (2-3 per transformateur)
```

### 7. Complaints Table
```sql
CREATE TABLE complaints (
  id BIGSERIAL PRIMARY KEY,
  reference VARCHAR(50) UNIQUE,
  nom_client VARCHAR(100) NOT NULL,
  email VARCHAR(150),
  phone VARCHAR(20),
  description TEXT,
  categorie VARCHAR(50),  -- INSTALLATION, MAINTENANCE, PERFORMANCE, BILLING, OTHER
  statut VARCHAR(20),  -- OUVERTE, EN_COURS, RESOLVEE, FERMEE, REJETEE
  priorite VARCHAR(20),  -- CRITICAL, HIGH, MEDIUM, LOW
  supervisor_id BIGINT,
  transformateur_id BIGINT,
  poste_id BIGINT,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  
  CONSTRAINT fk_complaints_users
    FOREIGN KEY (supervisor_id) REFERENCES users(id) ON DELETE SET NULL,
  CONSTRAINT fk_complaints_transformateurs
    FOREIGN KEY (transformateur_id) REFERENCES transformateurs(id) ON DELETE SET NULL,
  CONSTRAINT fk_complaints_postes
    FOREIGN KEY (poste_id) REFERENCES postes(id) ON DELETE SET NULL
);

-- Indexes
CREATE INDEX idx_complaints_supervisor_id ON complaints(supervisor_id);
CREATE INDEX idx_complaints_transformateur_id ON complaints(transformateur_id);
CREATE INDEX idx_complaints_statut ON complaints(statut);

-- Expected demo data:
-- 5-10 complaints (diverse categories and statuses)
```

---

## Expected Record Counts (After Demo Data Generation)

```
After POST /api/demo/generate-data:

users:           4-5 records
  - 1 ADMIN
  - 1 SUPERVISOR
  - 1 AGENT
  - 1 SERVICE_CLIENT
  - [Optional] 1 Test User

postes:          3 records
  - Poste Zone Nord
  - Poste Zone Sud
  - Poste Zone Est

transformateurs: 10 records
  - 3-4 per poste
  - Health scores: random 20-95
  - Mix of statuses: OPERATIONAL, MAINTENANCE, OFFLINE

measurements:    50+ records
  - 5+ per transformateur
  - Dates: Last 30 days (simulated)
  - Statuses: 65% NORMAL, 20% WARNING, 10% CRITICAL, 5% OFFLINE

alerts:          10-20 records
  - Auto-generated from CRITICAL/WARNING measurements
  - 5-10 per transformateur on average
  - Statuses: Mix of ACTIVE, ACKNOWLEDGED, RESOLVED

interventions:   15-20 records
  - 1-2 per transformateur
  - Assigned to AGENT or SUPERVISOR
  - Statuses: Mix of PLANIFIEE, EN_COURS, COMPLETEE

complaints:      5-10 records
  - Various categories and statuses
  - Assigned to SUPERVISOR
  - May reference transformateur or poste
```

---

## Data Integrity Checks

### Foreign Key Relationships
```sql
-- All transformateurs linked to valid postes
SELECT COUNT(*) FROM transformateurs 
WHERE poste_id NOT IN (SELECT id FROM postes);
-- Expected: 0

-- All measurements linked to valid transformateurs
SELECT COUNT(*) FROM measurements 
WHERE transformateur_id NOT IN (SELECT id FROM transformateurs);
-- Expected: 0

-- All alerts linked to valid transformateurs
SELECT COUNT(*) FROM alerts 
WHERE transformateur_id NOT IN (SELECT id FROM transformateurs);
-- Expected: 0

-- All interventions agents are valid users
SELECT COUNT(*) FROM interventions 
WHERE agent_id IS NOT NULL AND agent_id NOT IN (SELECT id FROM users);
-- Expected: 0

-- All complaints supervisors are valid users
SELECT COUNT(*) FROM complaints 
WHERE supervisor_id IS NOT NULL AND supervisor_id NOT IN (SELECT id FROM users);
-- Expected: 0
```

### Unique Constraints
```sql
-- Check for duplicate codes
SELECT code, COUNT(*) FROM postes GROUP BY code HAVING COUNT(*) > 1;
-- Expected: 0 duplicates

-- Check for duplicate references
SELECT reference, COUNT(*) FROM transformateurs GROUP BY reference HAVING COUNT(*) > 1;
-- Expected: 0 duplicates

SELECT reference, COUNT(*) FROM alerts GROUP BY reference HAVING COUNT(*) > 1;
-- Expected: 0 duplicates (if reference used)

SELECT reference, COUNT(*) FROM interventions GROUP BY reference HAVING COUNT(*) > 1;
-- Expected: 0 duplicates (if reference used)
```

### Health Score Validation
```sql
-- Check health scores are in valid range [0, 100]
SELECT COUNT(*) FROM measurements WHERE health_score < 0 OR health_score > 100;
-- Expected: 0

SELECT COUNT(*) FROM transformateurs WHERE health_score < 0 OR health_score > 100;
-- Expected: 0
```

---

## Verification Queries

Run these after generating demo data:

```sql
-- Total summary
SELECT 
  (SELECT COUNT(*) FROM users) as user_count,
  (SELECT COUNT(*) FROM postes) as poste_count,
  (SELECT COUNT(*) FROM transformateurs) as transformateur_count,
  (SELECT COUNT(*) FROM measurements) as measurement_count,
  (SELECT COUNT(*) FROM alerts) as alert_count,
  (SELECT COUNT(*) FROM interventions) as intervention_count,
  (SELECT COUNT(*) FROM complaints) as complaint_count;

-- Expected output (approximately):
-- user_count | poste_count | transformateur_count | measurement_count | alert_count | intervention_count | complaint_count
-- 4-5        | 3           | 10                   | 50+               | 10-20       | 15-20              | 5-10

-- Status distribution
SELECT 
  statut,
  COUNT(*) as count
FROM transformateurs
GROUP BY statut;

-- Expected: Mix of OPERATIONAL, MAINTENANCE, OFFLINE

-- Health score distribution
SELECT 
  COUNT(*) as total,
  AVG(health_score) as avg_health,
  MIN(health_score) as min_health,
  MAX(health_score) as max_health
FROM measurements;

-- Expected: avg_health ~50-70, min ~20, max ~95
```

---

## Performance Considerations

### Query Performance
- Measurements table will grow quickly (new data every few seconds in production)
- Index on (transformateur_id, measurement_date DESC) is critical
- Consider partitioning measurements by date for large datasets

### Data Cleanup
- Implement data retention policy:
  - Keep measurements for 1 year
  - Archive older measurements
  - Auto-delete very old alerts

### Backup Strategy
- Daily backups recommended
- Point-in-time recovery essential for production

---

## Integration with Application

### Hibernate DDL Auto
```properties
spring.jpa.hibernate.ddl-auto=update
```
- On first startup: creates all tables
- On subsequent starts: applies schema changes
- Safe for development/staging
- Use `validate` in production (manual migrations)

### Demo Data Generation
```bash
# Generate demo data via API
POST http://localhost:8081/api/demo/generate-data
Authorization: Bearer {ADMIN_TOKEN}

# Response: "Demo data generated successfully"

# Verify
GET http://localhost:8081/api/transformateurs
# Returns: List of 10 demo transformateurs with complete data
```

### Seed Data Files
- No SQL files needed (Hibernate creates schema)
- Use DemoDataGenerator (Java code) or API endpoint
- Data persists after restart

---

## Production Considerations

### Migration from SQLite/H2 to PostgreSQL
- No code changes needed (JPA handles it)
- Only connection string changes
- Migrate via Hibernate or pg_dump

### Disaster Recovery
```sql
-- Backup database
pg_dump -U postgres steg_monitoring > backup.sql

-- Restore
psql -U postgres steg_monitoring < backup.sql
```

### Monitoring Queries
```sql
-- Table sizes
SELECT 
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname NOT IN ('pg_catalog', 'information_schema')
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Index usage
SELECT 
  indexrelname,
  idx_scan,
  idx_tup_read,
  idx_tup_fetch
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;
```

---

**Database Status**: ✅ Schema ready  
**Data Status**: ⏳ Awaiting PostgreSQL connection + generation  
**Verification**: Can be tested once database is online

