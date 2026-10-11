# P9 - Power BI Architecture & Integration

## Executive Summary

Power BI integration architecture for STEG Smart Monitoring System provides:
- Pre-built database views for analytics
- Star schema data warehouse design
- Real-time data connectors
- KPI dashboards and reports
- Executive reporting capabilities
- Advanced analytics models

---

## 1. DATA ARCHITECTURE OVERVIEW

### Data Flow
```
Production Database (PostgreSQL)
    ↓
Analytics Views (Star Schema)
    ↓
Power BI Data Model
    ↓
Reports & Dashboards
    ├── Operational Dashboards
    ├── Executive Reports
    ├── KPI Scorecards
    └── Predictive Analytics
```

### Database Connection
- **Source**: PostgreSQL (production or replica)
- **Authentication**: Database user with read-only access
- **Encryption**: SSL/TLS for data in transit
- **Refresh**: Hourly or on-demand incremental refresh

---

## 2. STAR SCHEMA DESIGN

### Fact Tables (Events/Measurements)

#### FactMeasurements
```sql
CREATE TABLE fact_measurements (
    measurement_id BIGINT PRIMARY KEY,
    transformateur_dim_id BIGINT,
    poste_dim_id BIGINT,
    time_dim_id BIGINT,
    
    -- Metrics
    temperature DECIMAL(5,2),
    voltage DECIMAL(8,2),
    current DECIMAL(8,2),
    power DECIMAL(10,2),
    health_score DECIMAL(5,2),
    
    -- Status
    status VARCHAR(20),  -- NORMAL, WARNING, CRITICAL
    
    -- Facts
    measurement_date TIMESTAMP,
    created_at TIMESTAMP,
    
    FOREIGN KEY (transformateur_dim_id) REFERENCES dim_transformateur,
    FOREIGN KEY (poste_dim_id) REFERENCES dim_poste,
    FOREIGN KEY (time_dim_id) REFERENCES dim_time
);
```

#### FactAlerts
```sql
CREATE TABLE fact_alerts (
    alert_id BIGINT PRIMARY KEY,
    transformateur_dim_id BIGINT,
    poste_dim_id BIGINT,
    time_dim_id BIGINT,
    
    -- Alert Info
    title VARCHAR(255),
    level VARCHAR(20),  -- HIGH, MEDIUM, LOW
    type VARCHAR(50),   -- TEMPERATURE, VOLTAGE, HEALTH, etc.
    status VARCHAR(20), -- ACTIVE, ACKNOWLEDGED, RESOLVED
    
    -- Timeline
    created_at TIMESTAMP,
    acknowledged_at TIMESTAMP,
    resolved_at TIMESTAMP,
    
    -- Metrics
    resolution_hours DECIMAL(8,2),
    
    FOREIGN KEY (transformateur_dim_id) REFERENCES dim_transformateur,
    FOREIGN KEY (poste_dim_id) REFERENCES dim_poste,
    FOREIGN KEY (time_dim_id) REFERENCES dim_time
);
```

#### FactInterventions
```sql
CREATE TABLE fact_interventions (
    intervention_id BIGINT PRIMARY KEY,
    agent_dim_id BIGINT,
    transformateur_dim_id BIGINT,
    poste_dim_id BIGINT,
    time_dim_id BIGINT,
    
    -- Intervention Info
    reference VARCHAR(50),
    type VARCHAR(50),
    priority VARCHAR(20),  -- CRITICAL, HIGH, MEDIUM, LOW
    status VARCHAR(20),    -- PENDING, IN_PROGRESS, COMPLETED
    
    -- Timeline
    scheduled_date TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    duration_hours DECIMAL(8,2),
    
    -- Cost
    cost DECIMAL(10,2),
    
    FOREIGN KEY (agent_dim_id) REFERENCES dim_agent,
    FOREIGN KEY (transformateur_dim_id) REFERENCES dim_transformateur,
    FOREIGN KEY (poste_dim_id) REFERENCES dim_poste,
    FOREIGN KEY (time_dim_id) REFERENCES dim_time
);
```

#### FactComplaints
```sql
CREATE TABLE fact_complaints (
    complaint_id BIGINT PRIMARY KEY,
    client_dim_id BIGINT,
    transformateur_dim_id BIGINT,
    poste_dim_id BIGINT,
    time_dim_id BIGINT,
    
    -- Complaint Info
    reference VARCHAR(50),
    category VARCHAR(50),
    priority VARCHAR(20),
    status VARCHAR(20),
    
    -- Timeline
    filed_date TIMESTAMP,
    resolved_at TIMESTAMP,
    resolution_hours DECIMAL(8,2),
    
    -- Rating
    satisfaction_rating INT,  -- 1-5
    
    FOREIGN KEY (client_dim_id) REFERENCES dim_client,
    FOREIGN KEY (transformateur_dim_id) REFERENCES dim_transformateur,
    FOREIGN KEY (poste_dim_id) REFERENCES dim_poste,
    FOREIGN KEY (time_dim_id) REFERENCES dim_time
);
```

