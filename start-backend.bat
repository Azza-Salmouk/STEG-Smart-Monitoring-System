@echo off
cd /d "c:\Users\ASUS\Downloads\projet-steg\smart-monitoring-backend\smart-monitoring-backend"
set SPRING_PROFILES_ACTIVE=h2
call mvn spring-boot:run
