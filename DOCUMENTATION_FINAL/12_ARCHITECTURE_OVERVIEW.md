# 12. ARCHITECTURE OVERVIEW

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                              │
├─────────────────────────────────────────────────────────────────┤
│  Web Browser (Vue.js SPA) │ Mobile App │ Power BI │ Third-party │
└────────────────┬──────────────────────────────────────────────┬──┘
                 │                                              │
            HTTPS/SSL                                    HTTPS/SSL
                 │                                              │
┌────────────────▼──────────────────────────────────────────────▼──┐
│                      PRESENTATION LAYER                          │
├─────────────────────────────────────────────────────────────────┤
│  API Gateway (Load Balancer) │ CORS Handling │ Rate Limiting   │
└────────────────┬──────────────────────────────────────────────┬──┘
                 │                                              │
           TCP Port 443                                    TCP Port 443
                 │                                              │
┌────────────────▼──────────────────────────────────────────────▼──┐
│                     APPLICATION LAYER                            │
├─────────────────────────────────────────────────────────────────┤
│                     Spring Boot REST API                         │
│  ┌─────────────┬──────────────┬──────────────┬───────────────┐  │
│  │ Controllers │ Services     │ Repositories │ Validators    │  │
│  └─────────────┴──────────────┴──────────────┴───────────────┘  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ Business Logic: HealthScore, Anomaly Detection, Alerts  │  │
│  │ Intelligence: Prediction, Recommendations, Analytics    │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────┬──────────────────────────────────────────────┬──┘
                 │                                              │
          Connection Pool                               JWT Validation
          HikariCP (30)                                 Spring Security
                 │                                              │
┌────────────────▼──────────────────────────────────────────────▼──┐
│                    PERSISTENCE LAYER                             │
├─────────────────────────────────────────────────────────────────┤
│         Hibernate ORM │ JPA Repositories │ Named Queries        │
└────────────────┬──────────────────────────────────────────────┬──┘
                 │                                              │
          SQL Statements                              Parameterized Queries
                 │                                              │
┌────────────────▼──────────────────────────────────────────────▼──┐
│                     DATABASE LAYER                               │
├─────────────────────────────────────────────────────────────────┤
│  PostgreSQL 14+ (Primary) │ Read Replicas │ Backups │ Archives  │
│  ┌─────────────┬──────────────┬──────────────┬───────────────┐  │
│  │ Users       │ Postes       │ Measurements │ Alerts        │  │
│  │ Transform.  │ Interventions│ Complaints   │ Analytics     │  │
│  └─────────────┴──────────────┴──────────────┴───────────────┘  │
└────────────────┬──────────────────────────────────────────────┬──┘
                 │                                              │
          Daily Backups                                Replication
          Point-in-time Recovery                      WAL Archive
```

---

## Technology Stack

### Frontend
- **Framework**: Vue.js 3 (Composition API)
- **State Management**: Pinia
- **UI Components**: Tailwind CSS
- **Charts**: Chart.js
- **HTTP Client**: Axios
- **Build Tool**: Vite
- **Package Manager**: npm

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17
- **Database**: PostgreSQL 14+
- **ORM**: Hibernate/JPA
- **Authentication**: JWT + Spring Security
- **Cache**: Redis (optional)
- **Build Tool**: Maven
- **Logging**: Spring Logging

### Infrastructure
- **Web Server**: Nginx/Apache (reverse proxy)
- **Application Server**: Tomcat (embedded in Spring Boot)
- **Container**: Docker (optional)
- **Orchestration**: Kubernetes (optional)
- **Monitoring**: Prometheus + Grafana
- **Logging**: ELK Stack

### Analytics
- **Data Warehouse**: PostgreSQL Analytics Schema
- **BI Tool**: Power BI
- **ETL**: Native PostgreSQL Views

---

## Core Modules

### 1. Authentication & Authorization
```
Login → JWT Generation → Token Storage → Protected Endpoints
           ↓
    Spring Security Filter Chain
           ↓
    Role-Based Access Control (@PreAuthorize)
           ↓
    Method Security / Field Security
