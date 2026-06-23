package com.capcom.aspiro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestSecurityConfig {
    // Intentionally empty: test profile should not configure a security filter chain here
}
