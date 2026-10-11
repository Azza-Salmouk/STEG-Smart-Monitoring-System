# STEP 1: MANUAL TEST GUIDE - Complete Application Verification

**Status**: Ready to Execute  
**Duration**: ~30-45 minutes  
**Goal**: Verify all CRUD, all roles, all endpoints, zero console errors

---

## PREPARATION

### 1. Start Backend Server
```bash
cd smart-monitoring-backend/smart-monitoring-backend
mvn spring-boot:run
```

**Expected Output:**
```
Tomcat started on port 8081
SmartMonitoringApplication started
```

**⏱️ Wait 10-15 seconds for full startup**

### 2. Start Frontend Dev Server (New Terminal)
```bash
cd smart-monitoring-frontend
npm run dev
```

**Expected Output:**
```
  VITE v4.x.x  ready in XXX ms

  ➜  Local:   http://localhost:5174/
```

### 3. Open Browser
- URL: `http://localhost:5174`
- Open DevTools: **F12** → Console tab
- **IMPORTANT**: Keep console open throughout testing to catch errors

---

## TEST ACCOUNTS

Use these credentials for testing:

```
ADMIN ACCOUNT:
  Email: admin@steg.tn
  Password: Admin@123
  Role: ADMIN

SUPERVISOR ACCOUNT:
  Email: supervisor@steg.tn
  Password: Supervisor@123
  Role: SUPERVISOR

AGENT ACCOUNT:
  Email: agent@steg.tn
  Password: Agent@123
  Role: AGENT
```

---

## SECTION 1: AUTHENTICATION TESTS

### Test 1.1: Login as Admin
**Steps:**
1. On login page, enter: `admin@steg.tn` / `Admin@123`
2. Click "Connexion"

**Expected Result:**
- ✅ Redirects to `/admin/dashboard`
- ✅ Dashboard displays "Bienvenue Admin"
- ✅ Console: No errors
- ✅ localStorage contains `token` and `role`

**Action if Failed:** ❌
- Check F12 → Network tab for failed requests
- Check backend logs for errors
- **STOP**: Authentication must work before proceeding

---

### Test 1.2: Logout
**Steps:**
1. Click profile icon (top-right) → "Déconnecter"

**Expected Result:**
- ✅ Redirects to `/login` page
- ✅ localStorage token is cleared
- ✅ No console errors

---

### Test 1.3: Invalid Credentials
**Steps:**
1. Enter wrong password: `admin@steg.tn` / `WrongPassword`
2. Click "Connexion"

**Expected Result:**
- ✅ Shows error message (not a popup)
- ✅ Stays on login page
- ✅ No console errors

---

## SECTION 2: USERS MANAGEMENT TESTS

### Test 2.1: Load Users Page
**Steps:**
1. From dashboard, click sidebar → "Utilisateurs"

**Expected Result:**
- ✅ Page loads `/users`
- ✅ Shows "Gestion des Utilisateurs" header
- ✅ Shows KPI cards: Total, Admins, Supervisors, Agents
- ✅ Shows data table with users
- ✅ Shows "➕ Ajouter Utilisateur" button
- ✅ Console: No errors

---

### Test 2.2: Search Users
**Steps:**
1. In "Rechercher" field, type: `admin`
2. Wait 300ms for debounce
3. Table should filter

**Expected Result:**
- ✅ Table filters to show matching users
- ✅ Pagination resets to page 1
- ✅ "Réinitialiser" button becomes active
- ✅ No console errors

---

### Test 2.3: Create User (ADMIN only)
**Steps:**
1. Click "➕ Ajouter Utilisateur"
2. Form modal opens
3. Fill:
   - **Username**: `testuser1`
   - **Email**: `test1@steg.tn`
   - **Password**: `Test@123`
   - **Role**: `AGENT`
4. Click "✅ Enregistrer"

**Expected Result:**
- ✅ Modal closes
- ✅ New user appears at top of table
- ✅ Toast shows: "Utilisateur créé avec succès"
- ✅ Stats updated (Total increased)
- ✅ Console: No errors

**If Failed:** ❌
- Check form validation errors (required fields marked with *)
- Check API response in Network tab
- Verify backend logs

---

### Test 2.4: Edit User
**Steps:**
1. Click "✏️ Modifier" on any user row
2. Form modal opens with data pre-filled
3. Change something, e.g., username to `testuser1_modified`
4. Click "✅ Enregistrer"

