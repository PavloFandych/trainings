package org.total.example.spring_core.basics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.JdbcTemplateAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@SpringBootApplication(scanBasePackages = {
        "org.total.example.spring_core.basics"
}, exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration.class
})
@EnableAspectJAutoProxy
public class BasicsSpringCoreApplication {

    static void main(String[] args) {
        ConfigurableApplicationContext basicsContext = new SpringApplicationBuilder(BasicsSpringCoreApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        PrototypeBean prototypeBean1 = basicsContext.getBean(PrototypeBean.class);
        log.info("PrototypeBean is being created once needed");
        PrototypeBean prototypeBean2 = basicsContext.getBean(PrototypeBean.class);

        log.info("prototypeBean1 == prototypeBean2: {}", prototypeBean1 == prototypeBean2);

        TaskManager taskManager = basicsContext.getBean(TaskManager.class);
        taskManager.printTask();
        taskManager.getSomeInteger();
        try {
            taskManager.throwException();
        } catch (Exception e) {
            log.error("Exception occurred", e);
        }
        basicsContext.getBean(SimpleLifecycleBean.class).doSomething();
        log.info("TaskProperties: {}", basicsContext.getBean(TaskProperties.class));
        basicsContext.close();
    }
}
