# 5. TROUBLESHOOTING GUIDE

## Backend Issues

### Application Won't Start

**Symptom**: Process exits immediately or hangs during startup

**Solutions**:
1. Check logs for error messages
   ```bash
   tail -f logs/application.log
   ```

2. Verify port is available
   ```bash
   netstat -an | grep 8081
   # If in use, change port: --server.port=8082
   ```

3. Check database connection
   ```bash
   java -jar app.jar --spring.profiles.active=prod
   # Look for: "Database connection successful"
   ```

4. Verify environment variables
   ```bash
   echo $DB_HOST
   echo $DB_USER
   echo $DB_PASSWORD
   # Should show correct values
   ```

5. Check file permissions
   ```bash
   ls -l target/smart-monitoring-backend-1.0.0.jar
   # Should be readable
   ```

---

### Database Connection Failed

**Symptom**: "Unable to connect to database" error

**Solutions**:
1. Verify PostgreSQL is running
   ```bash
   psql -U steg_admin -d steg_production -c "SELECT 1"
   # Should return: 1
   ```

2. Check connection string
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/steg_production
   # Verify host, port, database name
   ```

3. Verify credentials
   ```bash
   psql -h localhost -U steg_admin -d steg_production
   # If prompt appears, credentials are correct
   ```

4. Check firewall/network
   ```bash
   telnet localhost 5432
   # Should connect successfully
   ```

5. Check database exists
   ```bash
   psql -U postgres -l | grep steg_production
   # Should show the database
   ```

---

### High Memory Usage

**Symptom**: Application consuming >8GB RAM

**Solutions**:
1. Check Java heap settings
   ```bash
   java -Xmx2048m -Xms512m -jar app.jar
   # Set appropriate heap size based on available RAM
   ```

2. Identify memory leak
   ```bash
   jmap -heap <pid>
   jmap -histo <pid>
   # Look for excessive object counts
   ```

3. Optimize database queries
   - Check slow query log
   - Add indexes to frequently queried columns
   - Use pagination instead of fetching all records

4. Reduce cache sizes
   - In application.properties, reduce cache limits
   - Monitor cache hit rates

---

### Slow API Response Times

**Symptom**: API endpoints taking >5 seconds to respond

**Solutions**:
1. Check database query performance
   ```sql
   SELECT query, mean_exec_time FROM pg_stat_statements 
   ORDER BY mean_exec_time DESC LIMIT 10;
   ```

2. Add missing indexes
   ```sql
   CREATE INDEX idx_measurements_transformateur_date 
   ON measurements(transformateur_id, measurement_date DESC);
   
   CREATE INDEX idx_transformateurs_status 
   ON transformateurs(status);
   ```

3. Analyze query execution plan
   ```sql
   EXPLAIN ANALYZE
   SELECT * FROM measurements 
   WHERE transformateur_id = 1 
   ORDER BY measurement_date DESC LIMIT 100;
   ```

4. Increase database connection pool
   ```properties
   spring.datasource.hikari.maximum-pool-size=30
   spring.datasource.hikari.minimum-idle=10
   ```

5. Enable query caching (if applicable)
   ```properties
   spring.jpa.properties.hibernate.cache.use_second_level_cache=true
   ```

---

### Out of Memory Error

**Symptom**: "java.lang.OutOfMemoryError: Java heap space"

**Solutions**:
1. Increase heap size
   ```bash
   java -Xmx4096m -Xms1024m -jar app.jar
   ```

2. Enable garbage collection logging
   ```bash
   java -Xmx2048m \
     -XX:+PrintGCDetails \
     -XX:+PrintGCTimeStamps \
     -Xloggc:gc.log \
     -jar app.jar
   ```

3. Identify memory leak
   - Take heap dump: `jmap -dump:live,format=b,file=heap.bin <pid>`
   - Analyze with Eclipse Memory Analyzer
   - Look for growing object collections

4. Reduce pagination size
   ```properties
   spring.data.web.pageable.default-page-size=10
   spring.data.web.pageable.max-page-size=100
   ```

---

## Frontend Issues

### Frontend Won't Load

**Symptom**: Blank page or error messages in browser console

**Solutions**:
1. Check browser console for errors
   - Press F12 → Console tab
   - Look for JavaScript errors

2. Verify backend is running
   ```bash
   curl http://localhost:8081/api/health
   # Should return: {"status":"UP"}
   ```

3. Check CORS configuration
   - If seeing CORS error, verify CORS settings in backend
   ```properties
   spring.web.cors.allowed-origins=http://localhost:5173
   ```

4. Clear browser cache
   - Hard refresh: Ctrl+Shift+R (Windows) or Cmd+Shift+R (Mac)
   - Or clear cache in browser settings

5. Check network tab for failed requests
   - Press F12 → Network tab
   - Look for 4xx/5xx responses

---

### API Calls Returning 401 Unauthorized

**Symptom**: "Unauthorized" error on protected endpoints

**Solutions**:
1. Verify token is being sent
   ```javascript
   // Check Authorization header in Network tab
   Authorization: Bearer eyJhbGc...
   ```

2. Check token expiration
   ```javascript
   // In browser console:
   const token = localStorage.getItem('token');
   const decoded = JSON.parse(atob(token.split('.')[1]));
   console.log(new Date(decoded.exp * 1000));
   // Should be in the future
   ```

3. Verify JWT secret matches
   - Backend JWT_SECRET must match token generation
   - If changed, all existing tokens become invalid

4. Check token storage
   ```javascript
   // In browser console:
   localStorage.getItem('token')
   // Should return non-empty token
   ```

5. Login again to get fresh token
   - Clear token: `localStorage.removeItem('token')`
   - Login again at /login page

---

### API Calls Returning 403 Forbidden

**Symptom**: "Access denied" or "Forbidden" error

**Solutions**:
1. Verify user role
   ```javascript
   // Check localStorage for user object
   const user = JSON.parse(localStorage.getItem('user'));
   console.log(user.role);
   // Should be appropriate role for action
   ```

2. Check endpoint authorization
   - Verify @PreAuthorize annotation in controller
   - Confirm user has required role

3. Check token validity
   - Token might have expired
   - Login again to refresh

4. Verify RBAC configuration
   - Check RoleService in backend
   - Ensure role-to-permission mapping is correct

---

### Dashboard Not Loading Data

**Symptom**: Dashboard shows "Loading..." indefinitely

**Solutions**:
1. Check API requests in Network tab
   - Press F12 → Network tab
   - Look for failed requests
   - Check response status and body

2. Verify backend is responding
   ```bash
   curl http://localhost:8081/api/transformateurs?page=0&size=5 \
     -H "Authorization: Bearer <token>"
   # Should return JSON with data
   ```

3. Check browser console for errors
   - Look for JavaScript exceptions
   - Check for unhandled promise rejections

4. Clear cache and refresh
   ```bash
   npm run dev
   # Restart frontend dev server
   ```

5. Check database has data
   ```bash
   curl http://localhost:8081/api/transformateurs/statistics
   # Should show totalTransformateurs > 0
   ```

---

### Charts Not Rendering

**Symptom**: Chart containers empty or showing errors

**Solutions**:
1. Verify Chart.js library is loaded
   - Check Network tab for chart library
   - Look for 404 errors

2. Check data format
   - Inspect Network tab for API response
   - Verify data structure matches chart expectations

3. Browser console errors
   - Look for "Cannot read property of undefined"
   - Check for SVG rendering errors

4. Try different browser
   - Some browsers have issues with certain chart types
   - Test in Chrome, Firefox, Safari

---

## Database Issues

### Cannot Connect to Database

**Symptom**: "Connection refused" or "Database unreachable"

**Solutions**:
1. Verify PostgreSQL is running
   ```bash
   systemctl status postgresql
   # On Windows: Check Services
   ```

2. Test connection
   ```bash
   psql -h localhost -U steg_admin -d steg_production
   # Enter password when prompted
   ```

3. Check PostgreSQL config
   ```bash
   cat /etc/postgresql/14/main/postgresql.conf | grep listen
   # Should show: listen_addresses = '*' or 'localhost'
   ```

4. Check firewall
   ```bash
   sudo ufw allow 5432/tcp
   # On Windows: Add firewall rule for port 5432
   ```

5. Verify credentials
   ```bash
   # Try different user
   psql -U postgres -d steg_production
   ```

---

### Slow Database Queries

**Symptom**: Dashboard or reports loading slowly

**Solutions**:
1. Enable slow query log
   ```properties
   # PostgreSQL postgresql.conf
   log_min_duration_statement = 1000  # Log queries >1s
   ```

2. Analyze slow queries
   ```bash
   tail -f /var/log/postgresql/postgresql.log | grep "LOG:  duration"
   ```

3. Use EXPLAIN ANALYZE
   ```sql
   EXPLAIN ANALYZE
   SELECT * FROM transformateurs 
   WHERE status = 'ACTIF' 
   ORDER BY health_score DESC;
   ```

4. Add appropriate indexes
   ```sql
   CREATE INDEX idx_transformateurs_status_health 
   ON transformateurs(status, health_score DESC);
   ```

5. Update table statistics
   ```sql
   ANALYZE transformateurs;
   VACUUM ANALYZE measurements;
   ```

---

### Database Disk Space Full

**Symptom**: Database stops accepting inserts, queries timeout

**Solutions**:
1. Check disk space
   ```bash
   df -h /var/lib/postgresql
   # Should show available space
   ```

2. Clean up old data (if applicable)
   ```sql
   DELETE FROM measurements 
   WHERE measurement_date < NOW() - INTERVAL '2 years';
   
   VACUUM FULL;
   ```

3. Increase disk space
   - Add new disk volume
   - Expand existing volume
   - Archive old data to archive database

4. Monitor future usage
   ```bash
   # Set up disk space alerts
   watch -n 3600 'du -sh /var/lib/postgresql'
   ```

---

## Authentication Issues

### Users Cannot Login

**Symptom**: Login page gives "Invalid credentials" error

**Solutions**:
1. Verify user exists in database
   ```sql
   SELECT * FROM users WHERE email = 'user@steg.tn';
   ```

2. Check password hashing
   ```sql
   -- Password should be BCrypt hash starting with $2a$ or $2b$
   SELECT email, password FROM users LIMIT 5;
   ```

3. Try test credentials
   ```
   admin@steg.tn / Admin@123
   supervisor@steg.tn / Supervisor@123
   ```

4. Reset user password
   ```sql
   UPDATE users SET password = '$2a$10$...' WHERE email = 'user@steg.tn';
   -- Use BCrypt hash of new password
   ```

5. Check authentication logs
   ```bash
   grep -i "authentication" logs/application.log
   ```

---

### JWT Token Expired

**Symptom**: "Token expired" error after inactivity

**Solutions**:
1. Extend token expiration
   ```properties
   jwt.expiration=7200  # 2 hours instead of 1 hour
   ```

2. Implement refresh token flow
   ```javascript
   // In frontend
   POST /api/auth/refresh
   Authorization: Bearer {refreshToken}
   ```

3. Logout and login again
   - Click logout button
   - Login again to get fresh token

---

## Performance Issues

### High Server CPU Usage

**Symptom**: CPU at 100%, application slow

**Solutions**:
1. Check running processes
   ```bash
   top -p $(pgrep -f "java.*app.jar")
   ```

2. Profile application
   ```bash
   java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
     -jar app.jar
   # Use JProfiler or YourKit to connect
   ```

3. Identify CPU-intensive operations
   - Check application logs for long-running requests
   - Add metrics/monitoring

4. Optimize slow queries
   - See "Database Issues" section above

5. Consider vertical scaling
   - Increase server resources
   - Or horizontal scaling with multiple instances

---

### High Network Latency

**Symptom**: Requests taking >5 seconds

**Solutions**:
1. Check network connectivity
   ```bash
   ping prod-db.example.com
   # Latency should be <50ms
   ```

2. Profile network calls
   - Use browser Network tab
   - Check for waterfall chart
   - Identify largest request

3. Optimize API responses
   - Return only necessary fields
   - Use pagination
   - Implement caching

4. Move database closer
   - Ensure database and application same region
   - Or use read replicas for reporting

---

## Monitoring & Alerts

### Check Application Health
```bash
# Backend health
curl http://localhost:8081/api/health

# Metrics endpoint (if enabled)
curl http://localhost:8081/actuator/metrics

# Database status
curl http://localhost:8081/api/transformateurs?page=0&size=1
```

### Check System Resources
```bash
# CPU usage
top -b -n 1 | head -20

# Memory usage
free -h

# Disk usage
df -h

# Network usage
netstat -i
```

### Check Application Logs
```bash
# Last 50 lines
tail -n 50 logs/application.log

# Follow logs in real-time
tail -f logs/application.log

# Search for errors
grep -i "error\|exception" logs/application.log | tail -20
```

---

## Emergency Contacts & Escalation

- **L1 Support**: First responder troubleshooting
- **L2 Support**: Database and backend specialists
- **L3 Support**: Architecture and infrastructure
- **On-call Engineer**: For after-hours emergencies

---

## Getting Help

1. Check this guide first
2. Review application logs
3. Check database status
4. Contact appropriate support level
5. Provide: error messages, logs, steps to reproduce

