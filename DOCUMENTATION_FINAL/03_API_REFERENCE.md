# 3. API REFERENCE

## Base URL
```
http://localhost:8081/api (development)
https://api.steg-monitoring.tn (production)
```

---

## Authentication

### Login
```
POST /auth/login
Content-Type: application/json

{
  "email": "admin@steg.tn",
  "password": "Admin@123"
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "email": "admin@steg.tn",
    "role": "ADMIN"
  }
}
```

### Refresh Token
```
POST /auth/refresh
Authorization: Bearer {refreshToken}

Response (200 OK):
{
  "token": "new-jwt-token",
  "expiresIn": 3600
}
```

### Logout
```
POST /auth/logout
Authorization: Bearer {token}

Response (200 OK):
{ "message": "Logout successful" }
```

---

## Users Management

### Get All Users (ADMIN only)
```
GET /users?page=0&size=10&role=ADMIN&status=ACTIVE
Authorization: Bearer {token}

Response (200 OK):
{
  "content": [
    {
      "id": 1,
      "email": "admin@steg.tn",
      "role": "ADMIN",
      "status": "ACTIVE",
      "createdAt": "2024-01-01T00:00:00Z",
      "updatedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 10
}
```

### Create User (ADMIN only)
```
POST /users
Authorization: Bearer {token}
Content-Type: application/json

{
  "email": "newuser@steg.tn",
  "password": "SecurePass@123",
  "role": "AGENT"
}

Response (201 Created):
{
  "id": 5,
  "email": "newuser@steg.tn",
  "role": "AGENT",
  "status": "ACTIVE"
}
```

### Update User (ADMIN only)
```
PUT /users/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "email": "updated@steg.tn",
  "role": "SUPERVISOR"
}

Response (200 OK):
{
  "id": 5,
  "email": "updated@steg.tn",
  "role": "SUPERVISOR"
}
```

### Delete User (ADMIN only)
```
DELETE /users/{id}
Authorization: Bearer {token}

Response (200 OK):
{ "message": "User deleted successfully" }
```

---

## Transformateurs

### Get All Transformateurs
```
GET /transformateurs?page=0&size=10&status=ACTIF&healthScore=50
Authorization: Bearer {token}

Response (200 OK):
{
  "content": [
    {
      "id": 1,
      "reference": "TR-001",
      "power": 630,
      "voltage": 400,
      "status": "ACTIF",
      "healthScore": 85.5,
      "posteId": 1,
      "installationDate": "2020-01-15",
      "createdAt": "2024-01-01T00:00:00Z"
    }
  ],
  "totalElements": 149,
  "totalPages": 15,
  "currentPage": 0
}
```

### Get Transformateur by ID
```
GET /transformateurs/{id}
Authorization: Bearer {token}

Response (200 OK):
{
  "id": 1,
  "reference": "TR-001",
  "power": 630,
  "voltage": 400,
  "status": "ACTIF",
  "healthScore": 85.5,
  "posteId": 1,
  "measurements": 4917,
  "lastMeasurement": "2024-12-01T14:30:00Z",
  "criticalAlerts": 2,
  "pendingInterventions": 1
}
```

### Create Transformateur (ADMIN only)
```
POST /transformateurs
Authorization: Bearer {token}
Content-Type: application/json

{
  "reference": "TR-150",
  "power": 400,
  "voltage": 400,
  "status": "ACTIF",
  "posteId": 5,
  "installationDate": "2024-01-01"
}

Response (201 Created):
{
  "id": 150,
  "reference": "TR-150",
  "power": 400,
  "voltage": 400,
  "status": "ACTIF",
  "healthScore": 75.0
}
```

### Update Transformateur (ADMIN/SUPERVISOR)
```
PUT /transformateurs/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "MAINTENANCE",
  "power": 500
}

Response (200 OK):
{
  "id": 1,
  "reference": "TR-001",
  "status": "MAINTENANCE",
  "power": 500
}
```