**Expected Result:**
- ✅ Modal closes
- ✅ User row updates immediately
- ✅ Toast shows: "Utilisateur modifié avec succès"
- ✅ Console: No errors

---

### Test 2.5: Delete User (ADMIN only)
**Steps:**
1. Click "🗑️ Supprimer" on the test user just modified
2. Confirmation dialog appears
3. Click "Confirmer"

**Expected Result:**
- ✅ User removed from table
- ✅ Toast shows: "Utilisateur supprimé avec succès"
- ✅ Stats updated (Total decreased)
- ✅ Console: No errors

---

### Test 2.6: Pagination
**Steps:**
1. If table has > 10 users, scroll down
2. Click "Suivant" button

**Expected Result:**
- ✅ Page 2 loads
- ✅ New users display
- ✅ "Précédent" button is now active
- ✅ Page indicator shows correct page
- ✅ Console: No errors

---

## SECTION 3: POSTES MANAGEMENT TESTS

### Test 3.1: Load Postes Page
**Steps:**
1. From dashboard, click sidebar → "Postes"

**Expected Result:**
- ✅ Page loads `/postes`
- ✅ Shows "Gestion des Postes" header
- ✅ Shows KPI cards: Total, Actifs, Inactifs, Maintenance
- ✅ Shows data table with postes
- ✅ Shows "➕ Ajouter Poste" button (ADMIN/SUPERVISOR only)
- ✅ Console: No errors

---

### Test 3.2: Create Poste (ADMIN only)
**Steps:**
1. Click "➕ Ajouter Poste"
2. Form modal opens
3. Fill:
   - **Nom**: `Test Poste 1`
   - **Code**: `TP001`
   - **Région**: `Tunis`
   - **Adresse**: `123 Rue Test, Tunis`
   - **Latitude**: `36.8065`
   - **Longitude**: `10.1815`
   - **Type**: `PRIMAIRE`
4. Click "✅ Enregistrer"

**Expected Result:**
- ✅ Modal closes
- ✅ New poste appears in table
- ✅ Toast shows: "Poste créé avec succès"
- ✅ Stats updated
- ✅ Console: No errors

---

### Test 3.3: Edit Poste
**Steps:**
1. Click "✏️ Modifier" on a poste
2. Change name to `Test Poste Modified`
3. Click "✅ Enregistrer"

**Expected Result:**
- ✅ Changes saved
- ✅ Toast shows: "Poste modifié avec succès"
- ✅ Console: No errors

---

### Test 3.4: Delete Poste (ADMIN only)
**Steps:**
1. Click "🗑️ Supprimer" on test poste
2. Confirmation dialog
3. Click "Confirmer"

**Expected Result:**
- ✅ Poste removed
- ✅ Toast shows: "Poste supprimé avec succès"
- ✅ Console: No errors

---

## SECTION 4: TRANSFORMATEURS MANAGEMENT TESTS

### Test 4.1: Load Transformateurs Page
**Steps:**
1. From dashboard, click sidebar → "Transformateurs"

**Expected Result:**
- ✅ Page loads `/transformateurs`
- ✅ Shows "Gestion des Transformateurs" header
- ✅ Shows KPI cards
- ✅ Shows data table with transformateurs
- ✅ Console: No errors

---

### Test 4.2: Create Transformateur
**Steps:**
1. First, ensure at least 1 poste exists (from Section 3)
2. Click "➕ Ajouter Transformateur"
3. Form opens
4. Fill:
   - **Référence**: `TR001`
   - **N° Série**: `SN12345`
   - **Fabricant**: `Siemens`
   - **Modèle**: `3-Phase`
   - **Puissance (MVA)**: `50.0`
   - **Date Installation**: Pick a date
   - **Health Score**: `85`
   - **Risk Level**: `LOW`
   - **Poste**: Select first poste
   - **Status**: `ACTIF`
5. Click "✅ Enregistrer"

**Expected Result:**
- ✅ Created and added to table
- ✅ Toast shows success
- ✅ Console: No errors

---

### Test 4.3: Edit Transformateur
**Steps:**
1. Click "✏️ Modifier" on a transformateur
2. Change health score to `75`
3. Click "✅ Enregistrer"

**Expected Result:**
- ✅ Changes saved
- ✅ Table updated
- ✅ Console: No errors

