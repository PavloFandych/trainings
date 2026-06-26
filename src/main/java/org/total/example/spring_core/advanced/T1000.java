package org.total.example.spring_core.advanced;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class T1000 extends T800 implements Robot {

    @Override
    public void doSomething() {
        log.info("I am a liquid metal terminator!");
    }
}
