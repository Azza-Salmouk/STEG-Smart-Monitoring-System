# 7. POWER BI SETUP GUIDE

## Overview
Complete guide for connecting STEG Smart Monitoring System to Microsoft Power BI for business intelligence and reporting.

---

## Prerequisites

- Microsoft Power BI Desktop (latest version)
- Power BI Premium or Pro license
- PostgreSQL database with analytics views
- Network access to production database
- Read-only database user credentials

---

## Step 1: Prepare Database

### Create Analytics Schema
```sql
-- Connect as admin user
CREATE SCHEMA analytics;

-- Create fact and dimension tables (see P9_POWER_BI_ARCHITECTURE.md)
-- Create views for reporting
CREATE OR REPLACE VIEW v_measurement_analytics AS ...
CREATE OR REPLACE VIEW v_alert_analytics AS ...
CREATE OR REPLACE VIEW v_intervention_analytics AS ...
CREATE OR REPLACE VIEW v_complaint_analytics AS ...
CREATE OR REPLACE VIEW v_health_score_trend AS ...

-- Create read-only user for Power BI
CREATE USER powerbi_user WITH PASSWORD 'strong_password_here';
GRANT CONNECT ON DATABASE steg_production TO powerbi_user;
GRANT USAGE ON SCHEMA analytics TO powerbi_user;
GRANT SELECT ON ALL TABLES IN SCHEMA analytics TO powerbi_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA analytics GRANT SELECT ON TABLES TO powerbi_user;
```

### Verify Data Availability
```sql
-- Check record counts
SELECT 'Measurements' as table_name, COUNT(*) as count FROM measurements
UNION ALL
SELECT 'Alerts', COUNT(*) FROM alerts
UNION ALL
SELECT 'Interventions', COUNT(*) FROM interventions
UNION ALL
SELECT 'Complaints', COUNT(*) FROM complaints;

-- Check view access
SELECT * FROM analytics.v_measurement_analytics LIMIT 5;
SELECT * FROM analytics.v_alert_analytics LIMIT 5;
```

---

## Step 2: Connect to Database in Power BI

### In Power BI Desktop

1. **Open Power BI Desktop**
   - Launch application
   - Click "Get Data"

2. **Connect to PostgreSQL**
   - Select "PostgreSQL database"
   - Click "Connect"

3. **Enter Connection Details**
   ```
   Server: prod-db.example.com
   Database: steg_production
   Data connectivity mode: Import (or DirectQuery for real-time)
   ```

4. **Authentication**
   ```
   Select "Database" as authentication method
   Username: powerbi_user
   Password: [stored password]
   ```

5. **Select Tables/Views**
   - Navigate to `analytics` schema
   - Select all views and fact/dimension tables
   - Click "Load"

---

## Step 3: Build Data Model

### Relationships
In Power BI, create these relationships:

```
fact_measurements ──┬─→ dim_transformateur
                    ├─→ dim_poste
                    ├─→ dim_time
                    └─→ dim_status

fact_alerts ────────┬─→ dim_transformateur
                    ├─→ dim_poste
                    ├─→ dim_time
                    └─→ dim_alert_level

fact_interventions──┬─→ dim_agent
                    ├─→ dim_transformateur
                    ├─→ dim_poste
                    ├─→ dim_time
                    └─→ dim_priority

fact_complaints─────┬─→ dim_client
                    ├─→ dim_transformateur
                    ├─→ dim_poste
                    ├─→ dim_time
                    └─→ dim_category
```

**Creating Relationships**:
1. Right-click on fact table → "Manage relationships"
2. Click "New"
3. Select related tables and matching columns
4. Set relationship type (One-to-Many)
5. Set cross-filter direction (Both, if appropriate)

---

## Step 4: Create Measures

### Key Performance Indicators

#### Equipment Health
```DAX
-- Average Health Score
Avg_HealthScore = AVERAGE(fact_measurements[health_score])

-- Critical Equipment Count
Critical_Equipment = CALCULATE(
    DISTINCTCOUNT(fact_measurements[transformateur_id]),
    fact_measurements[health_score] < 40
)

-- Equipment Uptime Percentage
Equipment_Uptime = DIVIDE(
    CALCULATE(COUNTROWS(fact_measurements), fact_measurements[status] = "NORMAL"),
    COUNTROWS(fact_measurements)
) * 100
```