### Delete Transformateur (ADMIN only)
```
DELETE /transformateurs/{id}
Authorization: Bearer {token}

Response (200 OK):
{ "message": "Transformateur deleted successfully" }
```

### Get Transformateur Statistics
```
GET /transformateurs/statistics
Authorization: Bearer {token}

Response (200 OK):
{
  "totalTransformateurs": 149,
  "activeTransformateurs": 145,
  "maintenanceTransformateurs": 4,
  "averageHealthScore": 72.3,
  "criticalCount": 3,
  "warningCount": 12,
  "lastUpdated": "2024-12-01T14:30:00Z"
}
```

---

## Measurements

### Get Measurements
```
GET /measurements?transformateurId=1&page=0&size=50&sortBy=date&order=DESC
Authorization: Bearer {token}

Response (200 OK):
{
  "content": [
    {
      "id": 4917,
      "transformateurId": 1,
      "temperature": 42.5,
      "voltage": 395,
      "current": 285,
      "power": 98.5,
      "healthScore": 82.0,
      "status": "WARNING",
      "measurementDate": "2024-12-01T14:30:00Z"
    }
  ],
  "totalElements": 4917,
  "currentPage": 0
}
```

### Create Measurement (AGENT)
```
POST /measurements
Authorization: Bearer {token}
Content-Type: application/json

{
  "transformateurId": 1,
  "temperature": 45.2,
  "voltage": 398,
  "current": 290,
  "power": 97.8
}

Response (201 Created):
{
  "id": 4918,
  "transformateurId": 1,
  "temperature": 45.2,
  "voltage": 398,
  "current": 290,
  "power": 97.8,
  "healthScore": 78.5,
  "status": "NORMAL"
}
```

---

## Alerts

### Get Alerts
```
GET /alerts?transformateurId=1&level=HIGH&status=ACTIVE&page=0&size=10
Authorization: Bearer {token}

Response (200 OK):
{
  "content": [
    {
      "id": 1,
      "transformateurId": 1,
      "title": "High Temperature Alert",
      "level": "HIGH",
      "type": "TEMPERATURE",
      "status": "ACTIVE",
      "message": "Temperature exceeds 80°C",
      "createdAt": "2024-12-01T14:30:00Z",
      "acknowledgedAt": null,
      "resolvedAt": null
    }
  ],
  "totalElements": 300
}
```

### Acknowledge Alert (SUPERVISOR/AGENT)
```
PUT /alerts/{id}/acknowledge
Authorization: Bearer {token}
Content-Type: application/json

{
  "comment": "Investigating the issue"
}

Response (200 OK):
{
  "id": 1,
  "status": "ACKNOWLEDGED",
  "acknowledgedAt": "2024-12-01T15:00:00Z",
  "acknowledgedBy": "supervisor@steg.tn"
}
```

### Resolve Alert (SUPERVISOR/AGENT)
```
PUT /alerts/{id}/resolve
Authorization: Bearer {token}
Content-Type: application/json

{
  "comment": "Issue resolved - temperature normalized"
}

Response (200 OK):
{
  "id": 1,
  "status": "RESOLVED",
  "resolvedAt": "2024-12-01T16:00:00Z"
}
```

---

## Interventions

### Get Interventions
```
GET /interventions?status=PENDING&priority=CRITICAL&agentId=3&page=0&size=10
Authorization: Bearer {token}

Response (200 OK):
{
  "content": [
    {
      "id": 1,
      "reference": "INT-001",
      "transformateurId": 1,
      "agentId": 3,
      "type": "INSPECTION",
      "priority": "CRITICAL",
      "status": "PENDING",
      "description": "Preventive maintenance check",
      "scheduledDate": "2024-12-05T09:00:00Z",
      "createdAt": "2024-12-01T14:30:00Z"
    }
  ],
  "totalElements": 120
}
```

