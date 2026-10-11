# 8. PERFORMANCE TUNING GUIDE

## Overview
Strategies and techniques for optimizing STEG Smart Monitoring System performance.

---

## 1. DATABASE OPTIMIZATION

### Index Strategy

#### Essential Indexes
```sql
-- User queries
CREATE INDEX idx_users_email ON users(email) WHERE status = 'ACTIVE';

-- Transformateur queries
CREATE INDEX idx_transformateurs_poste ON transformateurs(poste_id, status);
CREATE INDEX idx_transformateurs_health ON transformateurs(health_score DESC);
CREATE INDEX idx_transformateurs_ref ON transformateurs(reference);

-- Measurement queries (most frequent)
CREATE INDEX idx_measurements_transformateur_date ON measurements(transformateur_id, measurement_date DESC);
CREATE INDEX idx_measurements_date ON measurements(measurement_date DESC);
CREATE INDEX idx_measurements_status ON measurements(status);

-- Alert queries
CREATE INDEX idx_alerts_transformateur_status ON alerts(transformateur_id, status);
CREATE INDEX idx_alerts_created ON alerts(created_at DESC);
CREATE INDEX idx_alerts_level ON alerts(level);

-- Intervention queries
CREATE INDEX idx_interventions_agent_status ON interventions(agent_id, status);
CREATE INDEX idx_interventions_date ON interventions(date_planifiee DESC);
CREATE INDEX idx_interventions_transformateur ON interventions(transformateur_id);

-- Complaint queries
CREATE INDEX idx_complaints_status ON complaints(status);
CREATE INDEX idx_complaints_date ON complaints(created_at DESC);
```

#### Composite Indexes (for WHERE + ORDER BY)
```sql
-- Common query patterns
CREATE INDEX idx_measurements_composite ON measurements(
    transformateur_id, 
    measurement_date DESC, 
    status
);

CREATE INDEX idx_alerts_composite ON alerts(
    transformateur_id,
    status,
    created_at DESC
);
```

### Query Optimization

#### Example: Slow Query
```sql
-- ❌ SLOW - Full table scan, no indexes
SELECT m.* 
FROM measurements m
JOIN transformateurs t ON m.transformateur_id = t.id
WHERE t.status = 'ACTIF' 
ORDER BY m.measurement_date DESC
LIMIT 100;
```

#### Optimized Query
```sql
-- ✅ FAST - Uses indexes, filtering at source
SELECT m.* 
FROM measurements m
WHERE m.transformateur_id IN (
    SELECT id FROM transformateurs 
    WHERE status = 'ACTIF'
)
ORDER BY m.measurement_date DESC
LIMIT 100;
```

### Statistics & Maintenance

```sql
-- Analyze table statistics (optimizer uses this for query planning)
ANALYZE transformateurs;
ANALYZE measurements;
ANALYZE alerts;

-- Vacuum to reclaim space
VACUUM transformateurs;
VACUUM measurements;

-- Full maintenance (use during low-traffic periods)
VACUUM FULL;
ANALYZE;

-- Check index bloat
SELECT 
    schemaname, tablename, indexname,
    pg_size_pretty(pg_relation_size(indexrelid)) AS index_size
FROM pg_stat_user_indexes
ORDER BY pg_relation_size(indexrelid) DESC;

-- Reindex if bloated
REINDEX INDEX idx_measurements_transformateur_date;
```

### Connection Pooling

```properties
# application-prod.properties - HikariCP configuration
spring.datasource.hikari.maximum-pool-size=30        # Max connections
spring.datasource.hikari.minimum-idle=5              # Min idle connections
spring.datasource.hikari.connection-timeout=20000    # 20s to acquire connection
spring.datasource.hikari.idle-timeout=300000         # 5m idle before close
spring.datasource.hikari.max-lifetime=1200000        # 20m connection lifespan
spring.datasource.hikari.auto-commit=true
spring.datasource.hikari.leak-detection-threshold=60000  # Detect 1m+ leaks
```

### Query Caching

```properties
# Enable second-level cache for frequently accessed data
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.use_query_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory

# Cache provider configuration
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=10000,expireAfterWrite=5m
```

---

## 2. APPLICATION OPTIMIZATION

