package org.total.example.spring_core.kafka;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.JdbcTemplateAutoConfiguration;

@SpringBootApplication(scanBasePackages = {
        "org.total.example.spring_core.kafka.consumer",
}, exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration.class
})
public class KafkaConsumerStringBootApplication {

    static void main(String[] args) {
        new SpringApplicationBuilder(KafkaConsumerStringBootApplication.class)
                .web(WebApplicationType.NONE) // skips starting a web server entirely
                .profiles("kafka")
                .run(args);
    }
}
