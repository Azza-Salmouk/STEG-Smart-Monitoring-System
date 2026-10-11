package com.steg.smartmonitoring.initializer;

import com.steg.smartmonitoring.alert.entity.Alert;
import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertType;
import com.steg.smartmonitoring.alert.repository.AlertRepository;
import com.steg.smartmonitoring.complaint.entity.Complaint;
import com.steg.smartmonitoring.complaint.entity.ComplaintStatus;
import com.steg.smartmonitoring.complaint.entity.ComplaintCategory;
import com.steg.smartmonitoring.complaint.entity.ComplaintPriority;
import com.steg.smartmonitoring.complaint.repository.ComplaintRepository;
import com.steg.smartmonitoring.intervention.entity.Intervention;
import com.steg.smartmonitoring.intervention.entity.Priorite;
import com.steg.smartmonitoring.intervention.entity.StatutIntervention;
import com.steg.smartmonitoring.intervention.entity.TypeIntervention;
import com.steg.smartmonitoring.intervention.repository.InterventionRepository;
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
import com.steg.smartmonitoring.user.entity.Role;
import com.steg.smartmonitoring.user.entity.User;
import com.steg.smartmonitoring.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates comprehensive demo data for testing and demonstration
 */
@Component
@Configuration
public class DemoDataGenerator {

    private static final Logger log = LoggerFactory.getLogger(DemoDataGenerator.class);

    private final PosteRepository posteRepository;
    private final TransformateurRepository transformateurRepository;
    private final MeasurementRepository measurementRepository;
    private final AlertRepository alertRepository;
    private final InterventionRepository interventionRepository;
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random(42); // Fixed seed for reproducibility

