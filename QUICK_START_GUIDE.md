# 🚀 QUICK START GUIDE - STEG Smart Monitoring

## System Currently Running

Both servers are running and ready to use:
- **Backend**: http://localhost:8081
- **Frontend**: http://localhost:5173

## Access the Application

1. Open your browser
2. Go to: **http://localhost:5173**
3. You'll see the login page

## Test Logins (All Passwords are in format: [Role]@123)

| Email | Password | Role | Dashboard |
|-------|----------|------|-----------|
| admin@steg.tn | Admin@123 | ADMIN | System-wide Statistics |
| supervisor@steg.tn | Supervisor@123 | SUPERVISOR | Team & Operations |
| agent@steg.tn | Agent@123 | AGENT | My Work & Interventions |
| customer@steg.tn | Customer@123 | SERVICE_CLIENT | My Complaints |

## What You'll See

### Admin Dashboard
- 20 Postes (substations)
- 149 Transformateurs (transformers)
- 120 Interventions across system
- 300 Active Alerts
- 150 Complaints
- Full system metrics and statistics

### Agent Dashboard
- 40 Interventions assigned to agent@steg.tn
- 12 Interventions In Progress
- 5 Completed Interventions
- 8 Critical Priority Interventions
- 38 Accessible Transformers
- 73 Alerts on accessible equipment

### Supervisor Dashboard
- 20 Postes overview
- 149 Transformateurs status
- 19 Active Interventions
- Complaint statistics
- Operational metrics

### Service Client Dashboard
- 150 Total Complaints
- 35 New Complaints
- 28 In Analysis
- 19 Assigned
- 17 Processing
- 25 Resolved (34% resolution rate)
- 38 Critical Priority

## Key Features Working

✅ Authentication & Authorization
✅ Role-based Dashboard Access
✅ Real Data from Database
✅ Settings Page (for all roles)
✅ JWT Token Security
✅ Four Complete Dashboards
✅ Statistics & Metrics
✅ User Management

## API Endpoints (for testing)

### Authentication
```
POST http://localhost:8081/api/auth/login
Body: { "email": "admin@steg.tn", "password": "Admin@123" }
Response: { "id": 1, "token": "eyJ...", "role": "ADMIN" }
```

### Dashboard Endpoints
```
GET http://localhost:8081/api/dashboard/admin
GET http://localhost:8081/api/dashboard/supervisor
GET http://localhost:8081/api/dashboard/agent/3
GET http://localhost:8081/api/dashboard/service-client
```

## Troubleshooting

### Dashboards showing zeros?
- Backend may have restarted (H2 data regenerates on startup)
- Give it ~20 seconds after backend starts
- Refresh browser (F5)

### Can't login?
- Verify backend is running: http://localhost:8081/health
- Try credentials: admin@steg.tn / Admin@123

### Frontend is blank?
- Verify frontend is running: http://localhost:5173
- Clear browser cache (Ctrl+Shift+Delete)
- Reload page (F5)

### Settings page not visible?
- Make sure you're logged in
- Check sidebar for "Paramètres" link
- It should appear for all roles

## Database

H2 In-Memory Database
- Recreates automatically on backend restart
- Data includes: users, postes, transformateurs, interventions, alerts, complaints, measurements
- Console available at: http://localhost:8081/h2-console

## Backend Build (if needed)

To rebuild backend:
```bash
cd smart-monitoring-backend/smart-monitoring-backend
mvn clean install
java -jar target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2
```

## Frontend Build (if needed)

To rebuild frontend:
```bash
cd smart-monitoring-frontend
npm install
npm run dev
```

## System Architecture

```
Browser (Port 5173)
       ↓
  Vue 3 Frontend
       ↓
JWT Authentication
       ↓
Spring Boot Backend (Port 8081)
       ↓
H2 In-Memory Database
```

## Data Statistics

| Entity | Count | Details |
|--------|-------|---------|
| Users | 6 | 4 main roles + 2 additional agents |
| Postes | 20 | Electricity substations across Tunisia |
| Transformateurs | 149 | Distributed across postes |
| Interventions | 120 | 40 per agent (3 agents) |
| Alerts | 300 | Various severity levels |
| Complaints | 150 | Various statuses |
| Measurements | 4917 | IoT sensor data |

## Next Steps

1. **Test the dashboards** - Try different logins to see role-specific views
2. **Access Settings** - Check that settings page works for each role
3. **Monitor Real Data** - Verify charts display correctly with data
4. **Test Responsiveness** - Try on different screen sizes
5. **Performance Test** - Load test API endpoints

## Support

For issues or questions:
1. Check the FINAL_DASHBOARD_TEST_REPORT.md for detailed technical info
2. Check the MISSION_COMPLETE_SUMMARY.md for architecture overview
3. Look at backend logs: Application runs with DEBUG logging enabled

## Key Files Modified

- **DemoDataGenerator.java** - Fixed intervention assignment
- **LoginView.vue** - Added SERVICE_CLIENT redirect
- **Settings.vue** - Removed restricted sections
- **router/index.js** - Extended Settings to 4 roles
- **ModernSidebar.vue** - Added Settings link for all roles

---

**System Status**: ✅ OPERATIONAL
**All Dashboards**: ✅ DISPLAYING REAL DATA
**Ready for**: Testing, Demo, UAT
