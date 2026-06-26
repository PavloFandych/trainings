package org.total.example.spring_core.basics;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.total.example.spring_core.basics.aop.Loggable;

@Slf4j
@Getter
@ToString
@Component
@Scope("prototype") // Each request for this bean will create a new instance
public class PrototypeBean {

    static {
        log.info("PrototypeBean static block executed");
    }

    {
        log.info("PrototypeBean dynamic block executed");
    }

    private final String name;

    public PrototypeBean() {
        this.name = "prototypeBeanName";
        log.info("PrototypeBean created");
    }

    @Loggable // does not work. AOP works via proxy only!
    @PostConstruct
    public void postConstruct() {
        log.info("PrototypeBean postConstruct");
    }

    @PreDestroy // never being call by Spring for prototype-based beans!
    // Developer should call this manually
    public void preDestroy() {
        log.info("PrototypeBean preDestroy");
    }
}
