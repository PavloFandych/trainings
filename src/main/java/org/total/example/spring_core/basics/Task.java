package org.total.example.spring_core.basics;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Task {

    private final String name;
    private final Integer duration;

    @ToString.Exclude
    private final AnotherTask anotherTask;

    public Task(String name,
                Integer duration,
                AnotherTask anotherTask) {
        this.name = name;
        this.duration = duration;
        this.anotherTask = anotherTask;
    }
}