#### Alert Metrics
```DAX
-- Total Alerts
Total_Alerts = COUNTROWS(fact_alerts)

-- Active Alerts
Active_Alerts = CALCULATE(
    COUNTROWS(fact_alerts),
    fact_alerts[status] = "ACTIVE"
)

-- Average Resolution Time (hours)
Avg_Resolution_Time = AVERAGE(fact_alerts[resolution_hours])

-- High Priority Alerts
High_Priority_Alerts = CALCULATE(
    COUNTROWS(fact_alerts),
    fact_alerts[level] = "HIGH"
)
```

#### Operational Metrics
```DAX
-- Completed Interventions
Completed_Interventions = CALCULATE(
    COUNTROWS(fact_interventions),
    fact_interventions[status] = "COMPLETED"
)

-- Pending Interventions
Pending_Interventions = CALCULATE(
    COUNTROWS(fact_interventions),
    fact_interventions[status] = "PENDING"
)

-- Total Intervention Cost
Total_Intervention_Cost = SUM(fact_interventions[cost])

-- Avg Intervention Duration
Avg_Duration_Hours = AVERAGE(fact_interventions[duration_hours])
```

#### Customer Metrics
```DAX
-- Total Complaints
Total_Complaints = COUNTROWS(fact_complaints)

-- Resolved Complaints
Resolved_Complaints = CALCULATE(
    COUNTROWS(fact_complaints),
    fact_complaints[status] = "RESOLVED"
)

-- Complaint Resolution Rate %
Complaint_Resolution_Rate = DIVIDE(
    [Resolved_Complaints],
    [Total_Complaints]
) * 100

-- Avg Satisfaction Rating
Avg_Satisfaction = AVERAGE(fact_complaints[satisfaction_rating])
```

---

## Step 5: Create Dashboards

### Executive Dashboard

**Page Layout**:
- Row 1: System Health (Gauge), Critical Alerts (Card), Pending Work (Card)
- Row 2: Equipment Status Pie Chart, Alert Trend (Line), Top Issues (Bar)
- Row 3: Intervention Pipeline (Stacked Bar), Resolution Time (Gauge)

**Visualizations**:
1. **System Health Gauge**
   - Value: `Avg_HealthScore`
   - Target: 80
   - Ranges: Red <40, Yellow 40-70, Green 70-100

2. **KPI Cards**
   - `Active_Alerts`
   - `Pending_Interventions`
   - `Critical_Equipment`

3. **Alert Trend**
   - X-axis: `fact_alerts[created_at]` (by date)
   - Y-axis: `Total_Alerts` (count)
   - Trend line enabled

4. **Equipment Status**
   - Category: `dim_transformateur[status]`
   - Values: `DISTINCTCOUNT(transformateur_id)`
   - Pie chart

---

### Operations Dashboard

**Page Layout**:
- Row 1: Real-time Alerts, Intervention Pipeline, Team Performance
- Row 2: Trend charts, Maps (if location data available)
- Row 3: Tables with recent activity

**Filters (slicers)**:
- Date range
- Poste
- Status
- Priority

---

### Maintenance Dashboard

**Page Layout**:
- Row 1: Equipment Health Scorecard
- Row 2: Maintenance Schedule, Predictive Risk Matrix
- Row 3: Trending equipment, Failure predictions

**Visualizations**:
1. **Equipment Health Matrix**
   - Rows: Equipment Name
   - Values: Health Score (conditional formatting)
   - Column: Trend (last 30 days)

2. **Predictive Risk**
   - X-axis: Current Health Score
   - Y-axis: Failure Prediction Risk
   - Size: Alert Count
   - Color: Equipment Status

---

### Customer Service Dashboard

**Page Layout**:
- Row 1: Open Complaints, Resolution Time, Satisfaction
- Row 2: Complaint Trends, Category Distribution
- Row 3: Recent complaints table

**Key Metrics**:
- `Total_Complaints`
- `Resolved_Complaints`
- `Complaint_Resolution_Rate`
- `Avg_Satisfaction`

---

