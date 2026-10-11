# 6. SECURITY HARDENING GUIDE

## Executive Summary
Comprehensive security hardening checklist for production deployment of STEG Smart Monitoring System.

---

## 1. AUTHENTICATION & AUTHORIZATION

### JWT Token Configuration
```properties
# In application-prod.properties
jwt.secret=${JWT_SECRET}  # Min 32 characters
jwt.expiration=3600       # 1 hour
jwt.refresh-expiration=604800  # 7 days

# Ensure JWT_SECRET is strong
# Min 32 characters, mix of uppercase, lowercase, numbers, symbols
# Never commit to git, use environment variables only
```

### Password Security
```properties
# BCrypt configuration
spring.security.password-encoder.bcrypt.strength=10  # 10 rounds
# Never reduce below 10 rounds
```

### Token Refresh Strategy
```java
// Implement token refresh endpoint
POST /api/auth/refresh
Authorization: Bearer {refreshToken}

// Response includes new access token
{
  "token": "new-jwt-token",
  "refreshToken": "new-refresh-token",
  "expiresIn": 3600
}
```

### Logout & Token Revocation
```java
// Implement token blacklist/revocation
POST /api/auth/logout
Authorization: Bearer {token}

// Add token to revocation list
// Revoked tokens should be rejected on subsequent requests
```

---

## 2. RBAC & ACCESS CONTROL

### Verify @PreAuthorize Annotations
```java
// Every endpoint should have authorization check
@GetMapping
@PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
public ResponseEntity<?> getList() { ... }

@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> delete(@PathVariable Long id) { ... }
```

### Role-Based Dashboard Access
```java
@GetMapping("/dashboards/admin")
@PreAuthorize("hasRole('ADMIN')")
public DashboardResponse getAdminDashboard() { ... }

@GetMapping("/dashboards/agent")
@PreAuthorize("hasRole('AGENT')")
public DashboardResponse getAgentDashboard() { ... }
```

### Field-Level Security
```java
// Serialize only appropriate fields based on role
@JsonView(value = {Public.class, Internal.class})
private String securityField;

// Or use custom serializers
ObjectMapper mapper = new ObjectMapper();
mapper.setFilterProvider(/* role-based filter */);
```

---

## 3. CORS & CSRF Protection

### CORS Configuration
```properties
# application-prod.properties
spring.web.cors.allowed-origins=https://app.steg-monitoring.tn,https://admin.steg-monitoring.tn
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,PATCH
spring.web.cors.allowed-headers=Authorization,Content-Type
spring.web.cors.allow-credentials=true
spring.web.cors.max-age=3600
```

### CSRF Protection
```java
// Enable CSRF protection (should be default)
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .authorizeRequests()
            .antMatchers("/api/auth/login").permitAll()
            .anyRequest().authenticated();
        return http.build();
    }
}
```

---

## 4. DATA ENCRYPTION

### In-Transit Encryption (TLS/SSL)
```properties
# application-prod.properties
server.ssl.enabled=true
server.ssl.key-store=${SSL_KEYSTORE_PATH}
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat

# Redirect HTTP to HTTPS
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
```

### At-Rest Encryption (Database)
```sql
-- PostgreSQL native encryption (pgcrypto)
CREATE EXTENSION pgcrypto;

-- Encrypt sensitive fields
ALTER TABLE users ALTER COLUMN password 
  SET DEFAULT pgp_sym_encrypt('password', 'encryption_key');

-- Or use application-level encryption
-- Spring Data with @Convert annotation
@Convert(converter = EncryptedAttributeConverter.class)
private String sensitiveData;
```

### Field-Level Encryption
```java
@Column(name = "email")
@Convert(converter = EmailEncryptor.class)
private String email;

public class EmailEncryptor implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encrypt(attribute, getKey());
    }
    
    @Override
    public String convertToEntityAttribute(String encrypted) {
        return decrypt(encrypted, getKey());
    }
}
```

---

## 5. INPUT VALIDATION & SANITIZATION

### Server-Side Validation
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequest request) {
        // Validation handled by @Valid annotation
        // Request is validated before reaching controller method
        return ResponseEntity.status(201).body(userService.create(request));
    }
}

