package org.total.example.spring_core.web.message_generator;

import org.springframework.stereotype.Component;
import org.total.example.spring_core.advanced.annotations.Profiling;

import java.time.LocalDateTime;

@Profiling
@Component
public class CustomMessageGenerator implements MessageGenerator {

    @Override
    public String generateMessage() {
        return "Generated Message: local date time is " + LocalDateTime.now();
    }
}
