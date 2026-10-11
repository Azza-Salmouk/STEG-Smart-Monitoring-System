# 10. DISASTER RECOVERY PLAN

## Overview
Comprehensive disaster recovery procedures for STEG Smart Monitoring System.

---

## RTO & RPO Targets

- **RTO (Recovery Time Objective)**: 4 hours maximum downtime
- **RPO (Recovery Point Objective)**: 1 hour of data loss acceptable
- **Backup Frequency**: Every 6 hours
- **Test Frequency**: Monthly

---

## Backup Strategy

### Daily Full Backups
```bash
#!/bin/bash
# Full backup at 02:00 UTC daily
BACKUP_DIR="/backups/daily"
DATE=$(date +%Y%m%d_%H%M%S)

# PostgreSQL full backup
pg_dump -U steg_admin -d steg_production \
  --file="$BACKUP_DIR/steg_full_$DATE.sql"

# Compress
gzip "$BACKUP_DIR/steg_full_$DATE.sql"

# Upload to S3
aws s3 cp "$BACKUP_DIR/steg_full_$DATE.sql.gz" \
  s3://steg-backups/daily/

# Verify
if [ $? -eq 0 ]; then
    echo "Backup successful: steg_full_$DATE.sql.gz"
else
    echo "Backup FAILED" | mail -s "Backup Error" ops@steg.tn
fi
```

### Incremental Backups (WAL archiving)
```bash
# PostgreSQL WAL archiving for point-in-time recovery
# In postgresql.conf:
wal_level = replica
max_wal_senders = 3
wal_keep_size = 1GB

# Archive command:
archive_command = 'test ! -f /wal_archive/%f && cp %p /wal_archive/%f'

# Or upload to S3
archive_command = 'aws s3 cp %p s3://steg-backups/wal/%f'
```

### File System Backups
```bash
# Backup application configuration, custom code
tar -czf /backups/app_config_$(date +%Y%m%d).tar.gz \
  /opt/steg-app/config/ \
  /opt/steg-app/custom/ \
  /opt/steg-app/data/

# Upload to S3
aws s3 cp /backups/app_config_*.tar.gz s3://steg-backups/app-config/
```

---

## Backup Verification

### Automated Testing
```bash
#!/bin/bash
# Weekly backup restoration test
# Run on test server every Sunday 03:00 UTC

BACKUP_FILE=$(ls -t /backups/daily/*.sql.gz | head -1)
TEST_DB="steg_restore_test"

# Create test database
psql -U postgres -c "DROP DATABASE IF EXISTS $TEST_DB;"
psql -U postgres -c "CREATE DATABASE $TEST_DB;"

# Restore backup
gunzip -c "$BACKUP_FILE" | psql -U steg_admin -d "$TEST_DB"

# Verify data
TEST_RESULT=$(psql -U steg_admin -d $TEST_DB \
  -t -c "SELECT COUNT(*) FROM transformateurs;")

if [ $TEST_RESULT -gt 0 ]; then
    echo "Backup verification PASSED: $TEST_RESULT transformateurs"
else
    echo "Backup verification FAILED" | mail -s "Backup Error" ops@steg.tn
fi

# Cleanup
psql -U postgres -c "DROP DATABASE $TEST_DB;"
```

---

## Disaster Scenarios

### Scenario 1: Database Corruption

**Detection**:
- Application crashes on database query
- Table corruption error in logs

**Recovery (1 hour)**:
1. Stop application
2. Restore latest backup to clean database
3. Verify database integrity: `ANALYZE; VACUUM;`
4. Restart application
5. Monitor for data consistency

```bash
# Restore procedure
pg_restore -U steg_admin -d steg_production \
  --clean /backups/daily/steg_full_latest.sql.gz

# Verify
psql -U steg_admin -d steg_production -c "SELECT COUNT(*) FROM transformateurs;"
```

### Scenario 2: Application Server Failure

**Detection**:
- API endpoints return 500 error
- Application logs show connection errors

**Recovery (30 minutes)**:
1. Failover to secondary server (if configured)
2. Or: Restart application on new server
3. Deploy latest code version
4. Restore configuration files
5. Verify application health

```bash
# Deploy to new server
scp -r /opt/steg-app/config/* user@backup-server:/opt/steg-app/config/
ssh user@backup-server "cd /opt/steg-app && java -jar app.jar"
```

### Scenario 3: Complete Data Center Failure

**Detection**:
- All systems unreachable
- Network path failure

**Recovery (4 hours)**:
1. Alert team and activate disaster response
2. Provision new infrastructure in different region
3. Restore from latest backup to new database
4. Deploy application
5. Update DNS to point to new location
6. Verify all services operational

### Scenario 4: Network/Connectivity Loss

**Detection**:
- Frontend can't reach backend API
- Database replication lag >5 minutes

**Recovery (15 minutes)**:
1. Check network connectivity
2. Verify firewall rules
3. Check load balancer status
4. Restart networking components
5. Resume normal operation

