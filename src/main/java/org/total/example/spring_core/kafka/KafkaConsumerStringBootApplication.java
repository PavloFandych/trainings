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
                // this process only polls Kafka - it never receives HTTP requests, so there's
                // no reason to pay for an embedded Tomcat. WebApplicationType.NONE skips
                // starting a web server entirely (as opposed to excluding the autoconfiguration
                // class, which would still leave web-server-flavored properties/beans in play).
                .web(WebApplicationType.NONE)
                .profiles("kafka") // activates application-kafka.yaml
                .run(args);
    }
}
