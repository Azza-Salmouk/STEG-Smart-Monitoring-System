-- ====================================================================
-- DONNÉES DE TEST POUR DASHBOARDS - H2 Database
-- ====================================================================

-- UTILISATEURS
INSERT INTO "user" (email, password, role, username, created_at) VALUES
('admin@steg.tn', '$2a$10$nT3mKYVVVNmXP3CEYLvSvuOEPpNHpDEjq14tI5C8MUz5F8C2VF4Wy', 'ADMIN', 'Admin User', NOW()),
('supervisor@steg.tn', '$2a$10$nT3mKYVVVNmXP3CEYLvSvuOEPpNHpDEjq14tI5C8MUz5F8C2VF4Wy', 'SUPERVISOR', 'Supervisor User', NOW()),
('agent@steg.tn', '$2a$10$nT3mKYVVVNmXP3CEYLvSvuOEPpNHpDEjq14tI5C8MUz5F8C2VF4Wy', 'AGENT', 'Agent User', NOW()),
('customer@steg.tn', '$2a$10$nT3mKYVVVNmXP3CEYLvSvuOEPpNHpDEjq14tI5C8MUz5F8C2VF4Wy', 'SERVICE_CLIENT', 'Customer User', NOW());

-- POSTES
INSERT INTO poste (code, nom, region, created_at) VALUES
('POST001', 'Poste Centre-Ville', 'Tunis', NOW()),
('POST002', 'Poste Carthage', 'Tunis', NOW()),
('POST003', 'Poste Sfax', 'Sfax', NOW()),
('POST004', 'Poste Sousse', 'Sousse', NOW()),
('POST005', 'Poste Gabès', 'Gabès', NOW()),
('POST006', 'Poste Bizerte', 'Bizerte', NOW()),
('POST007', 'Poste Gafsa', 'Gafsa', NOW()),
('POST008', 'Poste Tataouine', 'Tataouine', NOW()),
('POST009', 'Poste Kairouan', 'Kairouan', NOW()),
('POST010', 'Poste Tozeur', 'Tozeur', NOW()),
('POST011', 'Poste Jendouba', 'Jendouba', NOW()),
('POST012', 'Poste Kasserine', 'Kasserine', NOW()),
('POST013', 'Poste Kef', 'Kef', NOW()),
('POST014', 'Poste Siliana', 'Siliana', NOW()),
('POST015', 'Poste Medenine', 'Medenine', NOW()),
('POST016', 'Poste Ben Arous', 'Ben Arous', NOW()),
('POST017', 'Poste Manouba', 'Manouba', NOW()),
('POST018', 'Poste Ariana', 'Ariana', NOW()),
('POST019', 'Poste Monastir', 'Monastir', NOW()),
('POST020', 'Poste Mahdia', 'Mahdia', NOW());

-- TRANSFORMATEURS
-- Pour chaque poste, ajouter des transformateurs avec différents états
INSERT INTO transformateur (reference, poste_id, voltage_primaire, voltage_secondaire, capacite, etat, created_at) VALUES
-- Poste 1 - 10 transformateurs
('TRF001001', 1, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF001002', 1, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF001003', 1, 110.0, 20.0, 50.0, 'AVERTISSEMENT', NOW()),
('TRF001004', 1, 110.0, 20.0, 50.0, 'CRITIQUE', NOW()),
('TRF001005', 1, 220.0, 10.0, 30.0, 'NORMAL', NOW()),
('TRF001006', 1, 220.0, 10.0, 30.0, 'NORMAL', NOW()),
('TRF001007', 1, 220.0, 10.0, 30.0, 'AVERTISSEMENT', NOW()),
('TRF001008', 1, 220.0, 10.0, 30.0, 'NORMAL', NOW()),
('TRF001009', 1, 110.0, 15.0, 40.0, 'NORMAL', NOW()),
('TRF001010', 1, 110.0, 15.0, 40.0, 'CRITIQUE', NOW()),

