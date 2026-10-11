# 9. MONITORING & ALERTING GUIDE

## Overview
Comprehensive monitoring strategy for production operations.

---

## Key Metrics

### Application Health
- **Uptime**: 99.9% SLA target
- **Error Rate**: <0.1% of requests
- **Response Time**: p95 <500ms
- **Active Users**: Track concurrent sessions

### Infrastructure
- **CPU Usage**: Alert if >80%
- **Memory Usage**: Alert if >85%
- **Disk Space**: Alert if <15% free
- **Network**: Monitor bandwidth utilization
- **Database Connections**: Alert if >90% pool used

### Database
- **Query Time**: p95 <100ms
- **Connection Pool**: Monitor active/idle
- **Transaction Duration**: Track long-running
- **Replication Lag**: <1s for replicas
- **Backup Status**: Daily verification

### Business Metrics
- **Alert Volume**: Track trend
- **Intervention Completion Rate**: >95%
- **Customer Satisfaction**: Track complaints/ratings
- **System Health Score**: Average >75

---

## Monitoring Stack

### Prometheus (Metrics Collection)
```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'spring-boot'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/actuator/prometheus'

  - job_name: 'postgresql'
    static_configs:
      - targets: ['localhost:9187']  # postgres_exporter
```

### Grafana (Visualization)
```bash
# Install Grafana
docker run -d -p 3000:3000 grafana/grafana

# Default credentials: admin / admin

# Add Prometheus datasource
# Configuration → Data Sources → Prometheus
```

### ELK Stack (Log Aggregation)
```bash
# Elasticsearch, Logstash, Kibana
docker-compose up -d elasticsearch logstash kibana

# Configure Spring Boot to send logs
spring.cloud.stream.bindings.applicationMetrics.destination=spring-logs
```

---

## Alert Rules

### Prometheus Alerts
```yaml
groups:
  - name: steg_alerts
    interval: 30s
    rules:
      - alert: HighErrorRate
        expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.001
        for: 5m
        annotations:
          summary: "High error rate detected"

      - alert: HighCPUUsage
        expr: process_cpu_usage > 0.8
        for: 5m
        annotations:
          summary: "CPU usage above 80%"

      - alert: LowDiskSpace
        expr: disk_free_bytes < 0.15 * disk_total_bytes
        for: 5m
        annotations:
          summary: "Disk space below 15%"

      - alert: DatabaseConnectionPoolFull
        expr: hikaricp_connections_active / hikaricp_connections_max > 0.9
        for: 2m
        annotations:
          summary: "Database connection pool >90% utilized"

      - alert: HighQueryTime
        expr: pg_slow_queries_total > 10
        for: 5m
        annotations:
          summary: "Slow queries detected"
```

---

## Dashboards

### System Health Dashboard
- System uptime (gauge)
- Current active users (stat)
- API error rate (line chart)
- Response time distribution (histogram)
- Request volume (bar chart)

### Infrastructure Dashboard
- CPU usage (gauge)
- Memory usage (gauge)
- Disk space (gauge)
- Network traffic (line)
- Process count (stat)

### Database Dashboard
- Query performance (histogram)
- Connection pool utilization (gauge)
- Replication lag (gauge)
- Slow queries (table)
- Table sizes (bar)

### Business Dashboard
- Active alerts (card)
- Intervention completion (gauge)
- Avg resolution time (stat)
- Health score trend (line)
- Critical equipment (card)

---

## Health Check Endpoints

### Application Health
```
GET http://localhost:8081/api/health
Response:
{
  "status": "UP",
  "database": "UP",
  "diskSpace": "UP",
  "timestamp": "2024-12-01T16:00:00Z"
}
```

### Detailed Metrics
```
GET http://localhost:8081/actuator/metrics
```

### Custom Health Check
```java
@Component
public class CustomHealthIndicator extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            // Check critical business logic
            long transformateurCount = transformateurRepository.count();
            long alertCount = alertRepository.countByStatus("ACTIVE");
            
            if (alertCount > 1000) {
                builder.outOfService()
                    .withDetail("activeAlerts", alertCount);
            } else {
                builder.up()
                    .withDetail("transformateurs", transformateurCount)
                    .withDetail("activeAlerts", alertCount);
            }
        } catch (Exception e) {
            builder.down().withException(e);
        }
    }
}
```

---

## Log Monitoring

### Application Logging Configuration
```properties
# Logback configuration
logging.level.root=INFO
logging.level.com.steg=DEBUG
logging.level.org.springframework=WARN
logging.level.org.hibernate=WARN

# Log files
logging.file.name=logs/application.log
logging.file.max-size=10MB
logging.file.max-history=30

# JSON logging for easier parsing
spring.cloud.gcp.logging.enabled=true
```

