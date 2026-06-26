package org.total.example.spring_core.basics;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@ToString
public class TaskProperties {

    @Value("${task.name}")
    private String name;

    @Value("${task.duration}")
    private Integer duration;

    @Value("#{'${task.valueList}'.split('\\s*,\\s')}") //SPEL in action. regexp to .trim()
    private List<String> valueList;

    @Value("#{'${task.valueSet}'.split('\\s*,\\s')}") //SPEL in action. regexp to .trim()
    private Set<String> valueSet;

    @Value("#{${task.valueMap}}") //SPEL in action
    private Map<String, Integer> valueMap;
}
