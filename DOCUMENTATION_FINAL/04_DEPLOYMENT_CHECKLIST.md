# 4. DEPLOYMENT CHECKLIST

## Pre-Deployment Phase

### Infrastructure & Environment
- [ ] Production server provisioned (minimum 4 vCPU, 16GB RAM)
- [ ] PostgreSQL database server setup and tested
- [ ] SSL certificates obtained and configured
- [ ] Web server (Nginx/Apache) configured
- [ ] Load balancer configured (if applicable)
- [ ] Monitoring tools installed (Prometheus, ELK, etc.)
- [ ] Backup system configured and tested
- [ ] DNS records updated for production domain
- [ ] Firewall rules configured
- [ ] VPN access configured for team

### Environment Variables
- [ ] `DB_HOST` set correctly
- [ ] `DB_PORT` set correctly
- [ ] `DB_NAME` set correctly
- [ ] `DB_USER` set correctly
- [ ] `DB_PASSWORD` secured in vault (not hardcoded)
- [ ] `JWT_SECRET` generated (min 32 characters)
- [ ] `JWT_EXPIRATION` set (e.g., 3600)
- [ ] `REFRESH_TOKEN_EXPIRATION` set (e.g., 604800)
- [ ] `CORS_ALLOWED_ORIGINS` configured
- [ ] `LOG_LEVEL` set to INFO (not DEBUG)
- [ ] `MAIL_SERVER` configured (for alerts)
- [ ] `MAIL_FROM` configured

### Database
- [ ] PostgreSQL 14+ installed and running
- [ ] Database created with correct encoding (UTF-8)
- [ ] Database user created with limited permissions
- [ ] Backup user created for automated backups
- [ ] Connection pooling configured (HikariCP)
- [ ] Max pool size set to 20-30
- [ ] Connection timeout configured
- [ ] Idle timeout configured
- [ ] Indexes created for performance
- [ ] Statistics analyzed (ANALYZE, VACUUM)
- [ ] Initial schema created via JPA migration

### Security
- [ ] SSL/TLS certificate installed
- [ ] HTTPS enforced (redirect HTTP to HTTPS)
- [ ] CORS headers properly configured
- [ ] Security headers configured (CSP, X-Frame-Options, etc.)
- [ ] CSRF protection enabled
- [ ] Rate limiting configured
- [ ] Input validation enabled
- [ ] SQL injection protection verified
- [ ] XSS protection verified
- [ ] Authentication endpoints protected
- [ ] Authorization rules verified
- [ ] Password requirements enforced
- [ ] BCrypt hashing verified (10 rounds)
- [ ] Sensitive data not logged

### Application Build
- [ ] Backend compiled: `mvn clean package -DskipTests`
- [ ] Build successful with no errors
- [ ] Build size within limits
- [ ] Frontend built: `npm run build`
- [ ] Frontend build successful
- [ ] Dist folder created without errors
- [ ] No console errors or warnings
- [ ] Bundle size analyzed and optimized
- [ ] Source maps configured (or removed for security)

### Testing
- [ ] Unit tests pass: `mvn test`
- [ ] Integration tests pass
- [ ] Security tests pass
- [ ] API endpoint tests pass
- [ ] Database migration tested
- [ ] Performance tests completed
- [ ] Load tests completed (at least 100 concurrent users)
- [ ] Failover tests completed
- [ ] Backup/restore tested
- [ ] SSL certificate verified
- [ ] DNS resolution verified

### Documentation
- [ ] Deployment guide reviewed
- [ ] Troubleshooting guide prepared
- [ ] Runbook created
- [ ] Architecture diagram updated
- [ ] Database schema documented
- [ ] API documentation generated
- [ ] Team trained on deployment
- [ ] Rollback procedure documented
- [ ] Incident response plan ready

---

## Deployment Phase

### Backend Deployment
- [ ] Stop current backend (if upgrading)
- [ ] Backup current application state
- [ ] Copy WAR/JAR to deployment directory
- [ ] Set environment variables
- [ ] Start application with production profile
- [ ] Wait for startup logs to confirm readiness
- [ ] Check health endpoint: `GET /api/health`
- [ ] Verify database connectivity
- [ ] Check application logs for errors
- [ ] Verify demo data loaded (if needed)

### Frontend Deployment
- [ ] Upload dist folder to web server
- [ ] Configure web server (Nginx/Apache)
- [ ] Set up routing (SPA configuration)
- [ ] Configure compression (gzip)
- [ ] Configure caching headers
- [ ] Verify frontend loads correctly
- [ ] Test all routes accessible
- [ ] Verify API calls reach backend
- [ ] Check browser console for errors
- [ ] Test on different browsers (Chrome, Firefox, Safari, Edge)

