#!/bin/bash

# STEG Smart Monitoring - Backend Dev Startup Script
# Bash script to set environment variables and run the backend

echo "======================================"
echo "STEG Smart Monitoring - Backend Setup"
echo "======================================"
echo ""

# Set environment variables
echo "Setting environment variables..."

# Database configuration
export DB_URL="jdbc:postgresql://localhost:5432/steg_monitoring"
export DB_USERNAME="steg_admin"
export DB_PASSWORD="steg_password"

# JWT configuration (dev-only secret)
export JWT_SECRET="dev-secret-key-NOT-FOR-PRODUCTION-CHANGE-THIS"
export JWT_EXPIRATION="86400000"

echo "✓ DB_URL: $DB_URL"
echo "✓ DB_USERNAME: $DB_USERNAME"
echo "✓ DB_PASSWORD: ***"
echo "✓ JWT_SECRET: ***"
echo ""

# Verify Maven
echo "Verifying Maven..."
mvn --version | head -1
echo ""

# Run the application
echo "Starting application..."
echo "Backend will run on http://localhost:8081"
echo ""

mvn spring-boot:run
