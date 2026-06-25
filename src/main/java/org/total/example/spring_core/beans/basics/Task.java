package org.total.example.spring_core.beans.basics;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Task {

    private final String name;
    private final String description;

    @ToString.Exclude
    private final AnotherTask anotherTask;

    public Task(AnotherTask anotherTask) {
        this.name = "task-awesome-name";
        this.description = "cool-description";
        this.anotherTask = anotherTask;
    }
}
