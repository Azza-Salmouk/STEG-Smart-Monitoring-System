# Script de lancement du backend avec H2 embedded

# Charger les variables d'env
$env:JWT_SECRET = "dev-secure-secret-key-for-testing-only-change-in-production"
$env:JWT_EXPIRATION = "86400000"
$env:SPRING_PROFILES_ACTIVE = "h2"

Write-Host "Demarrage du backend Spring Boot (H2 mode)..."
Write-Host "Profil: $env:SPRING_PROFILES_ACTIVE"
Write-Host ""

$jarPath = "c:\Users\ASUS\Downloads\projet-steg\smart-monitoring-backend\smart-monitoring-backend\target\smart-monitoring-backend-0.0.1-SNAPSHOT.jar"

if (-not (Test-Path $jarPath)) {
    Write-Host "JAR non trouve: $jarPath"
    exit 1
}

Write-Host "JAR trouve"
Write-Host ""
Write-Host "Demarrage en cours..."
Write-Host ""

& java -jar $jarPath --server.port=8081 --spring.profiles.active=h2