### Spring Boot Configuration

```properties
# Disable unnecessary features
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.generate_statistics=false
spring.h2.console.enabled=false

# Eager vs Lazy loading optimization
spring.jpa.properties.hibernate.default_batch_fetch_size=20
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.jdbc.fetch_size=50

# Connection pooling
server.tomcat.max-threads=200
server.tomcat.min-spare-threads=10
server.tomcat.max-connections=10000
```

### Entity Optimization

#### Lazy Loading
```java
@Entity
public class Transformateur {
    @Id
    private Long id;
    
    private String reference;
    
    // ✅ GOOD - Lazy load expensive relationships
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transformateur")
    private List<Measurement> measurements;
    
    // ✅ GOOD - Use DTO for list endpoints
    @Transient
    private Integer measurementCount;
}
```

#### DTO Projection
```java
// ✅ GOOD - Return only needed fields
@Query("SELECT new com.steg.dto.TransformateurDTO(t.id, t.reference, t.healthScore) FROM Transformateur t")
List<TransformateurDTO> findAllDTO();

public class TransformateurDTO {
    private Long id;
    private String reference;
    private Double healthScore;
    
    public TransformateurDTO(Long id, String reference, Double healthScore) {
        this.id = id;
        this.reference = reference;
        this.healthScore = healthScore;
    }
}
```

#### Pagination
```java
// ✅ GOOD - Always paginate list endpoints
@GetMapping
public Page<TransformateurDTO> getAll(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
) {
    return repository.findAllDTO(PageRequest.of(page, size));
}

// ❌ AVOID - This loads ALL records into memory
public List<Transformateur> getAll() {
    return repository.findAll();  // DO NOT DO THIS
}
```

### API Response Optimization

#### Compression
```properties
# Enable gzip compression
server.compression.enabled=true
server.compression.min-response-size=1024
server.compression.mime-types=application/json,text/plain,text/html
```

#### JSON Serialization
```java
// ✅ GOOD - Exclude unnecessary fields
@JsonView(Public.class)
private String publicField;

@JsonView(Internal.class)
private String internalField;

// Use view in controller
return new ResponseEntity<>(
    mapper.writerWithView(Public.class).writeValueAsString(data),
    OK
);
```

---

## 3. FRONTEND OPTIMIZATION

### Bundle Size Analysis
```bash
# Analyze bundle
npm run build --report

# Or use webpack-bundle-analyzer
npm install --save-dev webpack-bundle-analyzer
```

### Code Splitting
```javascript
// ✅ GOOD - Lazy load routes
const Dashboard = () => import('@/views/Dashboard.vue');
const TransformateurDetail = () => import('@/views/TransformateurDetail.vue');

// In router
{
  path: '/dashboard',
  component: Dashboard,
  meta: { requiresAuth: true }
}
```

### Image Optimization
```vue
<!-- ✅ GOOD - Use optimized images -->
<img src="@/assets/logo.webp" alt="Logo" loading="lazy">

<!-- Use picture element for responsive images -->
<picture>
  <source srcset="image.webp" type="image/webp">
  <source srcset="image.jpg" type="image/jpeg">
  <img src="image.jpg" alt="Description">
</picture>
```

### Cache Strategy
```javascript
// Service Worker for offline capability and caching
if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('/sw.js')
    .then(reg => console.log('SW registered'))
    .catch(err => console.log('SW error'));
}
```

### API Call Optimization
```javascript
// ✅ GOOD - Batch requests with axios
const [dashData, alerts, interventions] = await Promise.all([
  api.get('/dashboards/admin'),
  api.get('/alerts?status=ACTIVE'),
  api.get('/interventions?status=PENDING')
]);

// ✅ GOOD - Debounce search
const searchTransformateurs = debounce((query) => {
  api.get(`/transformateurs?search=${query}`);
}, 300);

// ✅ GOOD - Implement request cancellation
const controller = new AbortController();
api.get('/data', { signal: controller.signal });
// Cancel if request takes too long
setTimeout(() => controller.abort(), 5000);
```

---

## 4. CACHING STRATEGIES

### Redis Caching
```properties
# application-prod.properties
spring.redis.host=localhost
spring.redis.port=6379
spring.cache.type=redis

# Cache configuration
spring.cache.redis.time-to-live=3600000  # 1 hour
spring.cache.redis.key-prefix=steg:
```