## Step 6: Publish to Power BI Service

### Deploy to Cloud

1. **Save Report**
   - File → Save
   - Name: "STEG_Smart_Monitoring"
   - Format: .pbix

2. **Publish to Service**
   - Home → Publish
   - Select workspace
   - Report published successfully

3. **Configure Data Refresh**
   - Power BI Service → Settings → Datasets
   - Select dataset
   - Scheduled refresh → Set schedule
   - Enable automatic refresh (hourly recommended)

### Configure Gateway (for on-premise database)

If database is on-premise:

1. **Download and Install Gateway**
   - https://powerbi.microsoft.com/en-us/gateway/
   - Install "Power BI Gateway - Enterprise"

2. **Configure Data Source**
   - Power BI Service → Settings → Gateways
   - Add gateway
   - Add data source: PostgreSQL, server, credentials

3. **Configure Refresh Schedule**
   - Scheduled refresh: Every 1 hour
   - Retry failed refreshes: Yes

---

## Step 7: Configure Row-Level Security (RLS)

### Implement Role-Based Filtering

```DAX
-- Supervisor can only see their region
[Supervisor_Filter] = 
IF(
    USERNAME() = "supervisor@steg.tn",
    RELATED(dim_poste[region]) IN "North",
    TRUE()
)

-- Agent can only see assigned transformers
[Agent_Filter] = 
IF(
    USERNAME() LIKE "*@agent*",
    RELATED(dim_transformateur[assigned_agent_email]) = USERNAME(),
    TRUE()
)
```

### Apply RLS in Power BI

1. Modeling → Manage Roles
2. Create role: "Supervisor"
3. Add filter:
   - Table: dim_poste
   - Column: region
   - Expression: `[region] = "North"`

4. Repeat for other roles (Agent, Client, Admin)

---

## Step 8: Monitor & Optimize

### Monitor Dashboard Usage

- Power BI Service → Usage metrics
- Track views, unique users, performance
- Identify slow reports

### Query Performance

```sql
-- Check slow queries on database
SELECT query, mean_exec_time FROM pg_stat_statements 
ORDER BY mean_exec_time DESC LIMIT 10;

-- Optimize by adding indexes
CREATE INDEX idx_measurements_date ON measurements(measurement_date DESC);
CREATE INDEX idx_alerts_status ON alerts(status);
```

### Data Refresh Health

- Check refresh logs: Settings → Datasets → Refresh history
- If refresh fails, check:
  - Database connectivity
  - Credentials validity
  - Firewall rules
  - Data source availability

---

## Step 9: Share & Collaborate

### Share Reports

1. **Share with Users**
   - Report → Share
   - Enter user emails
   - Grant permissions (View, Edit, etc.)

2. **Create App**
   - Publish as app for organization
   - Controlled distribution
   - Consistent branding

3. **Embed in Portal**
   - Get embed code
   - Embed in company website
   - Or integrate with Grafana

---

## Best Practices

### Performance
- Use DirectQuery for real-time, Import for historical
- Limit dimensions to necessary columns only
- Aggregate in database views, not in Power BI
- Partition fact tables by date if very large

### Security
- Use RLS for multi-tenant scenarios
- Never expose sensitive data in measures
- Mask email addresses, phone numbers
- Audit report access logs

### Data Quality
- Validate data in source system first
- Use data validation rules
- Monitor for anomalies
- Alert on significant changes

### User Experience
- Keep dashboards uncluttered (max 6 visualizations)
- Use consistent color schemes
- Add tooltips for clarity
- Test on mobile devices

---

## Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| "Unable to connect" | Verify PostgreSQL is accessible, check firewall rules |
| "Refresh failed" | Check gateway status, verify credentials, check data availability |
| "Slow performance" | Reduce data volume, use aggregations, add indexes |
| "No data displayed" | Verify RLS not too restrictive, check relationships, validate source data |
| "Permission denied" | Check RLS roles, verify user access, refresh gateway credentials |

---

## Maintenance Schedule

- **Daily**: Monitor refresh jobs, check error logs
- **Weekly**: Review usage metrics, optimize slow reports
- **Monthly**: Database statistics, analyze performance trends
- **Quarterly**: Update data models, refresh security reviews