---

## Failover Architecture

### Database Replication Setup
```sql
-- Primary server configuration
-- In postgresql.conf
wal_level = replica
max_wal_senders = 5
wal_keep_size = 2GB

-- Secondary server
-- Recovery signal file:
touch /var/lib/postgresql/standby.signal

-- pg_basebackup from primary
pg_basebackup -h primary-server -D /var/lib/postgresql/14/main \
  -U replication -v -P -W

-- Start as standby
systemctl start postgresql
```

### Application Load Balancer
```nginx
# Nginx configuration for failover
upstream app_servers {
    server app1.example.com:8081 weight=5;
    server app2.example.com:8081 weight=5 backup;
    server app3.example.com:8081 weight=5 backup;
}

server {
    listen 80;
    server_name api.steg-monitoring.tn;
    
    location / {
        proxy_pass http://app_servers;
        proxy_connect_timeout 5s;
        proxy_send_timeout 5s;
        proxy_read_timeout 5s;
    }
}
```

---

## Point-in-Time Recovery

### Using WAL Archives
```bash
# Restore to specific point in time
# 1. Stop application
# 2. Stop PostgreSQL
# 3. Remove current database cluster
rm -rf /var/lib/postgresql/14/main

# 4. Base restore from backup
pg_basebackup -h primary -D /var/lib/postgresql/14/main

# 5. Configure recovery
cat > /var/lib/postgresql/14/main/recovery.conf << EOF
restore_command = 'cp /wal_archive/%f "%p"'
recovery_target_time = '2024-12-01 14:30:00'
recovery_target_timeline = 'latest'
EOF

# 6. Start recovery
systemctl start postgresql

# 7. Monitor recovery progress
tail -f /var/log/postgresql/postgresql.log
```

---

## Testing & Drills

### Monthly DR Drill Checklist
- [ ] Verify latest backup exists and is valid
- [ ] Test backup restoration on test system
- [ ] Verify data integrity after restoration
- [ ] Test failover to secondary database
- [ ] Test application deployment to backup server
- [ ] Verify DNS failover process
- [ ] Test network failover procedures
- [ ] Document any issues or improvements
- [ ] Update runbooks based on findings
- [ ] Team debriefing

### Annual Full Disaster Test
- [ ] Simulate complete data center loss
- [ ] Deploy to new infrastructure
- [ ] Restore all systems from backups
- [ ] Measure actual RTO/RPO
- [ ] Validate business continuity
- [ ] Document lessons learned

---

## Communication Plan

### During Disaster
1. **First 5 minutes**: Declare incident
2. **Minutes 5-15**: Alert team, activate war room
3. **Minutes 15-60**: Provide updates every 15 min
4. **Every 30 min after**: Status updates to management

### Status Page
```bash
# Update status page
curl -X POST https://status.steg-monitoring.tn/api/incidents \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Database Outage",
    "status": "investigating",
    "message": "Working on resolution"
  }'
```

### Notification Recipients
- **Critical (P1)**: CEO, CTO, VP Ops, on-call team
- **High (P2)**: VP Ops, Team Lead, on-call team
- **Medium (P3)**: Team Lead, affected users
- **Low (P4)**: Team, internal notification

---

## Documentation

### Keep Updated
- [ ] Emergency contact list
- [ ] Runbooks for each disaster scenario
- [ ] Network diagrams
- [ ] Database architecture diagrams
- [ ] Backup location and access procedures
- [ ] DNS provider credentials
- [ ] Cloud provider credentials

### Store Securely
- Password manager or vault
- Multiple encrypted copies
- Off-site storage (different location)
- Printed copy in safe (for extreme scenarios)

---

## Post-Disaster Actions

### Within 1 Day
- [ ] Document timeline of events
- [ ] Collect all logs and diagnostics
- [ ] Notify auditors/compliance if necessary
- [ ] Brief leadership

### Within 1 Week
- [ ] Root cause analysis
- [ ] Action items and owners
- [ ] Update runbooks and procedures
- [ ] Review and update RTO/RPO targets
- [ ] Team debriefing meeting

### Within 1 Month
- [ ] Implement all action items
- [ ] Run full disaster recovery drill
- [ ] Update insurance/SLA documentation
- [ ] Training on lessons learned

---

## Disaster Recovery Checklist

**Preparation**:
- [ ] Backup strategy documented
- [ ] Backup testing automated
- [ ] Failover infrastructure ready
- [ ] DNS configured for failover
- [ ] Load balancer configured
- [ ] Database replication tested
- [ ] Runbooks created and trained

**Response Readiness**:
- [ ] On-call schedule active
- [ ] Escalation procedures documented
- [ ] Communication templates ready
- [ ] Status page configured
- [ ] Team trained on procedures

**Testing & Validation**:
- [ ] Monthly backup verification
- [ ] Quarterly disaster drill
- [ ] Annual full system test
- [ ] RTO/RPO verified

