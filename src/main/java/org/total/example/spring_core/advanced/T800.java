package org.total.example.spring_core.advanced;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.total.example.spring_core.advanced.annotations.DeprecatedClass;

@DeprecatedClass(newImpl = T1000.class)
@Slf4j
@Component
public class T800 implements Robot {

    @Override
    public void doSomething() {
        log.info("I am a T800 terminator!");
    }
}
