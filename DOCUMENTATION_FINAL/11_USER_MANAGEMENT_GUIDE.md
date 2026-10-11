# 11. USER MANAGEMENT GUIDE

## Overview
Complete guide for managing users, roles, and permissions in STEG Smart Monitoring System.

---

## User Roles & Permissions

### Role Hierarchy

#### 1. ADMIN
**Purpose**: Full system control

**Permissions**:
- ✅ Create/Edit/Delete users
- ✅ Manage all transformateurs
- ✅ View all data
- ✅ Create/Edit/Delete interventions
- ✅ Access admin dashboard
- ✅ System configuration
- ✅ View audit logs

**Cannot**: None (full access)

#### 2. SUPERVISOR
**Purpose**: Operational oversight

**Permissions**:
- ✅ View all transformateurs
- ✅ Create/Edit interventions
- ✅ Assign interventions to agents
- ✅ View all alerts
- ✅ Acknowledge/resolve alerts
- ✅ View complaints
- ✅ Access supervisor dashboard
- ✅ Generate reports

**Cannot**:
- ❌ Create/Delete users
- ❌ Delete transformateurs
- ❌ Access system configuration
- ❌ View audit logs

#### 3. AGENT
**Purpose**: Field operations

**Permissions**:
- ✅ View assigned transformateurs
- ✅ View assigned interventions
- ✅ Create measurements
- ✅ Update intervention status
- ✅ View own performance metrics
- ✅ Access agent dashboard

**Cannot**:
- ❌ Create/Edit interventions
- ❌ Assign interventions
- ❌ Delete data
- ❌ View other agents' data

#### 4. SERVICE_CLIENT
**Purpose**: Customer service

**Permissions**:
- ✅ View own complaints
- ✅ File new complaints
- ✅ View complaint history
- ✅ Download documentation
- ✅ Access customer dashboard

**Cannot**:
- ❌ Create interventions
- ❌ View internal alerts
- ❌ Modify transformateur data
- ❌ View other customer data

---

## User Lifecycle

### Creating Users

#### Via Admin Dashboard
1. Navigate to Settings → User Management
2. Click "Add New User"
3. Enter details:
   - Email: user@steg.tn
   - Role: Select from dropdown
   - Status: ACTIVE
4. System generates temporary password
5. Send credentials via secure channel
6. User must change password on first login

#### Via API
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newagent@steg.tn",
    "password": "TempPass@123",
    "role": "AGENT"
  }'
```

#### CSV Bulk Import
```bash
# Prepare CSV: users.csv
email,role
agent1@steg.tn,AGENT
agent2@steg.tn,AGENT
supervisor1@steg.tn,SUPERVISOR

# Upload via API
curl -X POST http://localhost:8081/api/users/import \
  -H "Authorization: Bearer {admin_token}" \
  -F "file=@users.csv"
```

### Assigning Users to Postes

**For AGENT users only**:
```bash
curl -X POST http://localhost:8081/api/users/{userId}/assign-poste \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "posteIds": [1, 2, 3]
  }'
```

### Modifying User Roles

```bash
# Update user role
curl -X PUT http://localhost:8081/api/users/{userId} \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "role": "SUPERVISOR"
  }'
```

### Deactivating/Activating Users

```bash
# Deactivate user
curl -X PUT http://localhost:8081/api/users/{userId}/status \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "INACTIVE"
  }'

# Reactivate user
curl -X PUT http://localhost:8081/api/users/{userId}/status \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE"
  }'
```

### Resetting Passwords

```bash
# Admin reset user password
curl -X POST http://localhost:8081/api/users/{userId}/reset-password \
  -H "Authorization: Bearer {admin_token}"

# User changes own password
curl -X POST http://localhost:8081/api/users/change-password \
  -H "Authorization: Bearer {user_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "oldPassword": "current_password",
    "newPassword": "new_password"
  }'
```

### Deleting Users

```bash
curl -X DELETE http://localhost:8081/api/users/{userId} \
  -H "Authorization: Bearer {admin_token}"

# Note: Soft delete - user marked as deleted, data retained
```

---

## Password Management

### Password Requirements
- **Minimum Length**: 8 characters
- **Complexity**: Must contain uppercase, lowercase, number, special character
- **Examples**:
  - ✅ Secure@123
  - ✅ MyPass#456
  - ❌ password123 (no uppercase/special)
  - ❌ Pass@1 (too short)

### Password Policies
```properties
# In application.properties
password.min-length=8
password.require-uppercase=true
password.require-lowercase=true
password.require-digit=true
password.require-special-char=true
password.max-age-days=90  # Require change every 90 days
password.history=5         # Prevent reusing last 5 passwords
password.lockout-attempts=5  # Lock after 5 failed attempts
password.lockout-duration-minutes=30
```

### Expiration & Renewal
```bash
# Check password age
SELECT email, 
       DATE_TRUNC('day', CURRENT_TIMESTAMP - updated_at) as days_since_change
FROM users
WHERE DATE_TRUNC('day', CURRENT_TIMESTAMP - updated_at) > 90;