### Dimension Tables (Reference Data)

#### DimTransformateur
```sql
CREATE TABLE dim_transformateur (
    transformateur_dim_id BIGINT PRIMARY KEY,
    transformateur_id BIGINT,
    reference VARCHAR(50),
    poste_id BIGINT,
    manufacturer VARCHAR(100),
    model VARCHAR(100),
    power INT,
    voltage INT,
    installation_date DATE,
    status VARCHAR(20),
    
    -- Slowly Changing Dimension (Type 2)
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    is_current BOOLEAN
);
```

#### DimPoste
```sql
CREATE TABLE dim_poste (
    poste_dim_id BIGINT PRIMARY KEY,
    poste_id BIGINT,
    name VARCHAR(100),
    code VARCHAR(20),
    location VARCHAR(100),
    region VARCHAR(50),
    transformateur_count INT,
    
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    is_current BOOLEAN
);
```

#### DimAgent
```sql
CREATE TABLE dim_agent (
    agent_dim_id BIGINT PRIMARY KEY,
    agent_id BIGINT,
    name VARCHAR(100),
    email VARCHAR(100),
    role VARCHAR(50),
    status VARCHAR(20),
    
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    is_current BOOLEAN
);
```

#### DimClient
```sql
CREATE TABLE dim_client (
    client_dim_id BIGINT PRIMARY KEY,
    client_id BIGINT,
    name VARCHAR(100),
    email VARCHAR(100),
    region VARCHAR(50),
    account_type VARCHAR(50),
    
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    is_current BOOLEAN
);
```

#### DimTime
```sql
CREATE TABLE dim_time (
    time_dim_id BIGINT PRIMARY KEY,
    date_key INT,  -- YYYYMMDD format
    date DATE,
    year INT,
    quarter INT,
    month INT,
    week INT,
    day INT,
    day_of_week INT,
    day_name VARCHAR(10),
    month_name VARCHAR(10),
    is_weekend BOOLEAN,
    is_holiday BOOLEAN
);
```

---

## 3. ANALYTICS VIEWS

### V_MeasurementAnalytics
```sql
CREATE OR REPLACE VIEW v_measurement_analytics AS
SELECT 
    m.measurement_id,
    t.reference AS transformateur_ref,
    p.name AS poste_name,
    m.temperature,
    m.voltage,
    m.current,
    m.power,
    m.health_score,
    m.status,
    CASE 
        WHEN m.status = 'CRITICAL' THEN 1
        WHEN m.status = 'WARNING' THEN 0.5
        ELSE 0
    END AS risk_score,
    m.measurement_date,
    DATE_TRUNC('hour', m.measurement_date) AS hour_bucket,
    DATE(m.measurement_date) AS measurement_day,
    EXTRACT(HOUR FROM m.measurement_date) AS hour_of_day
FROM measurements m
JOIN transformateurs t ON m.transformateur_id = t.id
JOIN postes p ON t.poste_id = p.id
WHERE m.measurement_date >= CURRENT_DATE - INTERVAL '1 year';
```

### V_AlertAnalytics
```sql
CREATE OR REPLACE VIEW v_alert_analytics AS
SELECT 
    a.id AS alert_id,
    t.reference AS transformateur_ref,
    p.name AS poste_name,
    a.title,
    a.level,
    a.type,
    a.status,
    a.created_at,
    a.acknowledged_at,
    a.resolved_at,
    EXTRACT(EPOCH FROM (COALESCE(a.resolved_at, CURRENT_TIMESTAMP) - a.created_at)) / 3600.0 AS resolution_hours,
    CASE 
        WHEN a.level = 'HIGH' THEN 3
        WHEN a.level = 'MEDIUM' THEN 2
        WHEN a.level = 'LOW' THEN 1
    END AS severity_score,
    DATE(a.created_at) AS alert_date,
    EXTRACT(WEEK FROM a.created_at) AS alert_week
FROM alerts a
JOIN transformateurs t ON a.transformateur_id = t.id
JOIN postes p ON t.poste_id = p.id
WHERE a.created_at >= CURRENT_DATE - INTERVAL '1 year';
```

