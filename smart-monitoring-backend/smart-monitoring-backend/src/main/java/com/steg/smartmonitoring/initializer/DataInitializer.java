package com.steg.smartmonitoring.initializer;

import com.steg.smartmonitoring.user.entity.Role;
import com.steg.smartmonitoring.user.entity.User;
import com.steg.smartmonitoring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DemoDataGenerator demoDataGenerator;

    @Override
    public void run(String... args) {

        // Create default admin if not exists
        if (userRepository.count() == 0) {

            User admin = User.builder()

                    .username("Administrator")

                    .email("admin@steg.tn")

                    .password(passwordEncoder.encode("Admin@123"))

                    .role(Role.ADMIN)

                    .build();

            userRepository.save(admin);

            log.info("====================================");
            log.info("DEFAULT ADMIN CREATED");
            log.info("Email : admin@steg.tn");
            log.info("Password : Admin@123");
            log.info("====================================");

        }

        // Generate demo data
        try {
            demoDataGenerator.generateAllDemoData();
        } catch (Exception e) {
            log.error("Error generating demo data", e);
        }

    }

}