# Force password change on next login
UPDATE users 
SET password_expired = true 
WHERE email = 'user@steg.tn';
```

---

## Access Control & Permissions

### API Endpoint Authorization

#### Public Endpoints (No Auth Required)
```
POST /api/auth/login
POST /api/auth/logout
GET /api/health
```

#### Role-Specific Endpoints

**ADMIN Only**:
```
GET    /api/users
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
GET    /api/audit-logs
```

**SUPERVISOR + ADMIN**:
```
GET    /api/interventions
POST   /api/interventions
PUT    /api/interventions/{id}
GET    /api/alerts
PUT    /api/alerts/{id}/acknowledge
```

**AGENT + SUPERVISOR + ADMIN**:
```
GET    /api/measurements
POST   /api/measurements
GET    /api/transformateurs
```

**SERVICE_CLIENT**:
```
POST   /api/complaints
GET    /api/complaints?clientId={clientId}
GET    /api/dashboards/client
```

### Field-Level Access Control

```java
@Entity
public class Transformateur {
    
    // Visible to all authenticated users
    private String reference;
    
    // Visible only to ADMIN and SUPERVISOR
    @JsonView(Confidential.class)
    private Double cost;
    
    // Visible only to ADMIN
    @JsonView(Internal.class)
    private String internalNotes;
}
```

---

## Audit & Compliance

### Audit Logging
```sql
-- Enable audit logging
SELECT * FROM user_audit WHERE action = 'CREATE' AND entity_type = 'USER';

-- Monitor sensitive actions
SELECT * FROM audit_log 
WHERE action IN ('DELETE', 'PERMISSION_CHANGE')
ORDER BY timestamp DESC
LIMIT 100;
```

### Login History
```sql
-- Track user login attempts
SELECT email, login_time, success, ip_address 
FROM login_audit
WHERE login_date >= CURRENT_DATE - INTERVAL '7 days'
ORDER BY login_time DESC;

-- Detect suspicious activity
SELECT email, COUNT(*) as failed_attempts
FROM login_audit
WHERE success = false
  AND login_time > CURRENT_TIMESTAMP - INTERVAL '1 hour'
GROUP BY email
HAVING COUNT(*) > 5;
```

### User Activity Report
```sql
-- Generate monthly user activity report
SELECT 
    u.email,
    u.role,
    COUNT(DISTINCT CASE WHEN l.login_time >= CURRENT_DATE - INTERVAL '30 days' THEN l.id END) as logins_30d,
    COUNT(DISTINCT CASE WHEN c.created_at >= CURRENT_DATE - INTERVAL '30 days' THEN c.id END) as creations_30d,
    MAX(l.login_time) as last_login
FROM users u
LEFT JOIN login_audit l ON u.id = l.user_id
LEFT JOIN audit_log c ON u.id = c.user_id AND c.action = 'CREATE'
GROUP BY u.id, u.email, u.role
ORDER BY last_login DESC;
```

---

## Best Practices

### Account Security
- [ ] Never share password via email
- [ ] Require users to change password on first login
- [ ] Deactivate unused accounts after 30 days
- [ ] Use strong, unique passwords
- [ ] Enable multi-factor authentication (if available)
- [ ] Use password manager for secure storage

### Access Management
- [ ] Principle of Least Privilege (only needed permissions)
- [ ] Segregate duties (prevent fraud/errors)
- [ ] Review user permissions quarterly
- [ ] Remove access immediately upon departure
- [ ] Document all access changes
- [ ] Monitor for unusual activity

### Audit & Compliance
- [ ] Maintain audit logs for 3+ years
- [ ] Review audit logs monthly
- [ ] Investigate all unauthorized attempts
- [ ] Document all security incidents
- [ ] Notify users of policy changes
- [ ] Train users on security practices

---

## Troubleshooting

### User Cannot Login
1. Verify account is ACTIVE (not INACTIVE)
2. Check password is correct (case-sensitive)
3. Verify no account lockout (5+ failed attempts)
4. Check if password expired
5. Verify token not revoked
6. Check email for temporary password if new user

### User Has Wrong Permissions
1. Verify assigned role in database
2. Check token contains correct role claim
3. Verify @PreAuthorize annotations on endpoint
4. Clear browser cache and re-login
5. Verify user not in multiple conflicting roles

### Password Reset Issues
1. Verify user email exists in system
2. Check email service is working
3. Verify reset token not expired (15 min)
4. Ensure new password meets requirements
5. Check for password history conflicts

---

## Test Accounts

### Pre-configured Credentials
```
Admin Account:
  Email: admin@steg.tn
  Password: Admin@123
  Role: ADMIN

Supervisor Account:
  Email: supervisor@steg.tn
  Password: Supervisor@123
  Role: SUPERVISOR

Agent Accounts:
  Email: agent@steg.tn
  Password: Agent@123
  Role: AGENT

Client Account:
  Email: client@steg.tn
  Password: Client@123
  Role: SERVICE_CLIENT
```

---

## User Management Checklist

- [ ] All users have unique email addresses
- [ ] Users assigned to correct roles
- [ ] Password policy enforced
- [ ] Audit logging enabled
- [ ] Access reviewed quarterly
- [ ] Inactive accounts deactivated
- [ ] Incident procedures documented
- [ ] Users trained on security
- [ ] Emergency admin procedures documented
- [ ] Backup admin credentials secured

