package org.total.example.spring_core.advanced;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.total.example.spring_core.advanced.custom_context.CustomContextDemoBean;
import org.total.example.spring_core.advanced.custom_context.PropertyFileApplicationContext;
import org.total.example.spring_core.advanced.screen_saver.ColorFrame;

@Slf4j
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration.class
})
public class AdvancedSpringCoreApplication {

    static void main(String[] args) {
        AnnotationConfigApplicationContext advancedContext = new AnnotationConfigApplicationContext(
                "org.total.example.spring_core.advanced");
        advancedContext.getBean(Quoter.class).sayQuote(); // find interface
        advancedContext.getBean(Robot.class).doSomething();

        int counter = 1;
        ColorFrame colorFrame = advancedContext.getBean(ColorFrame.class);
        for (int i = 0; i < counter; i++) {
            try {
                colorFrame.showOnRandomPlace();
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        advancedContext.close();

        PropertyFileApplicationContext propertyFileApplicationContext = new PropertyFileApplicationContext();
        propertyFileApplicationContext.getBean(CustomContextDemoBean.class).doSomething();
        propertyFileApplicationContext.close();
    }
}
