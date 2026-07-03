package org.total.example.spring_core.kafka;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.JdbcTemplateAutoConfiguration;

@SpringBootApplication(scanBasePackages = {
        "org.total.example.spring_core.kafka.producer",
}, exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration.class
})
public class KafkaProducerStringBootApplication {

    static void main(String[] args) {
        new SpringApplicationBuilder(KafkaProducerStringBootApplication.class)
                .profiles("kafka")
                .properties("server.port=8081")
                .run(args);
    }
}
