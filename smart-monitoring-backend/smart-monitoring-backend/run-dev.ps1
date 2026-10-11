# STEG Smart Monitoring - Backend Dev Startup Script
# PowerShell script to set environment variables and run the backend

Write-Host "======================================"
Write-Host "STEG Smart Monitoring - Backend Setup"
Write-Host "======================================"
Write-Host ""

# Set environment variables
Write-Host "Setting environment variables..."

# Database configuration
$env:DB_URL = "jdbc:postgresql://localhost:5432/steg_monitoring"
$env:DB_USERNAME = "steg_admin"
$env:DB_PASSWORD = "steg_password"

# JWT configuration (dev-only secret)
$env:JWT_SECRET = "dev-secret-key-NOT-FOR-PRODUCTION-CHANGE-THIS"
$env:JWT_EXPIRATION = "86400000"

Write-Host "✓ DB_URL: $env:DB_URL"
Write-Host "✓ DB_USERNAME: $env:DB_USERNAME"
Write-Host "✓ DB_PASSWORD: ***"
Write-Host "✓ JWT_SECRET: ***"
Write-Host ""

# Verify Maven
Write-Host "Verifying Maven..."
$mvnVersion = mvn --version | Select-Object -First 1
Write-Host "✓ $mvnVersion"
Write-Host ""

# Run the application
Write-Host "Starting application..."
Write-Host "Backend will run on http://localhost:8081"
Write-Host ""

mvn spring-boot:run
