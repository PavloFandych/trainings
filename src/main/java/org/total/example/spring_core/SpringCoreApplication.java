package org.total.example.spring_core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.total.example.spring_core.beans.advanced.Quoter;
import org.total.example.spring_core.beans.basics.PrototypeBean;
import org.total.example.spring_core.beans.basics.SimpleLifecycleBean;
import org.total.example.spring_core.beans.basics.TaskManager;

@Slf4j
@SpringBootApplication(scanBasePackages = {
        "org.total.example.spring_core.beans.advanced"
})
public class SpringCoreApplication {

    static void main(String[] args) {
        log.info("-----------BASICS-----------");
        AnnotationConfigApplicationContext basicsContext = new AnnotationConfigApplicationContext(
                "org.total.example.spring_core.beans.basics");

        PrototypeBean prototypeBean1 = basicsContext.getBean(PrototypeBean.class);
        log.info("PrototypeBean is being created once needed");
        PrototypeBean prototypeBean2 = basicsContext.getBean(PrototypeBean.class);

        log.info("prototypeBean1 == prototypeBean2: {}", prototypeBean1 == prototypeBean2);

        basicsContext.getBean(TaskManager.class).printTask();
        basicsContext.getBean(SimpleLifecycleBean.class).doSomething();

        basicsContext.close();

        System.out.println(StringUtils.EMPTY);
        log.info("-----------ADVANCED-----------");
        AnnotationConfigApplicationContext advancedContext = new AnnotationConfigApplicationContext(
                "org.total.example.spring_core.beans.advanced");
        advancedContext.getBean(Quoter.class).sayQuote(); // find interface
        advancedContext.close();

        System.out.println(StringUtils.EMPTY);
        log.info("-----------WEB-----------");
        ConfigurableApplicationContext webContext = SpringApplication.run(SpringCoreApplication.class, args);
        webContext.getBean(Quoter.class); // find interface
    }
}