```

### 2. User Management
- Create/Read/Update/Delete users
- Role assignment
- Password management
- Audit logging

### 3. Poste Management
- Organizational unit representing electrical substations
- Contains multiple transformateurs
- Regional grouping

### 4. Transformateur Management
- Electrical equipment (transformers)
- Properties: power, voltage, status
- Health score calculation
- Maintenance tracking

### 5. Measurements
- Sensor data collection
- Parameters: temperature, voltage, current, power
- Real-time analysis
- Anomaly detection

### 6. Alerts
- Automated alert generation
- Multi-level severity (HIGH, MEDIUM, LOW)
- Acknowledgment & resolution tracking

### 7. Interventions
- Maintenance scheduling
- Agent assignment
- Status tracking (PENDING, IN_PROGRESS, COMPLETED)
- Cost tracking

### 8. Complaints
- Customer complaint management
- Status tracking
- Satisfaction rating
- Resolution tracking

### 9. Dashboards
- Role-based views:
  - Admin: System overview
  - Supervisor: Operational view
  - Agent: Personal focus
  - Customer: Self-service

### 10. Intelligence Layer
- Health Score: Sophisticated algorithm analyzing all metrics
- Anomaly Detection: Real-time pattern deviation detection
- Failure Prediction: ML-based risk scoring
- Recommendations: Proactive maintenance suggestions

---

## Data Flow

### Measurement Ingestion Flow
```
Sensor Data (IoT Device)
    ↓
POST /api/measurements
    ↓
MeasurementController
    ↓
MeasurementService
    ├─ Validate input
    ├─ Calculate health score
    ├─ Detect anomalies
    ├─ Generate alerts if needed
    └─ Store in database
    ↓
MeasurementRepository.save()
    ↓
PostgreSQL
    ↓
Dashboard (Real-time via WebSocket)
    ↓
Frontend UI Update
```

### Alert Generation Flow
```
New Measurement
    ↓
HealthScoreCalculator
    ├─ Temperature analysis
    ├─ Voltage analysis
    ├─ Current analysis
    └─ Power factor analysis
    ↓
Health Score < 40 ? → ALERT
    ↓
AlertService.create()
    ├─ Set severity based on thresholds
    ├─ Store in database
    ├─ Send email notification
    └─ Send Slack notification
    ↓
Dashboard Alert Widget
```

### Intervention Workflow
```
Alert or Scheduled Maintenance
    ↓
SupervisorDashboard
    ↓
Create Intervention
    ├─ Select transformer
    ├─ Assign to agent
    ├─ Set priority
    └─ Schedule date
    ↓
InterventionService
    ↓
Agent Notification
    ↓
Agent Accepts/Starts Work
    ↓
Update Status (IN_PROGRESS)
    ↓
Complete Work
    ├─ Add notes
    ├─ Update status (COMPLETED)
    └─ Add cost
    ↓
SupervisorDashboard (Performance metrics)
```

---

## Deployment Architecture

### Development Environment
```
Developer Laptop
├── Backend: Spring Boot (port 8081)
├── Frontend: Vite Dev Server (port 5173)
├── Database: H2 (in-memory) or PostgreSQL (local)
└── IDE: IntelliJ/VS Code
```

### Production Environment
```
Internet
    ↓
DNS
    ↓
CDN (Static Assets)
    ↓
Load Balancer (SSL Termination)
    ↓
├─ App Server 1 (Spring Boot on Tomcat)
├─ App Server 2 (Spring Boot on Tomcat)
└─ App Server 3 (Spring Boot on Tomcat)
    ↓
Connection Pool
    ↓
PostgreSQL Primary
    ├─ Read Replica 1
    ├─ Read Replica 2
    └─ Backup Server (streaming replication)
    ↓
Monitoring (Prometheus)
Logging (ELK)
Backups (S3)
```

---

## Security Architecture

### Authentication Flow
```
User Credentials
    ↓
POST /api/auth/login
    ↓
BCrypt Password Verification
    ↓
JWT Token Generation (RS256)
    ├─ Subject: user ID
    ├─ Role: user role
    ├─ Expiration: 1 hour
    └─ Refresh token: 7 days
    ↓
Token stored in Browser (localStorage)
    ↓
Every subsequent request includes token in Authorization header
```

### Authorization Flow
```
Protected Endpoint Request
    ↓
JWT Filter extracts token
    ↓
Token signature verification
    ↓
Token expiration check
    ↓
Extract user role from token
    ↓
@PreAuthorize evaluation
    ├─ hasRole('ADMIN')?
    ├─ hasAnyRole('ADMIN', 'SUPERVISOR')?
    └─ Custom expression evaluation
    ↓
Access Granted / Forbidden (403)
```

### Data Encryption
```
Data in Transit:
└─ HTTPS/TLS 1.3