### Create Intervention (AGENT/SUPERVISOR)
```
POST /interventions
Authorization: Bearer {token}
Content-Type: application/json

{
  "transformateurId": 1,
  "agentId": 3,
  "type": "REPAIR",
  "priority": "HIGH",
  "description": "Replace cooling fan",
  "scheduledDate": "2024-12-05T10:00:00Z"
}

Response (201 Created):
{
  "id": 121,
  "reference": "INT-121",
  "transformateurId": 1,
  "agentId": 3,
  "status": "PENDING",
  "priority": "HIGH"
}
```

### Update Intervention Status (AGENT)
```
PUT /interventions/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "COMPLETED",
  "notes": "Fan replaced successfully, temperature normalized"
}

Response (200 OK):
{
  "id": 1,
  "reference": "INT-001",
  "status": "COMPLETED",
  "completedDate": "2024-12-05T12:30:00Z"
}
```

---

## Complaints

### Get Complaints
```
GET /complaints?status=PENDING&priority=HIGH&page=0&size=10
Authorization: Bearer {token}

Response (200 OK):
{
  "content": [
    {
      "id": 1,
      "reference": "COM-001",
      "title": "Frequent power outages",
      "description": "Multiple outages last month",
      "category": "SERVICE_QUALITY",
      "priority": "HIGH",
      "status": "PENDING",
      "submittedBy": "client@steg.tn",
      "createdAt": "2024-12-01T10:00:00Z"
    }
  ],
  "totalElements": 150
}
```

### Create Complaint (SERVICE_CLIENT)
```
POST /complaints
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Service issue",
  "description": "Experiencing power fluctuations",
  "category": "SERVICE_QUALITY",
  "priority": "MEDIUM"
}

Response (201 Created):
{
  "id": 151,
  "reference": "COM-151",
  "title": "Service issue",
  "status": "PENDING",
  "createdAt": "2024-12-01T16:00:00Z"
}
```

### Update Complaint Status (SUPERVISOR)
```
PUT /complaints/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "RESOLVED",
  "resolution": "Issue was due to weather conditions, now normalized"
}

Response (200 OK):
{
  "id": 1,
  "status": "RESOLVED",
  "resolvedAt": "2024-12-02T10:00:00Z"
}
```

---

## Dashboards

### Get Admin Dashboard
```
GET /dashboards/admin
Authorization: Bearer {token}

Response (200 OK):
{
  "systemHealth": 72.3,
  "activeAlerts": 15,
  "pendingInterventions": 8,
  "completedToday": 5,
  "criticalTransformateurs": 3,
  "transformateursByStatus": {
    "ACTIF": 145,
    "MAINTENANCE": 4
  },
  "recentAlerts": [...],
  "upcomingInterventions": [...]
}
```

### Get Agent Dashboard
```
GET /dashboards/agent
Authorization: Bearer {token}

Response (200 OK):
{
  "assignedInterventions": 5,
  "completedToday": 2,
  "pendingToday": 3,
  "avgResolutionTime": 2.5,
  "myTransformateurs": 23,
  "recentActivity": [...],
  "performanceMetrics": {...}
}
```

---

## Health Check

### System Health
```
GET /health
Response (200 OK):
{
  "status": "UP",
  "database": "UP",
  "diskSpace": "UP",
  "timestamp": "2024-12-01T16:00:00Z"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-12-01T16:00:00Z",
  "status": 400,
  "error": "Invalid input",
  "message": "Email format is invalid",
  "path": "/api/users"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-12-01T16:00:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired token",
  "path": "/api/transformateurs"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-12-01T16:00:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied - requires ADMIN role",
  "path": "/api/users"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-12-01T16:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Transformateur with ID 999 not found",
  "path": "/api/transformateurs/999"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-12-01T16:00:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Database connection failed",
  "path": "/api/transformateurs"
}
```

---

## Rate Limiting

```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1701459600
```

Limits:
- Authenticated: 1000 requests/hour
- Unauthenticated: 100 requests/hour

---

## Pagination

All list endpoints support:
- `page`: 0-indexed page number (default: 0)
- `size`: Page size (default: 10, max: 100)
- `sort`: Field to sort by
- `order`: ASC or DESC

Example: `GET /transformateurs?page=1&size=20&sort=healthScore&order=DESC`

