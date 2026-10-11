# CRITICAL BUG DEBUG AUDIT - CORS 403 & DUPLICATE LAYOUT

**Status**: 🔴 **CRITICAL BUGS IDENTIFIED**  
**Date**: 15 Juillet 2026  
**Issues**: 2 (CORS 403 + Duplicate Layout)

---

## BUG #1: CORS 403 ERROR - OPTIONS REQUESTS BLOCKED

### Symptoms
```
Network Error: Access to XMLHttpRequest blocked by CORS
No 'Access-Control-Allow-Origin' header
GET /api/users → 403
GET /api/postes → 403
GET /api/transformateurs → 403
POST /api/users → 403
```

### Root Cause Analysis

#### Problem 1: CORS Config Not Integrated Into Spring Security
**File**: `SecurityConfig.java`
**Issue**: CorsConfig (WebMvcConfigurer) doesn't work with Spring Security filters
**Why**: WebMvcConfigurer runs AFTER Security Filters. Browser OPTIONS preflight fails at Security Filter level BEFORE reaching CORS config

**Current Code (WRONG)**:
```java
// CorsConfig.java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:5174", ...)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            ...
    }
}
```

**Why It Fails**:
1. Browser sends OPTIONS preflight request
2. Spring Security filter chain intercepts it
3. No CORS config in SecurityConfig
4. Request returns 403 BEFORE reaching CorsConfig
5. CORS headers never added
6. Browser blocks request

#### Problem 2: JWT Filter Doesn't Skip OPTIONS Requests
**File**: `JwtAuthenticationFilter.java`
**Issue**: Filter requires `Authorization: Bearer` header even for OPTIONS
**Why**: OPTIONS requests typically have no Authorization header

**Current Code (WRONG)**:
```java
@Override
protected void doFilterInternal(...) throws ... {
    final String authHeader = request.getHeader("Authorization");
    
    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;  // ← Continues but no CORS handling
    }
    // ... JWT validation
}
```

**Why It Fails**:
1. OPTIONS request has NO Authorization header
2. Filter skips JWT check (correct)
3. But continues without setting CORS headers
4. Spring Security denies request (403)

#### Problem 3: SecurityFilterChain Doesn't Allow OPTIONS
**File**: `SecurityConfig.java`
**Issue**: Authorization rules don't explicitly allow OPTIONS on all paths

**Current Code (WRONG)**:
```java
.authorizeHttpRequests(auth ->
    auth
        .requestMatchers("/api/auth/**")
        .permitAll()
        .anyRequest()
        .authenticated()  // ← ALL OTHER REQUESTS REQUIRE AUTH
)
```

**Why It Fails**:
- OPTIONS /api/users → Not in auth/**, not permitAll → DENIED (403)
- OPTIONS /api/postes → Not in auth/**, not permitAll → DENIED (403)
- Browser gets 403, never sends actual GET/POST

---

## BUG #2: DUPLICATE SIDEBAR/NAVBAR - LAYOUT RENDERED TWICE

### Symptoms
- Sidebar appears twice on page
- Navbar appears twice on page
- Both on left and overlapping

### Root Cause Analysis

**Status**: ✅ **NOT FOUND IN CODE** - Layout appears to be correct
- App.vue has single layout (ModernSidebar + ModernNavbar)
- Views don't wrap in additional layout
- isMainLayout computed property correctly controls rendering

**Possible Causes**:
1. CSS issue (transform/position rendering twice)
2. Browser DevTools showing cached version
3. Component remounting on route change
4. CSS class conflict causing duplication in display

**Investigation**: Need to check CSS and component rendering logic

---

## WHAT'S WORKING

✅ **Authentication**: JWT created, stored, sent correctly  
✅ **API Service**: axios configured correctly  
✅ **Frontend Logic**: Views structured correctly  

---

## WHAT'S BROKEN

❌ **CORS Handling**: Not configured in Spring Security  
❌ **OPTIONS Preflight**: Blocked by security filter chain  
❌ **Cross-Origin Requests**: 403 error on all CRUD operations  

---

## SOLUTION

### Fix #1: Add CORS to Spring Security Chain (HIGH PRIORITY)
**File to Modify**: `SecurityConfig.java`
**Action**: Add `.cors()` configuration to security filter chain
**Impact**: OPTIONS requests will get CORS headers before JWT validation

### Fix #2: Skip JWT Filter for OPTIONS (MEDIUM PRIORITY)
**File to Modify**: `JwtAuthenticationFilter.java`
**Action**: Skip JWT validation for OPTIONS requests
**Impact**: Cleaner logic, prevents unnecessary JWT checks

### Fix #3: Allow OPTIONS in Authorization (MEDIUM PRIORITY)
**File to Modify**: `SecurityConfig.java`
**Action**: Explicitly permitAll for OPTIONS requests
**Impact**: Defense-in-depth, ensures OPTIONS always pass

---

## TEST PLAN

After fixes, verify:
```bash
# 1. OPTIONS preflight returns 200 with CORS headers
curl -X OPTIONS http://localhost:8081/api/users \
  -H "Origin: http://localhost:5174" \
  -v

# 2. GET request succeeds
curl http://localhost:8081/api/users \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Origin: http://localhost:5174"

# 3. Frontend page loads without CORS errors
npm run dev
# Visit http://localhost:5174/postes
# Check browser console - NO CORS errors
```

---

## FILES TO MODIFY

1. **SecurityConfig.java** - Add CORS bean and cors() config
2. **JwtAuthenticationFilter.java** - Skip JWT check for OPTIONS
3. (Optional) **CorsConfig.java** - May need adjustments

---

**Next Step**: Apply fixes to SecurityConfig.java