    public DemoDataGenerator(
            PosteRepository posteRepository,
            TransformateurRepository transformateurRepository,
            MeasurementRepository measurementRepository,
            AlertRepository alertRepository,
            InterventionRepository interventionRepository,
            ComplaintRepository complaintRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.posteRepository = posteRepository;
        this.transformateurRepository = transformateurRepository;
        this.measurementRepository = measurementRepository;
        this.alertRepository = alertRepository;
        this.interventionRepository = interventionRepository;
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Generate all demo data
     */
    public void generateAllDemoData() {
        try {
            // Always create demo users (they may be needed even if data exists)
            createDemoUsers();
            log.info("Ensured demo users exist");

            // Check if data already exists
            if (posteRepository.count() > 0) {
                log.info("Demo data already exists, skipping generation");
                return;
            }

            log.info("Starting demo data generation...");

            List<Poste> postes = generatePostes();
            log.info("Created {} postes", postes.size());

            List<Transformateur> transformateurs = generateTransformateurs(postes);
            log.info("Created {} transformateurs", transformateurs.size());

            List<Measurement> measurements = generateMeasurements(transformateurs);
            log.info("Created {} measurements", measurements.size());

            List<Alert> alerts = generateAlerts(transformateurs, measurements);
            log.info("Created {} alerts", alerts.size());

            List<Intervention> interventions = generateInterventions(transformateurs);
            log.info("Created {} interventions", interventions.size());

            List<Complaint> complaints = generateComplaints();
            log.info("Created {} complaints", complaints.size());

            log.info("Demo data generation completed successfully!");

        } catch (Exception e) {
            log.error("Error during demo data generation", e);
            throw new RuntimeException("Demo data generation failed", e);
        }
    }

    /**
     * Generate 20 Postes
     */
    private List<Poste> generatePostes() {
        List<Poste> postes = new ArrayList<>();
        
        String[] regions = {"Tunis", "Ariana", "Ben Arous", "Mannouba", "Sousse", "Sfax", "Kairouan", "Gafsa"};
        TypePoste[] typePostes = {TypePoste.PRIMAIRE, TypePoste.SECONDAIRE, TypePoste.DISTRIBUTION};

        for (int i = 1; i <= 20; i++) {
            String region = regions[i % regions.length];
            TypePoste typePoste = typePostes[i % typePostes.length];

            Poste poste = Poste.builder()
                    .nom("Poste " + region + " " + i)
                    .code("P-" + String.format("%03d", i))
                    .region(region)
                    .adresse(i * 100 + " Rue de l'Électricité, " + region)
                    .latitude(36.8 + random.nextDouble())
                    .longitude(10.2 + random.nextDouble())
                    .typePoste(typePoste)
                    .etat(EtatPoste.ACTIF)
                    .description("Poste électrique type " + typePoste + " dans la région " + region)
                    .build();

            postes.add(posteRepository.save(poste));
        }

        return postes;
    }

    /**
     * Generate 150 Transformateurs (7-8 per Poste)
     */
    private List<Transformateur> generateTransformateurs(List<Poste> postes) {
        List<Transformateur> transformateurs = new ArrayList<>();
        
        String[] manufacturers = {"Siemens", "ABB", "Schneider Electric", "General Electric", "Eaton"};
        String[] models = {"TR-500", "TR-1000", "TR-2000", "TR-5000", "TR-10000"};

        int counter = 1;
        for (Poste poste : postes) {
            int countPerPoste = 7 + random.nextInt(2); // 7-8 transformateurs per poste
            for (int i = 0; i < countPerPoste; i++) {
                String manufacturer = manufacturers[random.nextInt(manufacturers.length)];
                String model = models[random.nextInt(models.length)];

                Transformateur transformateur = Transformateur.builder()
                        .reference("TF-" + String.format("%05d", counter))
                        .serialNumber("SN-" + System.nanoTime())
                        .manufacturer(manufacturer)
                        .model(model)
                        .power(100.0 + random.nextDouble() * 9900) // 100-10000 MVA
                        .installationDate(LocalDate.now().minusDays(random.nextInt(3650))) // 0-10 years old
                        .status(StatusTransformateur.ACTIF)
                        .healthScore(50 + random.nextInt(51)) // 50-100
                        .riskLevel(getRiskLevelForHealthScore(50 + random.nextInt(51)))
                        .description("Transformateur " + manufacturer + " " + model)
                        .poste(poste)
                        .build();

                transformateurs.add(transformateurRepository.save(transformateur));
                counter++;
            }
        }

        return transformateurs;
    }

    /**
     * Generate 5000 Measurements (distributed among transformateurs)
     */
    private List<Measurement> generateMeasurements(List<Transformateur> transformateurs) {
        List<Measurement> measurements = new ArrayList<>();

        int measurementsPerTransformateur = 5000 / transformateurs.size();

        for (Transformateur transformateur : transformateurs) {
            for (int i = 0; i < measurementsPerTransformateur; i++) {
                LocalDateTime measurementDate = LocalDateTime.now()
                        .minusHours(random.nextInt(24 * 30)); // Last 30 days

                Measurement measurement = Measurement.builder()
                        .transformateur(transformateur)
                        .measurementDate(measurementDate)
                        .temperature(20 + random.nextDouble() * 40) // 20-60°C
                        .voltage(380 + random.nextDouble() * 20) // 380-400V
                        .current(100 + random.nextDouble() * 400) // 100-500A
                        .power(50000 + random.nextDouble() * 450000) // 50-500 kW
                        .energyConsumption(1000 + random.nextDouble() * 9000) // 1-10 MWh
                        .frequency(50 + random.nextDouble() * 0.5) // 49.75-50.25 Hz
                        .humidity(30 + random.nextDouble() * 40) // 30-70%
                        .healthScore(50 + random.nextInt(51)) // 50-100
                        .status(MeasurementStatus.NORMAL)
                        .build();

                measurements.add(measurementRepository.save(measurement));
            }
        }

        return measurements;
    }

    /**
     * Generate 300 Alerts
     */
    private List<Alert> generateAlerts(List<Transformateur> transformateurs, List<Measurement> measurements) {
        List<Alert> alerts = new ArrayList<>();
        
        AlertType[] alertTypes = {AlertType.TEMPERATURE, AlertType.SURCHARGE, AlertType.CHUTE_TENSION, AlertType.COMMUNICATION};
        AlertLevel[] alertLevels = {AlertLevel.INFO, AlertLevel.WARNING, AlertLevel.HIGH, AlertLevel.CRITICAL};

        for (int i = 0; i < 300; i++) {
            Transformateur transformateur = transformateurs.get(random.nextInt(transformateurs.size()));
            Measurement measurement = measurements.isEmpty() ? null : 
                    measurements.get(random.nextInt(measurements.size()));

            AlertType type = alertTypes[random.nextInt(alertTypes.length)];
            AlertLevel level = alertLevels[random.nextInt(alertLevels.length)];

            Alert alert = Alert.builder()
                    .reference("ALT-" + String.format("%05d", i + 1))
                    .titre("Alerte " + type + " - Niveau " + level)
                    .description("Alert detected for " + type + " parameter")
                    .type(type)
                    .niveau(level)
                    .statut(i < 250 ? AlertStatus.ACTIVE : AlertStatus.RESOLVED) // 250 active, 50 resolved
                    .measurement(measurement)
                    .transformateur(transformateur)
                    .poste(transformateur.getPoste())
                    .isAcknowledged(random.nextBoolean())
                    .isArchived(false)
                    .build();

            alerts.add(alertRepository.save(alert));
        }

        return alerts;
    }

    /**
     * Generate 120 Interventions
     */
    private List<Intervention> generateInterventions(List<Transformateur> transformateurs) {
        List<Intervention> interventions = new ArrayList<>();
        
        TypeIntervention[] types = {
            TypeIntervention.PREVENTIVE,
            TypeIntervention.CORRECTIVE,
            TypeIntervention.URGENTE,
            TypeIntervention.INSPECTION
        };
        Priorite[] priorites = {Priorite.FAIBLE, Priorite.MOYENNE, Priorite.HAUTE, Priorite.CRITIQUE};
        StatutIntervention[] statuts = {
            StatutIntervention.EN_ATTENTE,
            StatutIntervention.PLANIFIEE,
            StatutIntervention.EN_COURS,
            StatutIntervention.TERMINEE,
            StatutIntervention.VALIDEE,
            StatutIntervention.ANNULEE
        };

        // Get main agent user (for dashboard testing)
        User mainAgent = userRepository.findByEmail("agent@steg.tn")
                .orElseThrow(() -> new RuntimeException("agent@steg.tn not found"));

        // Get or create demo agent users
        User agent1 = userRepository.findByEmail("agent1@steg.tn")
                .orElseGet(() -> {
                    User agent = User.builder()
                            .username("agent1")
                            .email("agent1@steg.tn")
                            .password(passwordEncoder.encode("password"))
                            .role(Role.AGENT)
                            .build();
                    return userRepository.save(agent);
                });

        User agent2 = userRepository.findByEmail("agent2@steg.tn")
                .orElseGet(() -> {
                    User agent = User.builder()
                            .username("agent2")
                            .email("agent2@steg.tn")
                            .password(passwordEncoder.encode("password"))
                            .role(Role.AGENT)
                            .build();
                    return userRepository.save(agent);
                });

        // Include main agent in the pool so they get some interventions
        User[] agents = {mainAgent, agent1, agent2};

        for (int i = 0; i < 120; i++) {
            Transformateur transformateur = transformateurs.get(random.nextInt(transformateurs.size()));
            User agent = agents[random.nextInt(agents.length)];
            TypeIntervention type = types[random.nextInt(types.length)];
            Priorite priorite = priorites[random.nextInt(priorites.length)];
            StatutIntervention statut = statuts[random.nextInt(statuts.length)];

            LocalDateTime dateCreation = LocalDateTime.now().minusDays(random.nextInt(30));
            LocalDateTime datePlanifiee = dateCreation.plusDays(1 + random.nextInt(7));
            LocalDateTime dateDebut = (statut == StatutIntervention.EN_ATTENTE || statut == StatutIntervention.PLANIFIEE) ? null : 
                    datePlanifiee.plusHours(random.nextInt(24));
            LocalDateTime dateFin = (statut == StatutIntervention.TERMINEE || statut == StatutIntervention.VALIDEE) ? 
                    dateDebut.plusHours(1 + random.nextInt(8)) : null;

            Intervention intervention = Intervention.builder()
                    .reference("INT-" + String.format("%05d", i + 1))
                    .transformateur(transformateur)
                    .agent(agent)
                    .dateCreation(dateCreation)
                    .datePlanifiee(datePlanifiee)
                    .dateDebut(dateDebut)
                    .dateFin(dateFin)
                    .typeIntervention(type)
                    .priorite(priorite)
                    .statut(statut)
                    .description("Intervention for " + type + " - " + statut)
                    .resultat((statut == StatutIntervention.TERMINEE || statut == StatutIntervention.VALIDEE) ? "Completed successfully" : null)
                    .observations("Observations for intervention " + (i + 1))
                    .dureeMinutes((statut == StatutIntervention.TERMINEE || statut == StatutIntervention.VALIDEE) ? 30L + random.nextInt(480) : null)
                    .cout((statut == StatutIntervention.TERMINEE || statut == StatutIntervention.VALIDEE) ? 100.0 + random.nextDouble() * 900 : null)
                    .build();

            interventions.add(interventionRepository.save(intervention));
        }

        return interventions;
    }

    /**
     * Generate 150 Complaints
     */
    private List<Complaint> generateComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        
        ComplaintCategory[] complaintCategories = {
            ComplaintCategory.COUPURE,
            ComplaintCategory.SURCHARGE,
            ComplaintCategory.PANNE,
            ComplaintCategory.FACTURATION,
            ComplaintCategory.BRUIT
        };
        ComplaintStatus[] complaintStatuses = {
            ComplaintStatus.NOUVELLE,
            ComplaintStatus.EN_ANALYSE,
            ComplaintStatus.ASSIGNEE,
            ComplaintStatus.EN_TRAITEMENT,
            ComplaintStatus.RESOLUE,
            ComplaintStatus.FERMEE
        };
        ComplaintPriority[] complaintPriorities = {
            ComplaintPriority.LOW,
            ComplaintPriority.MEDIUM,
            ComplaintPriority.HIGH,
            ComplaintPriority.CRITICAL
        };

        for (int i = 0; i < 150; i++) {
            ComplaintCategory categorie = complaintCategories[random.nextInt(complaintCategories.length)];
            ComplaintStatus statut = complaintStatuses[random.nextInt(complaintStatuses.length)];
            ComplaintPriority priorite = complaintPriorities[random.nextInt(complaintPriorities.length)];

            LocalDateTime dateCreation = LocalDateTime.now().minusDays(random.nextInt(30));
            LocalDateTime dateResolution = (statut == ComplaintStatus.RESOLUE || statut == ComplaintStatus.FERMEE) ?
                    dateCreation.plusHours(1 + random.nextInt(72)) : null;

            Complaint complaint = Complaint.builder()
                    .reference("COMP-" + String.format("%05d", i + 1))
                    .nomClient("Client " + (i + 1))
                    .telephone("+216 " + (20 + random.nextInt(80)) + " " + (100000 + random.nextInt(900000)))
                    .adresse("Adresse " + (i + 1) + ", Tunis")
                    .categorie(categorie)
                    .description("Réclamation pour " + categorie + " - Description détaillée " + (i + 1))
                    .priorite(priorite)
                    .statut(statut)
                    .createdAt(dateCreation)
                    .dateResolution(dateResolution)
                    .commentaires("Commentaires pour la réclamation " + (i + 1))
                    .build();

            complaints.add(complaintRepository.save(complaint));
        }

        return complaints;
    }