### Key Events to Monitor
```bash
# Successful logins
grep -i "login successful" logs/application.log

# Authentication failures
grep -i "authentication failed\|invalid credentials" logs/application.log

# Database errors
grep -i "database\|sql error" logs/application.log

# Critical errors
grep -i "error\|exception" logs/application.log | head -50

# Performance warnings
grep -i "slow query\|timeout" logs/application.log
```

---

## Alerting Channels

### Email Alerts
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USER}
spring.mail.password=${MAIL_PASSWORD}

# Alert recipients
monitoring.alert.email=devops@steg.tn,oncall@steg.tn
```

### Slack Integration
```java
@Component
public class SlackAlertService {
    public void sendAlert(String message, String severity) {
        SlackMessage msg = SlackMessage.builder()
            .channel("#monitoring")
            .text(message)
            .color(getSeverityColor(severity))
            .build();
        
        restTemplate.postForObject(SLACK_WEBHOOK_URL, msg, Object.class);
    }
}
```

### PagerDuty Integration
```bash
# Configure PagerDuty in Prometheus
global:
  resolve_timeout: 5m

alerting:
  alertmanagers:
    - static_configs:
        - targets:
            - pagerduty-receiver:9093
```

---

## On-Call Schedule

### Escalation Policy
1. **L1 (First Hour)**: Primary on-call engineer
2. **L2 (First Hour+)**: Senior engineer if no response
3. **L3 (After 2 hours)**: Team lead/manager
4. **L4 (Critical)**: Director of operations

### Response Times
- P1 (Critical/Down): 15 minutes
- P2 (Major/Degraded): 1 hour
- P3 (Minor/Workaround): 4 hours
- P4 (Trivial): Business hours

---

## SLA Commitments

| Severity | Resolution Time | Escalation |
|----------|-----------------|------------|
| Critical | 1 hour | Immediate |
| High | 4 hours | 30 min delay |
| Medium | 8 hours | 1 hour delay |
| Low | 24 hours | 4 hour delay |

---

## Incident Response

### Detection
- Alert fires in monitoring system
- Notification sent to on-call team
- Slack/Email/SMS alert

### Response
1. Acknowledge alert (within 15 min)
2. Investigate root cause
3. Implement fix or workaround
4. Verify resolution
5. Document in incident log
6. Post-mortem analysis (P1 only)

### Post-Incident
- Root cause analysis document
- Action items with owners
- Process improvements
- Team debriefing

---

## Capacity Planning

### Monthly Reports
- Peak CPU/Memory usage
- Disk space growth trend
- Database size growth
- User growth trend
- API request volume trend

### Triggers for Expansion
- CPU consistently >70%
- Memory consistently >75%
- Disk space <30% free
- Database size >80% of allocated
- Request latency trend increasing

### Scaling Actions
- Vertical: Increase server resources
- Horizontal: Add load balancer, additional servers
- Database: Add read replicas, upgrade hardware
- Cache: Expand Redis memory, add caching layers

---

## Backup Monitoring

### Daily Verification
```bash
#!/bin/bash
# Check backup status
BACKUP_FILE="/backups/steg_$(date +%Y%m%d).sql"

if [ ! -f "$BACKUP_FILE" ]; then
    echo "ALERT: Backup missing for today" | mail -s "Backup Error" ops@steg.tn
    exit 1
fi

# Check backup size (should be >100MB)
BACKUP_SIZE=$(du -b "$BACKUP_FILE" | cut -f1)
if [ $BACKUP_SIZE -lt 104857600 ]; then
    echo "ALERT: Backup size suspiciously small" | mail -s "Backup Error" ops@steg.tn
    exit 1
fi

echo "Backup verified: $BACKUP_SIZE bytes"
```

### Disaster Recovery Drill
- Monthly: Restore from backup to test environment
- Verify data integrity
- Measure RTO/RPO
- Document process
- Update runbook if needed

---

## Monitoring Checklist

- [ ] Prometheus + Grafana deployed
- [ ] All alert rules configured
- [ ] Dashboards created and shared
- [ ] Log aggregation active
- [ ] Health check endpoints monitored
- [ ] On-call schedule published
- [ ] Alert routing configured
- [ ] SLA documented and acknowledged
- [ ] Incident response runbook created
- [ ] Monthly capacity review scheduled
- [ ] Backup verification automated
- [ ] Disaster recovery drill scheduled

