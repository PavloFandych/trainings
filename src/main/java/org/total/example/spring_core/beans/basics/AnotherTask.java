package org.total.example.spring_core.beans.basics;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AnotherTask {

    private final String name;

    @ToString.Exclude
    private final Task task;

    public AnotherTask(Task task) {
        this.name = "another-task-awesome-name";
        this.task = task;
    }
}