@Data
public class CreateUserRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be 8-128 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "Password must contain uppercase, lowercase, digit, and special character")
    private String password;
}
```

### SQL Injection Prevention
```java
// Use parameterized queries (JPA does this by default)
// ✅ GOOD
List<User> users = userRepository.findByEmail(email);

// ❌ BAD - NEVER do this
String query = "SELECT * FROM users WHERE email = '" + email + "'";

// If using native queries, use parameters
@Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
User findByEmail(@Param("email") String email);
```

### XSS Prevention
```java
// HTML escape output in templates
// Thymeleaf (automatically escapes by default)
<p th:text="${userInput}"></p>  <!-- Safe -->

// In JSON responses (Jackson automatically escapes)
{
  "message": "Hello &lt;script&gt;alert('xss')&lt;/script&gt;"
}

// Frontend - Vue automatically escapes
<p>{{ userInput }}</p>  <!-- Safe -->
```

---

## 6. API SECURITY

### Rate Limiting
```java
@Configuration
public class RateLimitConfig {
    @Bean
    public RateLimitingInterceptor rateLimitingInterceptor() {
        return new RateLimitingInterceptor(
            1000,  // 1000 requests per hour
            3600   // per hour
        );
    }
}
```

### API Key Management
```java
// If using API keys instead of JWT
@Component
public class ApiKeyValidator {
    private static final String API_KEY_HEADER = "X-API-Key";
    
    public boolean validate(String apiKey) {
        // Validate against stored hashed keys
        // Never compare plain text keys
        return bcrypt.matches(apiKey, storedHash);
    }
}
```

### Request Logging (without sensitive data)
```java
@Component
public class SecurityLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) {
        logger.info("Request: {} {}", request.getMethod(), request.getRequestURI());
        // Log everything EXCEPT: passwords, tokens, credit cards
        logger.info("Headers: {}", sanitizeHeaders(request.getHeaderNames()));
        filterChain.doFilter(request, response);
    }
    
    private Map<String, String> sanitizeHeaders(Enumeration<String> headers) {
        // Remove sensitive headers from logging
        List<String> sensitive = Arrays.asList("Authorization", "X-API-Key", "X-Token");
        // Return only safe headers
    }
}
```

---

## 7. SECURE CONFIGURATION

### Environment Variables
```bash
# Use environment variables, never hardcode secrets
export DB_PASSWORD="secure_password_here"
export JWT_SECRET="long_random_string_min_32_chars"
export SSL_KEYSTORE_PASSWORD="keystore_password"
export MAIL_PASSWORD="mail_server_password"

# Never commit to git
echo "*.env" >> .gitignore
echo "secrets.yml" >> .gitignore
```

### Configuration Externalization
```properties
# application.properties - Public/default config only
server.port=8080
spring.application.name=smart-monitoring

# application-prod.properties - Environment-specific
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```

### Secrets Management
```bash
# Use secrets manager (Vault, AWS Secrets Manager, etc.)
# Option 1: HashiCorp Vault
vault kv put secret/steg-prod db_password="password" jwt_secret="secret"

# Option 2: AWS Secrets Manager
aws secretsmanager create-secret --name steg-prod --secret-string '{...}'

# Option 3: Spring Cloud Config Server
# Encrypted properties in config server
```

---

## 8. LOGGING & MONITORING

### Secure Logging
```java
@Aspect
@Component
public class SensitiveDataMaskingAspect {
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("password[\"']?\\s*[:=]\\s*[\"']?([^\"'\\s,}]+)");
    private static final Pattern TOKEN_PATTERN = Pattern.compile("token[\"']?\\s*[:=]\\s*[\"']?([^\"'\\s,}]+)");
    
    @Around("@annotation(LogSafetyAware)")
    public Object logSafely(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        String output = maskSensitiveData(result.toString());
        logger.info(output);
        return result;
    }
    
    private String maskSensitiveData(String data) {
        data = PASSWORD_PATTERN.matcher(data).replaceAll("password=\"***\"");
        data = TOKEN_PATTERN.matcher(data).replaceAll("token=\"***\"");
        return data;
    }
}
```

### Audit Logging
```java
@Entity
@Audited
@AuditTable("user_audit")
public class User {
    // Envers automatically tracks changes
}