    /**
     * Create additional demo users
     */
    private void createDemoUsers() {
        // Admin user - use the standard test password
        if (!userRepository.existsByEmail("admin@steg.tn")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@steg.tn")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Created admin user: admin@steg.tn");
        }

        // Supervisor user
        if (!userRepository.existsByEmail("supervisor@steg.tn")) {
            User supervisor = User.builder()
                    .username("supervisor")
                    .email("supervisor@steg.tn")
                    .password(passwordEncoder.encode("Supervisor@123"))
                    .role(Role.SUPERVISOR)
                    .build();
            userRepository.save(supervisor);
            log.info("Created supervisor user: supervisor@steg.tn");
        }

        // Agent user
        if (!userRepository.existsByEmail("agent@steg.tn")) {
            User agent = User.builder()
                    .username("agent")
                    .email("agent@steg.tn")
                    .password(passwordEncoder.encode("Agent@123"))
                    .role(Role.AGENT)
                    .build();
            userRepository.save(agent);
            log.info("Created agent user: agent@steg.tn");
        }

        // Service Client (Customer) user
        if (!userRepository.existsByEmail("customer@steg.tn")) {
            User customer = User.builder()
                    .username("customer")
                    .email("customer@steg.tn")
                    .password(passwordEncoder.encode("Customer@123"))
                    .role(Role.SERVICE_CLIENT)
                    .build();
            userRepository.save(customer);
            log.info("Created customer user: customer@steg.tn");
        }
    }

    /**
     * Helper method to determine risk level based on health score
     */
    private RiskLevel getRiskLevelForHealthScore(int healthScore) {
        if (healthScore >= 80) {
            return RiskLevel.LOW;
        } else if (healthScore >= 60) {
            return RiskLevel.MEDIUM;
        } else if (healthScore >= 40) {
            return RiskLevel.HIGH;
        } else {
            return RiskLevel.CRITICAL;
        }
    }
}
