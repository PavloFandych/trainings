package org.total.example.spring_core.beans.basics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;

@Slf4j
public class TaskManager {

    private final Task task;

    public TaskManager(Task task) {
        this.task = task;
    }

    public void printTask() {
        AnotherTask anotherTask = task.getAnotherTask();

        log.info("Before invocation:");
        log.info("Class: {}", anotherTask.getClass().getName());
        log.info("Proxy: {}", AopUtils.isAopProxy(anotherTask));

        log.info("Name: {}", anotherTask.getName());

        log.info("After invocation:");
        log.info("Class: {}", anotherTask.getClass().getName());
        log.info("Proxy: {}", AopUtils.isAopProxy(anotherTask));
    }
}