-- Poste 2 - 8 transformateurs
('TRF002001', 2, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF002002', 2, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF002003', 2, 110.0, 20.0, 50.0, 'AVERTISSEMENT', NOW()),
('TRF002004', 2, 220.0, 10.0, 30.0, 'NORMAL', NOW()),
('TRF002005', 2, 220.0, 10.0, 30.0, 'NORMAL', NOW()),
('TRF002006', 2, 110.0, 15.0, 40.0, 'NORMAL', NOW()),
('TRF002007', 2, 110.0, 15.0, 40.0, 'NORMAL', NOW()),
('TRF002008', 2, 220.0, 10.0, 30.0, 'CRITIQUE', NOW()),

-- Autres postes - Distribution simple
('TRF003001', 3, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF003002', 3, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF003003', 3, 110.0, 20.0, 50.0, 'AVERTISSEMENT', NOW()),

('TRF004001', 4, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF004002', 4, 110.0, 20.0, 50.0, 'NORMAL', NOW()),

('TRF005001', 5, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF005002', 5, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF005003', 5, 220.0, 10.0, 30.0, 'CRITIQUE', NOW()),

('TRF006001', 6, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF006002', 6, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF006003', 6, 110.0, 20.0, 50.0, 'AVERTISSEMENT', NOW()),
('TRF006004', 6, 220.0, 10.0, 30.0, 'NORMAL', NOW()),

('TRF007001', 7, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF007002', 7, 110.0, 20.0, 50.0, 'AVERTISSEMENT', NOW()),

('TRF008001', 8, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF008002', 8, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF008003', 8, 220.0, 10.0, 30.0, 'CRITIQUE', NOW()),

('TRF009001', 9, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF009002', 9, 110.0, 20.0, 50.0, 'NORMAL', NOW()),

('TRF010001', 10, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF010002', 10, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF010003', 10, 220.0, 10.0, 30.0, 'AVERTISSEMENT', NOW()),

('TRF011001', 11, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF011002', 11, 110.0, 20.0, 50.0, 'NORMAL', NOW()),

('TRF012001', 12, 110.0, 20.0, 50.0, 'NORMAL', NOW()),

('TRF013001', 13, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF013002', 13, 110.0, 20.0, 50.0, 'AVERTISSEMENT', NOW()),

('TRF014001', 14, 110.0, 20.0, 50.0, 'NORMAL', NOW()),

('TRF015001', 15, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF015002', 15, 110.0, 20.0, 50.0, 'CRITIQUE', NOW()),

('TRF016001', 16, 110.0, 20.0, 50.0, 'NORMAL', NOW()),

('TRF017001', 17, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF017002', 17, 110.0, 20.0, 50.0, 'NORMAL', NOW()),

('TRF018001', 18, 110.0, 20.0, 50.0, 'NORMAL', NOW()),

('TRF019001', 19, 110.0, 20.0, 50.0, 'NORMAL', NOW()),
('TRF019002', 19, 110.0, 20.0, 50.0, 'AVERTISSEMENT', NOW()),

('TRF020001', 20, 110.0, 20.0, 50.0, 'NORMAL', NOW());

-- INTERVENTIONS
INSERT INTO intervention (reference, transformateur_id, agent_id, statut, priorite, created_at) VALUES
-- Agent 1 interventions
('INT001', 1, 3, 'EN_ATTENTE', 'CRITIQUE', NOW()),
('INT002', 1, 3, 'EN_COURS', 'HAUTE', DATE_ADD(NOW(), INTERVAL -1 DAY)),
('INT003', 2, 3, 'TERMINEE', 'NORMALE', DATE_ADD(NOW(), INTERVAL -5 DAY)),
('INT004', 3, 3, 'EN_ATTENTE', 'HAUTE', NOW()),
('INT005', 4, 3, 'EN_COURS', 'CRITIQUE', DATE_ADD(NOW(), INTERVAL -2 DAY)),
('INT006', 5, 3, 'TERMINEE', 'NORMALE', DATE_ADD(NOW(), INTERVAL -10 DAY)),
('INT007', 6, 3, 'EN_ATTENTE', 'NORMALE', NOW()),
('INT008', 7, 3, 'TERMINEE', 'BASSE', DATE_ADD(NOW(), INTERVAL -7 DAY)),
('INT009', 8, 3, 'EN_COURS', 'HAUTE', DATE_ADD(NOW(), INTERVAL -3 DAY)),
('INT010', 9, 3, 'EN_ATTENTE', 'CRITIQUE', NOW()),

-- Agent 2 (if exists) interventions - using agent_id 2
('INT011', 10, 2, 'EN_COURS', 'HAUTE', DATE_ADD(NOW(), INTERVAL -1 DAY)),
('INT012', 11, 2, 'TERMINEE', 'NORMALE', DATE_ADD(NOW(), INTERVAL -5 DAY)),
('INT013', 12, 2, 'EN_ATTENTE', 'CRITIQUE', NOW()),

-- General interventions for statistics
('INT014', 15, 3, 'TERMINEE', 'NORMALE', DATE_ADD(NOW(), INTERVAL -15 DAY)),
('INT015', 20, 2, 'EN_COURS', 'HAUTE', DATE_ADD(NOW(), INTERVAL -2 DAY)),
('INT016', 25, 3, 'TERMINEE', 'BASSE', DATE_ADD(NOW(), INTERVAL -20 DAY)),
('INT017', 30, 2, 'EN_ATTENTE', 'NORMALE', DATE_ADD(NOW(), INTERVAL -1 DAY)),
('INT018', 35, 3, 'TERMINEE', 'HAUTE', DATE_ADD(NOW(), INTERVAL -25 DAY)),
('INT019', 40, 2, 'EN_COURS', 'CRITIQUE', NOW()),
('INT020', 45, 3, 'EN_ATTENTE', 'NORMALE', NOW());

-- ALERTS
INSERT INTO alert (titre, description, niveau, statut, transformateur_id, created_at) VALUES
-- Critical alerts
('Surtension détectée', 'Tension supérieure au seuil', 'CRITIQUE', 'ACTIVE', 1, NOW()),
('Surcharge imminente', 'Courant approche de la limite', 'CRITIQUE', 'ACTIVE', 4, DATE_ADD(NOW(), INTERVAL -1 DAY)),
('Température critique', 'Transformateur surchauffé', 'CRITIQUE', 'ACKNOWLEDGED', 10, DATE_ADD(NOW(), INTERVAL -3 DAY)),

-- High alerts
('Harmoniques élevées', 'Distorsion harmonique détectée', 'HAUTE', 'ACTIVE', 3, NOW()),
('Tension anormale', 'Chute de tension significative', 'HAUTE', 'ACTIVE', 7, DATE_ADD(NOW(), INTERVAL -1 DAY)),
('Désequilibre de phase', 'Asymétrie détectée', 'HAUTE', 'ACKNOWLEDGED', 15, DATE_ADD(NOW(), INTERVAL -5 DAY)),
('Courant de fuite', 'Isolement dégradé', 'HAUTE', 'ACTIVE', 2, DATE_ADD(NOW(), INTERVAL -2 DAY)),

-- Medium alerts
('Variation de fréquence', 'Fréquence instable', 'MOYENNE', 'ACTIVE', 5, NOW()),
('Perte reactive', 'Consommation reactive élevée', 'MOYENNE', 'ACKNOWLEDGED', 8, DATE_ADD(NOW(), INTERVAL -4 DAY)),
('Température élevée', 'Temp. transformateur au-dessus du seuil', 'MOYENNE', 'ACTIVE', 12, DATE_ADD(NOW(), INTERVAL -1 DAY)),

-- Low alerts
('Humidité élevée', 'Taux d''humidité en surveillance', 'BASSE', 'ACKNOWLEDGED', 6, DATE_ADD(NOW(), INTERVAL -10 DAY)),
('Maintenance préventive due', 'Révision périodique nécessaire', 'BASSE', 'ACTIVE', 20, DATE_ADD(NOW(), INTERVAL -30 DAY)),
('Calibrage de capteur', 'Capteur à recalibrer', 'BASSE', 'ACKNOWLEDGED', 9, DATE_ADD(NOW(), INTERVAL -7 DAY));

-- RÉCLAMATIONS
INSERT INTO complaint (reference, description, categorie, priorite, statut, created_at) VALUES
('REC001', 'Coupure d''électricité prolongée', 'COUPURE', 'CRITICAL', 'NOUVELLE', NOW()),
('REC002', 'Fluctuations de tension', 'SURCHARGE', 'HIGH', 'EN_ANALYSE', DATE_ADD(NOW(), INTERVAL -1 DAY)),
('REC003', 'Transformateur défaillant', 'PANNE', 'CRITICAL', 'ASSIGNEE', DATE_ADD(NOW(), INTERVAL -2 DAY)),
('REC004', 'Bruit anormal du transformateur', 'BRUIT', 'MEDIUM', 'EN_TRAITEMENT', DATE_ADD(NOW(), INTERVAL -3 DAY)),
('REC005', 'Odeur de brûlé', 'ODEUR', 'HIGH', 'NOUVELLE', NOW()),
('REC006', 'Facturation incorrecte', 'FACTURATION', 'MEDIUM', 'RESOLUE', DATE_ADD(NOW(), INTERVAL -10 DAY)),
('REC007', 'Surcharge dans la zone', 'SURCHARGE', 'CRITICAL', 'EN_TRAITEMENT', DATE_ADD(NOW(), INTERVAL -5 DAY)),
('REC008', 'Transformateur en panne', 'PANNE', 'CRITICAL', 'ASSIGNEE', DATE_ADD(NOW(), INTERVAL -7 DAY)),
('REC009', 'Problème de qualité électrique', 'AUTRE', 'MEDIUM', 'EN_ANALYSE', DATE_ADD(NOW(), INTERVAL -2 DAY)),
('REC010', 'Problème résolu', 'COUPURE', 'LOW', 'RESOLUE', DATE_ADD(NOW(), INTERVAL -15 DAY)),
('REC011', 'Nouvelle défaillance', 'PANNE', 'CRITICAL', 'NOUVELLE', DATE_ADD(NOW(), INTERVAL -1 DAY)),
('REC012', 'Maintenance urgente', 'AUTRE', 'HIGH', 'EN_TRAITEMENT', NOW());

-- MESURES (IoT measurements)
INSERT INTO measurement (transformateur_id, tension, courant, puissance, temperature, timestamp) VALUES
-- Recent measurements for TRF001001
(1, 110.5, 45.2, 4885, 65.3, DATE_ADD(NOW(), INTERVAL -5 MINUTE)),
(1, 110.3, 46.1, 5019, 66.1, DATE_ADD(NOW(), INTERVAL -4 MINUTE)),
(1, 110.7, 45.8, 5069, 65.8, DATE_ADD(NOW(), INTERVAL -3 MINUTE)),
(1, 110.2, 45.5, 5005, 66.0, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(1, 110.4, 46.0, 5064, 65.9, DATE_ADD(NOW(), INTERVAL -1 MINUTE)),

-- Measurements for TRF001003 (Avertissement)
(3, 112.1, 48.5, 5434, 72.5, DATE_ADD(NOW(), INTERVAL -3 MINUTE)),
(3, 112.3, 49.0, 5523, 73.1, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(3, 112.0, 48.7, 5471, 72.8, DATE_ADD(NOW(), INTERVAL -1 MINUTE)),

-- Measurements for TRF001004 (Critique)
(4, 115.5, 50.2, 5796, 80.5, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(4, 115.8, 51.0, 5919, 81.2, DATE_ADD(NOW(), INTERVAL -1 MINUTE)),

-- Measurements for other transformers
(2, 110.3, 44.8, 4928, 64.9, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(5, 110.1, 43.2, 4752, 62.5, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(6, 110.5, 45.0, 4950, 65.2, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(7, 112.5, 47.2, 5309, 71.8, DATE_ADD(NOW(), INTERVAL -3 MINUTE)),
(8, 110.2, 44.5, 4895, 64.8, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(9, 110.4, 45.3, 4983, 65.5, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(10, 114.8, 50.0, 5740, 79.2, DATE_ADD(NOW(), INTERVAL -3 MINUTE)),
(15, 110.6, 45.2, 4988, 65.3, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(20, 110.3, 44.9, 4939, 65.0, DATE_ADD(NOW(), INTERVAL -2 MINUTE)),
(25, 110.5, 46.0, 5060, 65.9, DATE_ADD(NOW(), INTERVAL -2 MINUTE));

COMMIT;