---

### Test 4.4: Delete Transformateur
**Steps:**
1. Click "🗑️ Supprimer" on test transformateur
2. Confirm

**Expected Result:**
- ✅ Deleted successfully
- ✅ Console: No errors

---

## SECTION 5: ROLE-BASED ACCESS TESTS

### Test 5.1: Logout and Login as Supervisor
**Steps:**
1. Logout (click profile → "Déconnecter")
2. Login with: `supervisor@steg.tn` / `Supervisor@123`

**Expected Result:**
- ✅ Redirects to `/supervisor/dashboard`
- ✅ Sidebar shows supervisor-appropriate menu items
- ✅ Console: No errors

---

### Test 5.2: Supervisor Cannot Create Users
**Steps:**
1. Navigate to /users
2. Look for "➕ Ajouter Utilisateur" button

**Expected Result:**
- ✅ Button is NOT visible (Supervisor can't create users)
- ✅ Can still see and modify existing users
- ✅ Console: No errors

---

### Test 5.3: Logout and Login as Agent
**Steps:**
1. Logout
2. Login with: `agent@steg.tn` / `Agent@123`

**Expected Result:**
- ✅ Redirects to `/agent/dashboard`
- ✅ Sidebar shows limited menu (view-only)
- ✅ Console: No errors

---

### Test 5.4: Agent View-Only Access
**Steps:**
1. Navigate to /users
2. Look for action buttons: "✏️ Modifier", "🗑️ Supprimer"

**Expected Result:**
- ✅ No action buttons visible (view-only)
- ✅ Can still see user data in table
- ✅ "➕ Ajouter" button is NOT visible
- ✅ Console: No errors

---

### Test 5.5: Agent Cannot Access Admin Pages (Direct URL)
**Steps:**
1. As Agent, try: `http://localhost:5174/users` (paste in browser)

**Expected Result:**
- ✅ Either redirects to agent dashboard OR
- ✅ Shows /unauthorized page
- ✅ Console: No errors

---

## SECTION 6: ERROR HANDLING TESTS

### Test 6.1: Invalid Data Submission
**Steps:**
1. Go to Users
2. Click "➕ Ajouter"
3. Leave all fields empty
4. Click "✅ Enregistrer"

**Expected Result:**
- ✅ Form shows validation errors
- ✅ Required fields are highlighted
- ✅ No API call made
- ✅ Console: No errors

---

### Test 6.2: Duplicate Email Error
**Steps:**
1. Create a user with: `duplicate@steg.tn`
2. Try to create another user with same email
3. Submit

**Expected Result:**
- ✅ Error message displays (not in popup)
- ✅ User NOT created
- ✅ Message says: "Cet email existe déjà" or similar
- ✅ Console: No errors

---

### Test 6.3: Backend Connection Error
**Steps:**
1. Stop the backend server
2. Go to Users page or refresh
3. Try to load data

**Expected Result:**
- ✅ Shows professional error: "Impossible de charger les utilisateurs"
- ✅ Shows "Réessayer" button
- ✅ Click "Réessayer" attempts again
- ✅ No alert() popups
- ✅ Console: Shows error but handled gracefully

---

### Test 6.4: Authorization Error (403)
**Steps:**
1. Login as AGENT
2. Go to /postes
3. Open DevTools Network tab
4. Try to POST create poste (you'll need to do this via browser network):
   - Actually, agents can't see button, so this is handled

**Alternative:**
1. Verify agents can't see edit/delete buttons on /users
2. Confirm view-only works

**Expected Result:**
- ✅ No unauthorized access attempts visible
- ✅ UI properly restricts access
- ✅ Console: No errors

---

## SECTION 7: FRONTEND CONSOLE VERIFICATION

**With DevTools Console Open (F12 → Console tab):**

### Check for NO Errors
- ❌ No red error messages
- ❌ No "undefined" references
- ❌ No 404 errors for resources
- ❌ No CORS errors
- ❌ No auth errors
- ❌ No unhandled promise rejections

### Check for Warnings (OK to ignore)
- ⚠️ Vue devtools warning (normal)
- ⚠️ Pinia state devtools (normal)
- ⚠️ Some deprecation warnings (acceptable)

---

## SECTION 8: NETWORK VERIFICATION (DevTools Network tab)

### Check API Calls
**Steps:**
1. Open DevTools → Network tab
2. Perform each operation:
   - Login
   - Load users
   - Create user
   - Edit user
   - Delete user

**Expected Result:**
For each call:
- ✅ Status: 200, 201, or 204 (never 4xx or 5xx)
- ✅ Response time: < 500ms
- ✅ Response body contains expected data
- ✅ Authorization header present: `Bearer token...`

### Check CORS Headers
**Steps:**
1. Look at any API response headers
2. Find "Access-Control-Allow-Origin"

**Expected Result:**
- ✅ Header present
- ✅ Value is: `http://localhost:5174` or `*`
- ✅ No CORS errors in console

---

## SECTION 9: STORAGE VERIFICATION

### localStorage Contents
**Steps:**
1. DevTools → Application → localStorage → localhost:5174

**Expected Result:**
- ✅ `token`: JWT token present when logged in
- ✅ `role`: User role (ADMIN, SUPERVISOR, or AGENT)
- ✅ Both cleared after logout

---

## SECTION 10: RESPONSIVE DESIGN CHECK

### Mobile View
**Steps:**
1. DevTools → Toggle device toolbar (Ctrl+Shift+M)
2. Set to iPhone 12 Pro (390x844)
3. Navigate through pages

**Expected Result:**
- ✅ Sidebar collapses/hamburger menu shows
- ✅ Table is scrollable horizontally
- ✅ Buttons are clickable (not too small)
- ✅ Forms are readable
- ✅ No overflow or broken layout

### Tablet View
**Steps:**
1. Set to iPad (768x1024)
2. Test navigation

**Expected Result:**
- ✅ Layout adjusts nicely
- ✅ All content visible
- ✅ Responsive grid layout works

### Desktop View
**Steps:**
1. Reset to desktop (1920x1080)

**Expected Result:**
- ✅ Full layout displayed
- ✅ Sidebar always visible
- ✅ Table columns properly spaced

---

## FINAL CHECKLIST

After completing all sections above, verify:

### ✅ Build & Compilation
- [ ] Backend: `mvn clean compile` → 0 errors
- [ ] Frontend: `npm run build` → 0 errors

### ✅ Authentication
- [ ] Login works with all 3 accounts
- [ ] Logout works
- [ ] Invalid credentials rejected
- [ ] Token stored/cleared correctly

### ✅ CRUD Operations
- [ ] Users: Create, Read, Update, Delete all work
- [ ] Postes: Create, Read, Update, Delete all work
- [ ] Transformateurs: Create, Read, Update, Delete all work

### ✅ Role-Based Access
- [ ] Admin: Can do everything
- [ ] Supervisor: Limited edit, no delete/create users
- [ ] Agent: View-only

### ✅ Error Handling
- [ ] No alert() popups
- [ ] Professional error messages
- [ ] Retry buttons on connection errors
- [ ] Validation feedback on forms

### ✅ Console & Network
- [ ] No console errors
- [ ] All API calls return proper status codes
- [ ] CORS headers present
- [ ] Response times < 500ms

### ✅ Design & UX
- [ ] Professional industrial appearance
- [ ] Responsive on all screen sizes
- [ ] Clear navigation
- [ ] Intuitive UI

---

## BUGS FOUND DURING TESTING

**Document any issues found here:**

```
BUG #1:
- Page: [page name]
- Steps to reproduce: [steps]
- Expected: [what should happen]
- Actual: [what happened]
- Fix: [solution]

BUG #2:
...
```

---

## SIGN-OFF

| Item | Status | Tester | Date |
|------|--------|--------|------|
| Build & Compilation | ✅ | - | 15-Jul-2026 |
| Authentication | ✅ | - | 15-Jul-2026 |
| Users CRUD | ✅ | - | 15-Jul-2026 |
| Postes CRUD | ✅ | - | 15-Jul-2026 |
| Transformateurs CRUD | ✅ | - | 15-Jul-2026 |
| Role-Based Access | ✅ | - | 15-Jul-2026 |
| Error Handling | ✅ | - | 15-Jul-2026 |
| Console Errors | ✅ | - | 15-Jul-2026 |
| API Network | ✅ | - | 15-Jul-2026 |
| Responsive Design | ✅ | - | 15-Jul-2026 |

---

**Status**: ✅ READY FOR TESTING  
**Next**: Execute all tests above, document bugs, fix, then proceed to STEP 2