Data at Rest:
├─ Database: pgcrypto (PostgreSQL encryption)
├─ Backup: GPG encryption
└─ Secrets: Environment variables (Vault)

Passwords:
└─ BCrypt (10 rounds)
```

---

## Scalability Considerations

### Horizontal Scaling
- Stateless application servers (easy to add/remove)
- Load balancer distributes traffic
- Session management via token-based JWT
- Database connection pooling

### Vertical Scaling
- Increase server resources (CPU, RAM)
- Increase database resources
- Optimize queries with indexes
- Implement caching (Redis)

### Database Scaling
- Read replicas for reporting queries
- Database sharding (if needed)
- Partitioning large tables by date
- Archive old data to separate storage

### Cache Strategy
- Redis for distributed caching
- HTTP caching headers for static assets
- Query result caching for analytics
- Session caching (if needed)

---

## Monitoring & Observability

### Metrics
- Application: Request count, response time, error rate
- JVM: Memory, GC, threads
- Database: Query time, connection pool, replication lag
- Infrastructure: CPU, memory, disk, network

### Logging
- Structured logging (JSON)
- Log levels: ERROR, WARN, INFO, DEBUG
- Centralized log aggregation (ELK)
- Log retention: 30 days hot, 1 year archive

### Tracing
- Distributed tracing (Spring Cloud Sleuth + Zipkin)
- Request correlation ID propagation
- Service-to-service trace visualization

### Alerting
- Threshold-based alerts (Prometheus)
- Anomaly detection
- Multiple notification channels (email, Slack, PagerDuty)
- Escalation policies

---

## High Availability & Disaster Recovery

### Redundancy
- Multiple application server instances
- Database replication (primary + standbys)
- Load balancer with health checks
- DNS failover capability

### Backup & Recovery
- Full database backup: daily (6-hour RPO)
- Incremental backups: WAL archiving
- Point-in-time recovery capability
- Multi-region backup copies

### Failover Procedures
- Automated health checks (5-minute interval)
- Manual failover to secondary database
- DNS update to new primary
- Application restart on backup servers

---

## Integration Points

### Third-Party Systems
- **Email**: SMTP for notifications
- **SMS**: SMS gateway for alerts (optional)
- **Slack**: Incident notifications
- **Power BI**: Business intelligence
- **ERP System**: Data synchronization (if applicable)

### APIs
- **REST API**: Primary integration method
- **WebSocket**: Real-time dashboard updates
- **Webhooks**: External system notifications

---

## Technology Rationale

| Component | Choice | Rationale |
|-----------|--------|-----------|
| Frontend | Vue 3 | Lightweight, reactive, easy to maintain |
| Backend | Spring Boot | Mature, secure, large ecosystem |
| Database | PostgreSQL | Reliable, ACID-compliant, open-source |
| Auth | JWT | Stateless, scalable, standard |
| Caching | Redis | Fast, in-memory, enterprise-grade |
| Monitoring | Prometheus + Grafana | Open-source, flexible, wide adoption |
| BI | Power BI | Rich visualizations, Excel integration |

---

## Future Enhancements

### Short-term (3-6 months)
- [ ] Multi-factor authentication
- [ ] Mobile app (native)
- [ ] Advanced search & filtering
- [ ] Custom reports builder
- [ ] Email templates customization

### Medium-term (6-12 months)
- [ ] Machine learning models for prediction
- [ ] Real-time IoT integration
- [ ] Blockchain for audit trail
- [ ] Microservices architecture
- [ ] Kubernetes orchestration

### Long-term (12+ months)
- [ ] AI-powered anomaly detection
- [ ] Edge computing for sensors
- [ ] Autonomous system optimization
- [ ] Federated learning
- [ ] Quantum-resistant encryption

---

## Architecture Principles

1. **Separation of Concerns**: Controllers, Services, Repositories
2. **DRY (Don't Repeat Yourself)**: Utilities, shared components
3. **SOLID Principles**: 
   - Single Responsibility: Each class has one reason to change
   - Open/Closed: Open for extension, closed for modification
   - Liskov Substitution: Substitutable interfaces
   - Interface Segregation: Specific interfaces
   - Dependency Inversion: Depend on abstractions
4. **Security-First**: Encryption, validation, RBAC
5. **Performance-First**: Indexes, caching, query optimization
6. **Resilience**: Error handling, timeouts, retries
7. **Observability**: Logging, metrics, traces

