package org.total.example.spring_core.remake_advanced.puzzler_1.env;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

public class TerminatorMissionEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        environment.getPropertySources().addFirst(
                new MapPropertySource("terminatorMission", Map.of("terminator.main.mission", "save Sarah Connor"))
        );
    }
}
