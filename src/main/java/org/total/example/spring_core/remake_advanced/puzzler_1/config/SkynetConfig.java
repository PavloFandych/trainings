package org.total.example.spring_core.remake_advanced.puzzler_1.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SkynetConfig {

    @Value("${terminator.main.mission}")
    private String mission;

    @PostConstruct
    public void init() {
        log.info("Terminator main mission: {}", mission);
    }
}