### V_InterventionAnalytics
```sql
CREATE OR REPLACE VIEW v_intervention_analytics AS
SELECT 
    i.id AS intervention_id,
    i.reference,
    ag.name AS agent_name,
    t.reference AS transformateur_ref,
    p.name AS poste_name,
    i.type,
    i.priority,
    i.status,
    i.date_planifiee AS scheduled_date,
    i.date_debut AS start_date,
    i.date_fin AS end_date,
    EXTRACT(EPOCH FROM (COALESCE(i.date_fin, CURRENT_TIMESTAMP) - i.date_debut)) / 3600.0 AS duration_hours,
    i.cost,
    CASE 
        WHEN i.status = 'COMPLETED' THEN 1
        ELSE 0
    END AS is_completed,
    DATE(i.date_planifiee) AS scheduled_day
FROM interventions i
JOIN users ag ON i.agent_id = ag.id
JOIN transformateurs t ON i.transformateur_id = t.id
JOIN postes p ON t.poste_id = p.id
WHERE i.date_planifiee >= CURRENT_DATE - INTERVAL '1 year';
```

### V_HealthScoreTrend
```sql
CREATE OR REPLACE VIEW v_health_score_trend AS
SELECT 
    t.id,
    t.reference,
    p.name AS poste_name,
    m.measurement_date,
    m.health_score,
    AVG(m.health_score) OVER (
        PARTITION BY t.id 
        ORDER BY m.measurement_date 
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
    ) AS health_score_7day_avg,
    AVG(m.health_score) OVER (
        PARTITION BY t.id 
        ORDER BY m.measurement_date 
        ROWS BETWEEN 29 PRECEDING AND CURRENT ROW
    ) AS health_score_30day_avg,
    CASE 
        WHEN m.health_score >= 80 THEN 'Excellent'
        WHEN m.health_score >= 60 THEN 'Good'
        WHEN m.health_score >= 40 THEN 'Fair'
        ELSE 'Poor'
    END AS health_status,
    LAG(m.health_score) OVER (
        PARTITION BY t.id 
        ORDER BY m.measurement_date
    ) AS previous_health_score
FROM measurements m
JOIN transformateurs t ON m.transformateur_id = t.id
JOIN postes p ON t.poste_id = p.id
WHERE m.measurement_date >= CURRENT_DATE - INTERVAL '90 days';
```

---

## 4. POWER BI DATA MODEL

### Relationships
```
FactMeasurements ─── DimTransformateur ─── DimPoste
     ├─────────────── DimTime
     └─────────────── DimStatus

FactAlerts ────────── DimTransformateur ─── DimPoste
    ├─────────────── DimTime
    └─────────────── DimAlertType

FactInterventions ─── DimAgent
    ├─────────────── DimTransformateur ─── DimPoste
    ├─────────────── DimTime
    └─────────────── DimPriority

FactComplaints ────── DimClient
    ├─────────────── DimTransformateur ─── DimPoste
    ├─────────────── DimTime
    └─────────────── DimCategory
```

---

## 5. KEY PERFORMANCE INDICATORS (KPIs)

### Equipment KPIs
1. **Average Health Score** - By poste, by region, trend
2. **Critical Assets Count** - Number of transformers below 40% health
3. **Equipment Uptime** - Percentage of operational time
4. **Mean Time Between Failures (MTBF)**
5. **Equipment Age Distribution**

### Operational KPIs
1. **Alert Volume** - Trend, by severity, by type
2. **Alert Resolution Time** - Average hours to resolve
3. **Intervention Count** - Completed, pending, by priority
4. **Intervention Duration** - Average hours to complete
5. **Intervention Cost** - Total, average, by type

### Customer KPIs
1. **Complaint Volume** - Filed, resolved, pending
2. **Complaint Resolution Time** - Average days to resolve
3. **Customer Satisfaction** - Average rating
4. **First Contact Resolution Rate**

### Performance KPIs
1. **Response Time** - Average time to respond to alerts
2. **Completion Rate** - Percentage of interventions completed on time
3. **Cost Per Intervention** - Average cost
4. **Employee Utilization** - Hours worked per agent

---

## 6. POWER BI REPORTS & DASHBOARDS

### Executive Dashboard
**Audience**: Management, C-Level

**Visualizations**:
- System Health Gauge (average health score)
- KPI Cards (critical alerts, pending interventions, resolution time)
- Equipment Status Distribution (pie chart)
- Alert Trend (line chart - 30 days)
- Top 5 Problem Postes (bar chart)
- Intervention Performance (completion rate gauge)

**Filters**: Date range, region, poste

---

### Operations Dashboard
**Audience**: Supervisors, Team Leads

**Visualizations**:
- Real-time Alert Matrix (type vs severity)
- Intervention Pipeline (by status)
- Agent Performance (interventions completed, average duration)
- Equipment Performance (health scores by poste)
- Response Time Trend
- Cost Analysis (budget vs actual)

**Filters**: Date, poste, agent, status

---

### Maintenance & Predictions Report
**Audience**: Maintenance Teams

