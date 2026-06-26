package org.total.example.spring_core.advanced.screen_saver;

import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class PeriodicalScopeConfigurer implements Scope {

    Map<String, Pair<LocalTime, Object>> map = new HashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (map.containsKey(name)) {
            Pair<LocalTime, Object> pair = map.get(name);
            int secondsSinceLastRequest = LocalTime.now().getSecond() - pair.getLeft().getSecond();
            if (secondsSinceLastRequest > 3) {
                map.put(name, Pair.of(LocalTime.now(), objectFactory.getObject()));
            }
        } else {
            map.put(name, Pair.of(LocalTime.now(), objectFactory.getObject()));
        }
        return map.get(name).getRight();
    }

    @Override
    public @Nullable Object remove(String name) {
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // impl
    }
}
