package org.total.example.spring_core.advanced.annotations;

import java.lang.annotation.Retention;

// convention: any bean annotated with @Deprecated should be replaced by its newer implementation
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface DeprecatedClass {

    Class<?> newImpl();
}
