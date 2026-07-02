package org.total.example.spring_core.remake_advanced;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.JdbcTemplateAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication(scanBasePackages = "org.total.example.spring_core.remake_advanced.puzzler_1",
        exclude = {
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                JdbcTemplateAutoConfiguration.class,
                org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration.class
        })
public class RemakeSpringCoreApplication {

    static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(RemakeSpringCoreApplication.class)
                .web(WebApplicationType.NONE)
                .run("--terminator.main.mission=save and release all hostages");
        context.close();
    }
}
