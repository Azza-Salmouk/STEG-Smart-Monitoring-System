# 🛠️ MAVEN & BUILD COMMANDS REFERENCE

## Build Commands

### Clean & Compile

```bash
# Clean et compiler sans tests
mvn clean compile

# Clean et compiler avec logs verbose
mvn clean compile -X

# Clean et compiler silencieux
mvn clean compile -q
```

### Package & Build

```bash
# Build JAR sans exécuter les tests
mvn clean package -DskipTests

# Build JAR avec tous les tests
mvn clean package

# Build JAR avec logs details
mvn clean package -e

# Build de manière rapide (parallèle)
mvn clean package -T 1C
```

### Run Application

```bash
# Via Maven plugin
mvn spring-boot:run

# Depuis le JAR
java -jar target/smart-monitoring-backend-0.0.1-SNAPSHOT.jar

# Avec un port différent
java -jar target/*.jar --server.port=9000

# Avec properties custom
java -jar target/*.jar \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/db_custom \
  --spring.datasource.username=user \
  --spring.datasource.password=pass
```

---

## Dependency Management

### List Dependencies

```bash
# Voir toutes les dépendances
mvn dependency:tree

# Voir dépendances conflictées
mvn dependency:tree -Dverbose

# Voir la dépendance pour un JAR
mvn dependency:resolve -DoutputFile=/tmp/deps.txt
```

### Update Dependencies

```bash
# Voir les updates disponibles
mvn versions:display-dependency-updates

# Appliquer les updates
mvn versions:use-latest-versions

# Checker les vulnerabilités
mvn org.owasp:dependency-check-maven:check
```

### Dependency Issues

```bash
# Réafficher les dépendances (purge local cache)
mvn dependency:purge-local-repository

# Forcer re-download
mvn clean dependency:resolve -U
```

---

## Testing

### Run All Tests

```bash
# Exécuter tous les tests
mvn test

# Tests avec couverture (si Jacoco configuré)
mvn clean test jacoco:report

# Tests avec logs
mvn test -X
```

### Run Specific Tests

```bash
# Une classe de test
mvn test -Dtest=AuthenticationControllerTest

# Une méthode de test
mvn test -Dtest=AuthenticationControllerTest#testLoginSuccess

# Pattern matching
mvn test -Dtest=*Service*Test
```

### Test Reports

```bash
# Voir rapport de test
target/surefire-reports/

# Générer rapport HTML
mvn surefire-report:report

# Consulter rapport
target/site/surefire-report.html
```

---

## Code Quality

### Code Analysis

```bash
# Compile check
mvn compiler:check

# SpotBugs (trouver bugs)
mvn spotbugs:check

# PMD (code violations)
mvn pmd:check

# CheckStyle
mvn checkstyle:check
```

### Build Complètement

```bash
# Build avec tous les checks
mvn clean verify

# Build rapide
mvn clean package -q -T 1C
```

---

## Documentation

### Generate Documentation

```bash
# Javadoc
mvn javadoc:javadoc

# Consulter
target/site/apidocs/index.html

# Swagger/Springdoc (si configuré)
mvn springdoc-openapi-maven-plugin:generate
```

---

## Debugging

### Debug Mode

```bash
# Exécuter en debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

# IDE attache debugger sur port 5005
```

### Verbose Logs

```bash
# Très détaillé
mvn clean package -X

# Erreurs seulement
mvn clean package -q --fail-fast

# Stack trace complet
mvn clean package -e
```

---

## Plugin Commands

### Spring Boot Plugin

```bash
# Lancer l'app
mvn spring-boot:run

# Build uber JAR
mvn spring-boot:repackage

# Stop l'app (background)
mvn spring-boot:stop
```

### Flyway (Database Migration - si utilisé)

```bash
# Voir status migrations
mvn flyway:info

# Appliquer migrations
mvn flyway:migrate

# Nettoyer DB (⚠️ Warning)
mvn flyway:clean
```

### Liquibase (Alternative à Flyway - si utilisé)

```bash
# Voir status
mvn liquibase:status

# Appliquer
mvn liquibase:update

# Rollback
mvn liquibase:rollback
```

---

## Profile-Specific Builds

### Available Profiles

```bash
# Voir les profils disponibles
mvn help:active-profiles

# Build avec profil dev
mvn clean package -P dev

# Build avec profil prod
mvn clean package -P prod

# Build avec multiples profils
mvn clean package -P dev,test
```

### Exemple pom.xml avec Profils

