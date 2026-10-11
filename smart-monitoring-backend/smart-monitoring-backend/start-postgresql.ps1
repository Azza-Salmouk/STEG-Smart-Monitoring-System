$env:SPRING_PROFILES_ACTIVE = "dev"
$env:DB_URL = "jdbc:postgresql://localhost:5432/steg_monitoring"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "postgres"

Write-Host "================================================"
Write-Host "Backend STEG Monitoring - PostgreSQL Mode"
Write-Host "================================================"
Write-Host "Profile: dev (PostgreSQL)"
Write-Host "Database: steg_monitoring"
Write-Host "Host: localhost:5432"
Write-Host "User: postgres"
Write-Host "Port Backend: 8081"
Write-Host "================================================"
Write-Host ""

Set-Location "c:\Users\ASUS\Downloads\projet-steg\smart-monitoring-backend\smart-monitoring-backend"

Write-Host "Démarrage du backend..."
& mvn spring-boot:run 2>&1