### Cache Invalidation
```java
@Service
public class TransformateurService {
    @Cacheable(value = "transformateurs", key = "#id")
    public Transformateur getById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    @CacheEvict(value = "transformateurs", key = "#result.id")
    public Transformateur save(Transformateur t) {
        return repository.save(t);
    }
    
    @CacheEvict(value = "transformateurs", allEntries = true)
    public void updateAll() {
        // Invalidate entire cache
    }
}
```

### Cache Warming
```java
@Component
public class CacheWarmupService {
    
    @EventListener(ApplicationReadyEvent.class)
    public void warmCache() {
        log.info("Warming up cache...");
        
        // Pre-load frequently accessed data
        List<Poste> postes = posteRepository.findAll();
        postes.forEach(p -> {
            transformateurService.getByPosteId(p.getId());
        });
        
        log.info("Cache warming complete");
    }
}
```

---

## 5. MONITORING & PROFILING

### Application Metrics
```properties
# Enable Spring Boot Actuator
management.endpoints.web.exposure.include=metrics,health,info
management.endpoints.web.base-path=/actuator
management.metrics.enable.jvm=true
management.metrics.enable.process=true
```

### Performance Metrics to Monitor
```java
// JVM Memory
runtime.jvm.memory.usage

// Garbage Collection
runtime.jvm.gc.pause

// Thread Count
runtime.jvm.threads.live

// Database Connection Pool
hikaricp.connections.active
hikaricp.connections.idle
hikaricp.connections.pending

// HTTP Requests
http.server.requests (count, total time)
```

### Distributed Tracing
```properties
# Spring Cloud Sleuth + Zipkin
spring.sleuth.enabled=true
spring.sleuth.sampler.probability=0.1  # Sample 10% of requests

# Zipkin backend
spring.zipkin.base-url=http://localhost:9411
```

### Slow Query Logging
```properties
# PostgreSQL slow query log
log_min_duration_statement = 1000  # Log queries >1s

# Hibernate slow query logging
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.stat=DEBUG
```

---

## 6. LOAD TESTING

### JMeter Test Plan
```bash
# Install JMeter
# Create test with:
# - Thread Group: 100 users
# - Ramp-up: 30 seconds
# - Duration: 5 minutes
# - GET /api/transformateurs requests

jmeter -n -t steg_load_test.jmx -l results.jtl -j jmeter.log
```

### Expected Performance Targets
- **API Response Time**: <500ms (p95)
- **Throughput**: >1000 req/sec
- **Error Rate**: <0.1%
- **CPU Usage**: <70%
- **Memory Usage**: <75% allocated

---

## 7. OPTIMIZATION CHECKLIST

### Database
- [ ] Indexes created for all frequently queried columns
- [ ] Statistics analyzed (ANALYZE run)
- [ ] Slow query log reviewed
- [ ] Connection pool sized appropriately
- [ ] Query plans verified with EXPLAIN ANALYZE
- [ ] Partitioning implemented for large tables

### Application
- [ ] JPA lazy loading configured
- [ ] DTOs used for list endpoints
- [ ] Pagination implemented
- [ ] Caching enabled for hot data
- [ ] Compression enabled
- [ ] Connection pool optimized

### Frontend
- [ ] Bundle size <500KB (gzipped)
- [ ] Images optimized and lazy-loaded
- [ ] Code splitting implemented
- [ ] API calls batched/debounced
- [ ] Service worker for caching
- [ ] CSS/JS minified

### Infrastructure
- [ ] Load balancer configured
- [ ] CDN for static assets
- [ ] Monitoring/alerting active
- [ ] Backup strategy automated
- [ ] Disaster recovery tested

---

## Performance Baseline

After optimization, target metrics:

| Metric | Target | Current |
|--------|--------|---------|
| API Response Time (p95) | <500ms | ___ |
| Database Query Time | <100ms | ___ |
| Frontend Load Time | <3s | ___ |
| Throughput (req/sec) | >500 | ___ |
| Error Rate | <0.1% | ___ |
| CPU Usage | <60% | ___ |
| Memory Usage | <70% | ___ |

