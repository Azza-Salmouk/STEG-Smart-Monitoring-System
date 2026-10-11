-- Query all table counts in steg_monitoring database
-- This script identifies all tables and counts records

SELECT 'USERS' as table_name, COUNT(*) as record_count FROM users
UNION ALL
SELECT 'POSTES', COUNT(*) FROM postes
UNION ALL
SELECT 'TRANSFORMATEURS', COUNT(*) FROM transformateurs
UNION ALL
SELECT 'MEASUREMENTS', COUNT(*) FROM measurements
UNION ALL
SELECT 'ALERTS', COUNT(*) FROM alerts
UNION ALL
SELECT 'INTERVENTIONS', COUNT(*) FROM interventions
UNION ALL
SELECT 'COMPLAINTS', COUNT(*) FROM complaints
UNION ALL
SELECT '--- SUBTOTAL (7 TABLES) ---' as table_name, 
  (SELECT COUNT(*) FROM users) +
  (SELECT COUNT(*) FROM postes) +
  (SELECT COUNT(*) FROM transformateurs) +
  (SELECT COUNT(*) FROM measurements) +
  (SELECT COUNT(*) FROM alerts) +
  (SELECT COUNT(*) FROM interventions) +
  (SELECT COUNT(*) FROM complaints)
ORDER BY table_name;