### Database Deployment
- [ ] Create database backup
- [ ] Run schema migrations (if any)
- [ ] Verify schema created successfully
- [ ] Run data migrations (if any)
- [ ] Validate data integrity
- [ ] Create required views
- [ ] Create required indexes
- [ ] Verify foreign key constraints

### Monitoring & Logging
- [ ] Application logs streaming to centralized log service
- [ ] Database logs monitored
- [ ] Error tracking configured (e.g., Sentry)
- [ ] Performance monitoring configured (APM)
- [ ] Alert thresholds configured
- [ ] Health check monitoring active
- [ ] Disk space monitoring active
- [ ] Memory usage monitoring active
- [ ] CPU usage monitoring active
- [ ] Network monitoring active

---

## Post-Deployment Phase

### Smoke Tests (First Hour)
- [ ] Backend responds to requests
- [ ] Frontend loads without errors
- [ ] Login works with all test credentials
- [ ] Can create, read, update, delete transformateur
- [ ] Dashboard loads and displays data
- [ ] Alerts generate correctly
- [ ] Interventions can be created
- [ ] Complaints can be filed
- [ ] API rate limiting works
- [ ] Error messages display correctly

### System Verification (First Day)
- [ ] Performance baseline established
- [ ] No error spikes in logs
- [ ] Database queries performing well
- [ ] Frontend responsive (<3 seconds load time)
- [ ] API response times <500ms
- [ ] User sessions created successfully
- [ ] Role-based access control working
- [ ] Data consistency verified
- [ ] Backup job completed successfully
- [ ] Email notifications working (if configured)

### Production Monitoring (First Week)
- [ ] No unhandled exceptions
- [ ] Database connections stable
- [ ] Memory usage stable
- [ ] CPU usage acceptable
- [ ] Network bandwidth acceptable
- [ ] Disk space not filling up
- [ ] Backup jobs running on schedule
- [ ] No security alerts
- [ ] User feedback positive
- [ ] Performance stable

### User Acceptance Testing
- [ ] Admin user can manage all features
- [ ] Supervisor can manage assignments
- [ ] Agent can view and complete work
- [ ] Service Client can file complaints
- [ ] All role-based features working
- [ ] Data exports working
- [ ] Reports generating correctly
- [ ] No major bugs reported

---

## Deployment Rollback Procedures

If critical issues discovered:

### Immediate Actions
- [ ] Alert team of issue
- [ ] Stop serving traffic to new version
- [ ] Enable previous version (or maintenance page)
- [ ] Investigate root cause
- [ ] Document issue and timeline

### Rollback Steps
1. [ ] Stop current backend application
2. [ ] Restore database from pre-deployment backup
3. [ ] Revert to previous application version
4. [ ] Start application with previous version
5. [ ] Verify health checks passing
6. [ ] Verify data integrity
7. [ ] Resume traffic to rollback version
8. [ ] Communicate status to users

### Post-Rollback
- [ ] Root cause analysis completed
- [ ] Fix implemented and tested
- [ ] Deployment re-scheduled
- [ ] Team debriefing conducted
- [ ] Documentation updated

---

## Production Maintenance

### Daily Tasks
- [ ] Check application health status
- [ ] Review error logs
- [ ] Monitor performance metrics
- [ ] Verify backup completion
- [ ] Check disk space usage

### Weekly Tasks
- [ ] Review security logs
- [ ] Analyze performance trends
- [ ] Update dependencies (if patch available)
- [ ] Test disaster recovery procedures
- [ ] Review user feedback

### Monthly Tasks
- [ ] Database maintenance (VACUUM, ANALYZE)
- [ ] Security updates applied
- [ ] Performance tuning
- [ ] Capacity planning review
- [ ] Documentation updates

### Quarterly Tasks
- [ ] Full security audit
- [ ] Performance baseline update
- [ ] Disaster recovery drill
- [ ] Architecture review
- [ ] Upgrade planning

---

## Go-Live Checklist

Final sign-off before production announcement:

- [ ] All smoke tests passed
- [ ] No critical bugs remaining
- [ ] Performance acceptable
- [ ] Team trained and ready
- [ ] Support team ready
- [ ] Monitoring active
- [ ] Rollback procedure tested
- [ ] Documentation complete
- [ ] Data integrity verified
- [ ] Security verified
- [ ] Management approval obtained
- [ ] Communication plan ready

---

## Post-Production Support

### SLA Commitments
- Critical (System Down): 1-hour response
- High (Major Feature Broken): 4-hour response
- Medium (Feature Degraded): 8-hour response
- Low (Minor Issue): 24-hour response

### Support Channels
- [ ] On-call schedule established
- [ ] Escalation path defined
- [ ] Support email configured
- [ ] Support chat/ticketing system active
- [ ] Status page active

