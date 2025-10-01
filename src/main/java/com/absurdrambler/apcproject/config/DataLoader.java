package com.absurdrambler.apcproject.config;

import com.absurdrambler.apcproject.entity.User;
import com.absurdrambler.apcproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Data loader to initialize the database with default users
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if it doesn't exist
        if (!userService.existsByUsername("admin")) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), "ROLE_ADMIN");
            userService.save(admin);
            log.info("Created admin user with username: admin and password: admin123");
        }

        // Create regular user if it doesn't exist
        if (!userService.existsByUsername("user")) {
            User user = new User("user", passwordEncoder.encode("user123"), "ROLE_USER");
            userService.save(user);
            log.info("Created regular user with username: user and password: user123");
        }

        log.info("Data initialization completed");
    }
}
