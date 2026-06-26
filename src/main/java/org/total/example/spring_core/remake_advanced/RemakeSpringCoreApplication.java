package org.total.example.spring_core.remake_advanced;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication(scanBasePackages = "org.total.example.spring_core.remake_advanced.puzzler_1")
public class RemakeSpringCoreApplication {

    static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(RemakeSpringCoreApplication.class)
                .run("--terminator.main.mission=save and release all hostages");
        context.close();
    }
}
