package org.total.example.spring_core.advanced.annotations;

import java.lang.annotation.Retention;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface InjectRandomInt {
    int min();

    int max();
}