**Visualizations**:
- Equipment Health Scorecard (by transformateur)
- Maintenance Schedule (upcoming interventions)
- Predictive Risk Matrix (risk level vs health score)
- Failure Prediction Alerts (high-risk equipment)
- Preventive vs Reactive Maintenance Ratio
- Equipment Lifecycle (installation date, age)

**Filters**: Health score threshold, risk level, age

---

### Customer Service Dashboard
**Audience**: Customer Service Team

**Visualizations**:
- Complaint Status Distribution
- Resolution Time by Category
- Satisfaction Rating Trend
- Complaint Timeline (created vs resolved)
- Top Issue Categories
- Customer Satisfaction KPI

**Filters**: Date, customer, category, status

---

## 7. INCREMENTAL REFRESH STRATEGY

### Refresh Schedule
```
Measurement Data:
  - Incremental refresh: Every 1 hour
  - Full refresh: Daily at 02:00 UTC

Alert Data:
  - Incremental refresh: Every 30 minutes
  - Full refresh: Daily at 03:00 UTC

Intervention Data:
  - Incremental refresh: Every 2 hours
  - Full refresh: Weekly (Sunday 02:00 UTC)

Complaint Data:
  - Incremental refresh: Every 4 hours
  - Full refresh: Weekly (Sunday 03:00 UTC)
```

### Query Performance Optimization
- Partitioning on `measurement_date` (monthly)
- Indexes on foreign keys
- Materialized views for complex aggregations
- Star schema denormalization for faster queries

---

## 8. SECURITY & ACCESS CONTROL

### Row-Level Security (RLS)
```
- Supervisors: See only their assigned postes
- Agents: See only transformateurs they service
- Clients: See only their own complaint data
- Admins: Full access
```

### Data Freshness & Latency
- Near real-time for measurements (1-hour lag)
- Real-time for alerts (30-minute lag)
- 4-hour lag for interventions and complaints
- Full historical data retained for 3 years

---

## 9. IMPLEMENTATION ROADMAP

### Phase 1: Foundation (Weeks 1-2)
- [ ] Create analytics schema and views
- [ ] Build dimension tables
- [ ] Deploy fact tables
- [ ] Test data quality

### Phase 2: Power BI Setup (Weeks 3-4)
- [ ] Create Power BI data model
- [ ] Build base reports
- [ ] Configure refresh schedule
- [ ] Set up RLS

### Phase 3: Advanced Analytics (Weeks 5-6)
- [ ] Implement predictive models
- [ ] Create advanced dashboards
- [ ] Performance tuning
- [ ] User training

### Phase 4: Production Deployment (Week 7)
- [ ] Deploy to production
- [ ] Monitor refresh performance
- [ ] User acceptance testing
- [ ] Go-live

---

## 10. MONITORING & MAINTENANCE

### Refresh Job Monitoring
- Track refresh duration and failures
- Alert if refresh takes >2 hours
- Log all refresh activities
- Monitor data staleness

### Query Performance Monitoring
- Track query execution times
- Identify slow queries
- Optimize indexes
- Monitor resource usage

### Data Quality Checks
- Validate row counts in fact tables
- Check for NULL values in key columns
- Monitor data consistency
- Alert on anomalies

---

## 11. SQL SCRIPTS FOR DEPLOYMENT

### Create Analytics Schema
```sql
CREATE SCHEMA analytics;

-- Create fact tables (see section 2 above)
-- Create dimension tables (see section 2 above)
-- Create views (see section 3 above)
-- Create indexes for performance

-- Grant permissions
GRANT SELECT ON ALL TABLES IN SCHEMA analytics TO powerbi_user;
```

### Create Refresh View
```sql
CREATE OR REPLACE VIEW v_last_refresh AS
SELECT 
    'measurements' AS table_name,
    MAX(measurement_date) AS last_data_date,
    CURRENT_TIMESTAMP AS last_refresh_time
FROM measurements
UNION ALL
SELECT 
    'alerts',
    MAX(created_at),
    CURRENT_TIMESTAMP
FROM alerts
UNION ALL
SELECT 
    'interventions',
    MAX(date_planifiee),
    CURRENT_TIMESTAMP
FROM interventions
UNION ALL
SELECT 
    'complaints',
    MAX(created_at),
    CURRENT_TIMESTAMP
FROM complaints;
```

---

## Summary

🟢 **P9 POWER_BI ARCHITECTURE - COMPLETE**

**Deliverables**:
- ✅ Star schema design with fact & dimension tables
- ✅ 5+ analytics views for reporting
- ✅ 4 role-based dashboards designed
- ✅ KPI definitions (15+ KPIs)
- ✅ Incremental refresh strategy
- ✅ Row-level security model
- ✅ Implementation roadmap (4 phases)
- ✅ SQL deployment scripts

**Ready for**: Final documentation and project completion.

