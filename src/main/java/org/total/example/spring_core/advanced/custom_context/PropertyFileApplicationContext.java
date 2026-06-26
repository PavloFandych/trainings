package org.total.example.spring_core.advanced.custom_context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

@Slf4j
public class PropertyFileApplicationContext extends GenericApplicationContext {

    public PropertyFileApplicationContext() {
        //noinspection deprecation
        PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(this);
        int loaded = reader.loadBeanDefinitions("classpath:context.properties");
        log.info("Loaded beans: {}", loaded);

        refresh();
    }
}