```xml
<profiles>
  <profile>
    <id>dev</id>
    <properties>
      <app.environment>development</app.environment>
    </properties>
  </profile>
  <profile>
    <id>prod</id>
    <properties>
      <app.environment>production</app.environment>
    </properties>
  </profile>
</profiles>
```

---

## Versioning

### Version Management

```bash
# Voir version actuelle
mvn help:describe -Dcmd=versions:set

# Bump version
mvn versions:set -DnewVersion=1.0.1

# Rollback version
mvn versions:revert

# Commit version change
mvn versions:commit
```

---

## Performance & Optimization

### Faster Builds

```bash
# Parallèle compilation (1 thread par core)
mvn clean package -T 1C

# Skip tests & javadoc
mvn clean package -DskipTests -Dspringdoc.skip=true

# Offline mode (dépendances en cache)
mvn clean package -o

# Threads + offline
mvn clean package -T 1C -o -DskipTests
```

### Memory Management

```bash
# Plus de mémoire pour Maven
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=512m"
mvn clean package

# ou inline
MAVEN_OPTS="-Xmx1024m" mvn clean package
```

---

## Common Issues & Solutions

### OUT OF MEMORY

```bash
# Augmenter la mémoire
export MAVEN_OPTS="-Xmx2048m"
mvn clean package
```

### Dependency Download Issues

```bash
# Forcer re-download des dépendances
mvn clean dependency:resolve -U

# Vérifier intégrité
mvn dependency:purge-local-repository
```

### Plugin Not Found

```bash
# Forcer update des plugins
mvn clean package -U

# ou purger plugin cache
rm -rf ~/.m2/repository/org/apache/maven/plugins/
```

### Test Failures

```bash
# Exécuter tests en verbose
mvn test -X

# Voir le rapport
cat target/surefire-reports/TEST-*.txt
```

---

## CI/CD Integration

### Jenkins

```bash
# Build stage
mvn clean package -DskipTests

# Test stage
mvn test

# Deploy stage
mvn deploy
```

### GitHub Actions

```yaml
name: Maven Build
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '21'
      - run: mvn clean package
```

### GitLab CI

```yaml
build:
  stage: build
  script:
    - mvn clean package -DskipTests
  artifacts:
    paths:
      - target/
```

---

## Advanced Commands

### Shade Plugin (Fat JAR)

```bash
# Créer JAR avec toutes les dépendances
mvn clean package shade:shade
```

### Assembly Plugin (Zip, Tar)

```bash
# Créer distribution
mvn clean package assembly:single
```

### Jar Plugin

```bash
# Créer JAR sans exécuter tests
mvn jar:jar
```

---

## Cleanup

### Clean Cache

```bash
# Nettoyer la cible
mvn clean

# Nettoyer le cache Maven
rm -rf ~/.m2/repository

# Nettoyer les fichiers temporaires
mvn clean:clean

# Nettoyer et reconstruire
mvn clean install -U
```

---

## Environment Variables

### Common MAVEN_OPTS

```bash
# Development (verbose logs)
export MAVEN_OPTS="-Xmx1024m -X"

# Production (optimized)
export MAVEN_OPTS="-Xmx2048m -XX:+UseG1GC -XX:+UseStringDeduplication"

# Debugging
export MAVEN_OPTS="-Xmx1024m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

### System Properties

```bash
# Property via CLI
mvn clean package -Dspring.profiles.active=prod

# Multiple properties
mvn clean package \
  -Dspring.datasource.url=jdbc:postgresql://prod-db:5432/steg \
  -Dspring.datasource.username=admin \
  -Dspring.datasource.password=secret
```

---

## Useful Shortcuts

```bash
# Alias (ajouter à ~/.bash_profile ou ~/.zshrc)
alias mvn-build="mvn clean package -DskipTests -T 1C"
alias mvn-test="mvn clean test"
alias mvn-clean="rm -rf ~/.m2/repository && mvn clean"
alias mvn-run="mvn spring-boot:run"

# Usage
mvn-build    # Quick build
mvn-test     # Run tests
mvn-run      # Run application
```

---

## Reference Card

| Command | Purpose | Time |
|---------|---------|------|
| `mvn clean` | Nettoyer | ~2s |
| `mvn compile` | Compiler | ~5s |
| `mvn test` | Tests | ~10s |
| `mvn package` | Build JAR | ~15s |
| `mvn verify` | Tous les checks | ~20s |
| `mvn install` | Install en local repo | ~3s |
| `mvn deploy` | Deploy en remote repo | ~5s |
| `mvn spring-boot:run` | Exécuter | ~5s |
| `mvn clean package -DskipTests -T 1C` | Build rapide | ~8s |

---

**Happy Building! 🔨**
