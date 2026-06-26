package org.total.example.spring_core.advanced.custom_context;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CustomContextDemoBean {

    private String message;

    public void doSomething() {
        log.info("[MESSAGE]: {}", message);
    }
}
