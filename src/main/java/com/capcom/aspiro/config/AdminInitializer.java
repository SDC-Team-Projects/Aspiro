package com.capcom.aspiro.config;

import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.model.enums.UserRole;
import com.capcom.aspiro.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@aspiro.com";

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = User.builder()
                .name("Admin")
                .email(adminEmail)
                .password(passwordEncoder.encode("admin12345"))
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(admin);
    }
}