// Query audit history
AuditReader auditReader = AuditReaderFactory.get(entityManager);
List<Number> revisions = auditReader.getRevisions(User.class, userId);
User userV1 = auditReader.find(User.class, userId, revisions.get(0));
```

### Centralized Logging
```properties
# Send logs to centralized service (ELK, Splunk, etc.)
spring.cloud.stream.bindings.applicationMetrics.destination=spring-metrics

# Or use Log4j with remote appender
log4j.appender.remote=org.apache.log4j.net.SyslogAppender
log4j.appender.remote.SyslogHost=logs.example.com
```

---

## 9. DEPENDENCY SECURITY

### Vulnerability Scanning
```bash
# Check for known vulnerabilities
mvn dependency-check:check

# Update to latest safe versions
mvn versions:display-dependency-updates

# Compile with security manager
mvn clean package -Dhttps.protocols=TLSv1.2,TLSv1.3
```

### Dependency Verification
```xml
<!-- pom.xml -->
<dependencyManagement>
    <!-- Pin versions to known good versions -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-bom</artifactId>
            <version>3.1.5</version>  <!-- Latest stable -->
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 10. INFRASTRUCTURE SECURITY

### Firewall Configuration
```bash
# Allow only necessary ports
ufw allow 22/tcp   # SSH
ufw allow 80/tcp   # HTTP (redirect to HTTPS)
ufw allow 443/tcp  # HTTPS
ufw allow 5432/tcp # PostgreSQL (only from app server)
ufw deny incoming
ufw enable
```

### SSH Hardening
```bash
# Disable root login
echo "PermitRootLogin no" >> /etc/ssh/sshd_config

# Disable password auth, use keys only
echo "PasswordAuthentication no" >> /etc/ssh/sshd_config

# Change default port
echo "Port 2222" >> /etc/ssh/sshd_config

# Restart SSH
systemctl restart ssh
```

### Database Security
```sql
-- Limit database user permissions
REVOKE ALL ON DATABASE steg_production FROM steg_admin;
GRANT CONNECT ON DATABASE steg_production TO steg_admin;
GRANT USAGE ON SCHEMA public TO steg_admin;

-- Create read-only user for reports
CREATE USER steg_readonly WITH PASSWORD 'readonly_password';
GRANT SELECT ON ALL TABLES IN SCHEMA public TO steg_readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO steg_readonly;
```

---

## 11. SECURITY HEADERS

### HTTP Security Headers
```java
@Configuration
public class SecurityHeadersConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers()
            .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'")
            .and()
            .xssProtection()
            .and()
            .frameOptions().deny()
            .and()
            .httpStrictTransportSecurity().maxAgeInSeconds(31536000).includeSubDomains(true)
            .and()
            .referrerPolicy().policy(ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN);
        return http.build();
    }
}
```

### Response Headers
```java
@Component
public class SecurityHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        response.setHeader("Content-Security-Policy", "default-src 'self'");
        filterChain.doFilter(request, response);
    }
}
```

---

## 12. SECURITY AUDIT CHECKLIST

- [ ] All endpoints have @PreAuthorize annotations
- [ ] Password complexity requirements enforced
- [ ] JWT secret is >32 characters
- [ ] HTTPS/SSL configured for production
- [ ] CORS allowed-origins restricted
- [ ] Database encryption enabled
- [ ] Input validation on all endpoints
- [ ] SQL injection prevention verified
- [ ] XSS prevention verified
- [ ] Sensitive data not logged
- [ ] Rate limiting configured
- [ ] Audit logging enabled
- [ ] Secrets in environment variables only
- [ ] Firewall rules configured
- [ ] Database user permissions restricted
- [ ] Dependencies scanned for vulnerabilities
- [ ] Security headers configured
- [ ] Token refresh mechanism implemented
- [ ] Logout/revocation implemented
- [ ] Error messages don't leak information

---

## Security Incident Response

### If Security Breach Suspected
1. Isolate affected systems immediately
2. Preserve logs for investigation
3. Notify security team
4. Change all credentials/secrets
5. Audit database access logs
6. Review recent commits for malicious code
7. Perform vulnerability scan
8. Document timeline of events
9. Notify affected users (if data compromise)
10. Implement preventive measures

