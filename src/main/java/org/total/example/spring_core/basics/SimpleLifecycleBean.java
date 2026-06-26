package org.total.example.spring_core.basics;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@ToString
@Component
public class SimpleLifecycleBean {

    private final String name;

    public SimpleLifecycleBean() {
        this.name = "some-name";
        log.info("[STEP 1]: Singleton SimpleLifecycleBean created");
    }

    @PostConstruct
    public void postConstruct() { // empty args!
        log.info("[STEP 2]: Singleton SimpleLifecycleBean postConstruct");
    }

    @PreDestroy
    public void preDestroy() { // empty args!
        log.info("[STEP 4]: Singleton SimpleLifecycleBean preDestroy");
    }

    public void doSomething() {
        log.info("[STEP 3]: Singleton SimpleLifecycleBean doSomething");
    }
}
