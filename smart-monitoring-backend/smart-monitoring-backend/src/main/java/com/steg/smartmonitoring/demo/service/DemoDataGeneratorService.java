package com.steg.smartmonitoring.demo.service;

import com.steg.smartmonitoring.alert.entity.Alert;
import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertType;
import com.steg.smartmonitoring.alert.repository.AlertRepository;
import com.steg.smartmonitoring.complaint.entity.Complaint;
import com.steg.smartmonitoring.complaint.entity.ComplaintCategory;
import com.steg.smartmonitoring.complaint.entity.ComplaintPriority;
import com.steg.smartmonitoring.complaint.entity.ComplaintStatus;
import com.steg.smartmonitoring.complaint.repository.ComplaintRepository;
import com.steg.smartmonitoring.intervention.entity.Intervention;
import com.steg.smartmonitoring.intervention.entity.Priorite;
import com.steg.smartmonitoring.intervention.entity.StatutIntervention;
import com.steg.smartmonitoring.intervention.entity.TypeIntervention;
import com.steg.smartmonitoring.intervention.repository.InterventionRepository;
import com.steg.smartmonitoring.measurement.analyzer.MeasurementAnalyzer;
import com.steg.smartmonitoring.measurement.calculator.HealthScoreCalculator;
import com.steg.smartmonitoring.measurement.entity.Measurement;
import com.steg.smartmonitoring.measurement.entity.MeasurementStatus;
import com.steg.smartmonitoring.measurement.repository.MeasurementRepository;
import com.steg.smartmonitoring.poste.entity.EtatPoste;
import com.steg.smartmonitoring.poste.entity.Poste;
import com.steg.smartmonitoring.poste.entity.TypePoste;
import com.steg.smartmonitoring.poste.repository.PosteRepository;
import com.steg.smartmonitoring.transformateur.entity.RiskLevel;
import com.steg.smartmonitoring.transformateur.entity.StatusTransformateur;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.transformateur.repository.TransformateurRepository;
import com.steg.smartmonitoring.user.entity.User;
import com.steg.smartmonitoring.user.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service de génération de données de démonstration
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DemoDataGeneratorService {

    private final PosteRepository posteRepository;
    private final TransformateurRepository transformateurRepository;
    private final MeasurementRepository measurementRepository;
    private final AlertRepository alertRepository;
    private final InterventionRepository interventionRepository;
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final HealthScoreCalculator healthScoreCalculator;
    private final MeasurementAnalyzer measurementAnalyzer;

    private static final Random random = new Random();

    /**
     * Génère toutes les données de démonstration
     */
    @Transactional
    public GeneratedDataSummary generateAllData() {
        GeneratedDataSummary summary = new GeneratedDataSummary();
        
        // Postes
        summary.postesCount = generatePostes();
        
        // Transformateurs
        summary.transformateursCount = generateTransformateurs();
        
        // Mesures et alertes
        summary.measurementsCount = generateMeasurements();
        summary.alertsCount = countAlerts();
        
        // Interventions
        summary.interventionsCount = generateInterventions();
        
        // Plaintes
        summary.complaintsCount = generateComplaints();
        
        return summary;
    }

    /**
     * Génère des postes
     */
    @Transactional
    public int generatePostes() {
        List<Poste> postes = new ArrayList<>();
        
        String[] regions = {"Nord", "Sud", "Est", "Ouest", "Centre"};
        String[] types = {"POSTE_PRIMAIRE", "POSTE_SECONDAIRE", "POSTE_RURAL"};
        
        int count = 5;
        for (int i = 0; i < count; i++) {
            Poste poste = new Poste();
            poste.setCode("PS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            poste.setNom("Poste " + regions[i % regions.length] + " " + (i + 1));
            poste.setRegion(regions[i % regions.length]);
            poste.setAdresse("Adresse " + (i + 1));
            poste.setLatitude(33.5 + i * 0.5);
            poste.setLongitude(7.5 + i * 0.5);
            poste.setTypePoste(TypePoste.valueOf(types[i % types.length]));
            poste.setEtat(EtatPoste.ACTIF);
            poste.setCreatedAt(LocalDateTime.now());
            
            postes.add(poste);
        }
        
        posteRepository.saveAll(postes);
        log.info("Generated {} postes", count);
        return count;
    }

    /**
     * Génère des transformateurs
     */
    @Transactional
    public int generateTransformateurs() {
        List<Poste> postes = posteRepository.findAll();
        if (postes.isEmpty()) {
            generatePostes();
            postes = posteRepository.findAll();
        }
        
        List<Transformateur> transformateurs = new ArrayList<>();
        int count = 15;
        
        LocalDate today = LocalDate.now();
        
        for (int i = 0; i < count; i++) {
            Transformateur t = new Transformateur();
            t.setReference("TR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            t.setSerialNumber("SN-" + String.format("%05d", i + 1000));
            t.setManufacturer(i % 3 == 0 ? "Siemens" : i % 3 == 1 ? "ABB" : "Schneider");
            t.setModel("Model-" + (i % 5 + 1));
            t.setPower(100.0 + (i * 10)); // in MVA
            t.setInstallationDate(today.minusYears(2 + (i % 5))); // 2-7 years ago
            t.setPoste(postes.get(i % postes.size()));
            t.setStatus(StatusTransformateur.ACTIF);
            t.setRiskLevel(i % 4 == 0 ? RiskLevel.MEDIUM : RiskLevel.LOW);
            t.setHealthScore(50 + random.nextInt(50)); // 50-100
            t.setCreatedAt(LocalDateTime.now());
            
            transformateurs.add(t);
        }
        
        transformateurRepository.saveAll(transformateurs);
        log.info("Generated {} transformateurs", count);
        return count;
    }

    /**
     * Génère des mesures
     */
    @Transactional
    public int generateMeasurements() {
        List<Transformateur> transformateurs = transformateurRepository.findAll();
        if (transformateurs.isEmpty()) {
            generateTransformateurs();
            transformateurs = transformateurRepository.findAll();
        }
        
        List<Measurement> measurements = new ArrayList<>();
        int measurementsPerTransformateur = 5;
        
        LocalDateTime now = LocalDateTime.now();
        
        for (Transformateur t : transformateurs) {
            for (int j = 0; j < measurementsPerTransformateur; j++) {
                Measurement m = new Measurement();
                m.setTransformateur(t);
                
                // Température (40-90°C)
                double temp = 40 + random.nextDouble() * 50;
                m.setTemperature(temp);
                
                // Voltage (95000-110000 V)
                double voltage = 95000 + random.nextDouble() * 15000;
                m.setVoltage(voltage);
                
                // Current (100-400 A)
                double current = 100 + random.nextDouble() * 300;
                m.setCurrent(current);
                
                // Power (kW)
                double power = voltage * current / 1000;
                m.setPower(power);
                
                // Energy consumption
                m.setEnergyConsumption(random.nextDouble() * 1000);
                
                // Frequency (49.5-50.5 Hz)
                double frequency = 49.5 + random.nextDouble() * 1;
                m.setFrequency(frequency);
                
                // Humidity
                m.setHumidity(30 + random.nextDouble() * 70);
                
                // Calculate health score
                Integer healthScore = healthScoreCalculator.calculateHealthScore(
                        temp, voltage, current, frequency
                );
                m.setHealthScore(healthScore);
                
                // Determine status
                MeasurementStatus status = measurementAnalyzer.determineMeasurementStatus(
                        temp, voltage, current, frequency, healthScore
                );
                m.setStatus(status);
                
                // Measurement date (5 days ago to now)
                long daysAgo = j;
                m.setMeasurementDate(now.minusDays(daysAgo).withHour(random.nextInt(24)));
                m.setCreatedAt(m.getMeasurementDate());
                
                measurements.add(m);
            }
        }
        
        measurementRepository.saveAll(measurements);
        log.info("Generated {} measurements", measurements.size());
        return measurements.size();
    }

    /**
     * Génère des interventions
     */
    @Transactional
    public int generateInterventions() {
        List<Transformateur> transformateurs = transformateurRepository.findAll();
        List<User> users = userRepository.findAll();
        
        if (transformateurs.isEmpty() || users.isEmpty()) {
            return 0;
        }
        
        List<Intervention> interventions = new ArrayList<>();
        int count = 10;
        
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 0; i < count; i++) {
            Intervention intervention = new Intervention();
            intervention.setReference("INT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            intervention.setTypeIntervention(i % 2 == 0 ? TypeIntervention.PREVENTIVE : TypeIntervention.CORRECTIVE);
            intervention.setDescription("Intervention de maintenance " + (i + 1));
            intervention.setStatut(StatutIntervention.values()[i % StatutIntervention.values().length]);
            intervention.setPriorite(Priorite.values()[i % Priorite.values().length]);
            
            // Assign to random user (agent)
            User agent = users.stream()
                    .findAny()
                    .orElse(users.get(0));
            intervention.setAgent(agent);
            
            intervention.setTransformateur(transformateurs.get(i % transformateurs.size()));
            intervention.setDatePlanifiee(now.plusDays(random.nextInt(30)));
            intervention.setDateDebut(now.minusDays(random.nextInt(7)));
            intervention.setDateFin(now.minusDays(random.nextInt(3)));
            intervention.setCreatedAt(now);
            
            interventions.add(intervention);
        }
        
        interventionRepository.saveAll(interventions);
        log.info("Generated {} interventions", count);
        return count;
    }

    /**
     * Génère des plaintes
     */
    @Transactional
    public int generateComplaints() {
        List<Transformateur> transformateurs = transformateurRepository.findAll();
        List<User> users = userRepository.findAll();
        
        if (transformateurs.isEmpty()) {
            return 0;
        }
        
        List<Complaint> complaints = new ArrayList<>();
        int count = 8;
        
        for (int i = 0; i < count; i++) {
            Complaint complaint = new Complaint();
            complaint.setReference("CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            complaint.setNomClient("Client " + (i + 1));
            complaint.setTelephone("0600000000");
            complaint.setAdresse("Adresse " + (i + 1) + ", Algérie");
            complaint.setDescription("Plainte concernant le transformateur " + (i + 1));
            complaint.setCategorie(ComplaintCategory.values()[i % ComplaintCategory.values().length]);
            complaint.setStatut(ComplaintStatus.values()[i % ComplaintStatus.values().length]);
            complaint.setPriorite(ComplaintPriority.values()[i % ComplaintPriority.values().length]);
            
            // Assign to random supervisor
            if (!users.isEmpty()) {
                User supervisor = users.get(random.nextInt(users.size()));
                complaint.setAssignedSupervisor(supervisor);
            }
            
            complaint.setTransformateur(transformateurs.get(i % transformateurs.size()));
            complaint.setCommentaires("Notes de suivi pour la plainte " + (i + 1));
            complaint.setCreatedAt(LocalDateTime.now());
            
            complaints.add(complaint);
        }
        
        complaintRepository.saveAll(complaints);
        log.info("Generated {} complaints", count);
        return count;
    }

    /**
     * Compte les alertes présentes dans la base
     */
    private int countAlerts() {
        return (int) alertRepository.count();
    }

    /**
     * Supprime toutes les données
     */
    @Transactional
    public void clearAllData() {
        log.info("Clearing all demo data...");
        
        interventionRepository.deleteAll();
        complaintRepository.deleteAll();
        alertRepository.deleteAll();
        measurementRepository.deleteAll();
        transformateurRepository.deleteAll();
        posteRepository.deleteAll();
        
        log.info("All data cleared");
    }

    /**
     * Obtient l'état de la base de données
     */
    public Map<String, Object> getDatabaseStatus() {
        Map<String, Object> status = new HashMap<>();
        
        status.put("users", userRepository.count());
        status.put("postes", posteRepository.count());
        status.put("transformateurs", transformateurRepository.count());
        status.put("measurements", measurementRepository.count());
        status.put("alerts", alertRepository.count());
        status.put("interventions", interventionRepository.count());
        status.put("complaints", complaintRepository.count());
        status.put("timestamp", LocalDateTime.now());
        
        return status;
    }

    /**
     * DTO pour le résumé des données générées
     */
    @Data
    public static class GeneratedDataSummary {
        private int postesCount;
        private int transformateursCount;
        private int measurementsCount;
        private int alertsCount;
        private int interventionsCount;
        private int complaintsCount;
    }
}
