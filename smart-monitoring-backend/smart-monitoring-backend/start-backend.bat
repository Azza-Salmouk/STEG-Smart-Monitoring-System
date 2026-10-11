@echo off
cd smart-monitoring-backend
set SPRING_PROFILES_ACTIVE=h2
mvn spring-boot:run
