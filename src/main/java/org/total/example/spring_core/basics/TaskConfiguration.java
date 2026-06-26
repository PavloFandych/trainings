package org.total.example.spring_core.basics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Slf4j
@Configuration
public class TaskConfiguration {

    @Bean("mainTask")
    public Task mainTask(@Value("${task.name}") String name,
                         @Value("${task.duration}") Integer duration,
                         @Lazy AnotherTask anotherTask) {
        return new Task(name, duration, anotherTask);
    }

    @Bean("nonMainTask")
    public Task nonMaintask(@Value("${task.name}") String name,
                            @Value("${task.duration}") Integer duration,
                            @Lazy AnotherTask anotherTask) { // Injects a lazy proxy to break the circular dependency during context initialization
        return new Task(name, duration, anotherTask);
    }

    @Bean
    @Lazy // Defers creation of the actual bean until it is first needed
    public AnotherTask anotherTask(@Qualifier("mainTask") Task task) {
        log.info("Creating real AnotherTask bean");
        return new AnotherTask(task);
    }

    @Bean
    public TaskManager taskManager(@Qualifier("mainTask") Task task) {
        return new TaskManager(task);
    }
}
