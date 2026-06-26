package org.total.example.spring_core.advanced.bean_factory_post_processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;
import org.total.example.spring_core.advanced.annotations.DeprecatedClass;

// Works with BeanDefinitions. This is a very early stage of the application lifecycle, so no beans have been instantiated yet.
@Slf4j
@Component
public class DeprecationHandlerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName == null) {
                continue;
            }
            try {
                Class<?> beanClass = Class.forName(beanClassName);
                DeprecatedClass annotation = beanClass.getAnnotation(DeprecatedClass.class);
                if (annotation != null) {
                    log.info("{} is deprecated, replacing with {}", beanClassName, annotation.newImpl().getName());
                    beanDefinition.setBeanClassName(annotation.newImpl().getName());
                    ((BeanDefinitionRegistry) beanFactory).removeBeanDefinition(name);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
