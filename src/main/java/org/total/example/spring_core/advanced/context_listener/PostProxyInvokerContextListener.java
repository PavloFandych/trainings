package org.total.example.spring_core.advanced.context_listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.total.example.spring_core.advanced.annotations.PostProxy;

import java.lang.reflect.Method;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ConfigurableListableBeanFactory beanFactory;

    // Runs after the context is refreshed and all bean proxies are ready.
    // Useful for tasks such as cache warm-up.
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        // context returns the final bean instance, which may be a proxy.

        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String originalBeanClassName = beanDefinition.getBeanClassName();

            if (originalBeanClassName == null) {
                log.debug("Skipping bean '{}' because originalBeanClassName is null", beanName);
                continue;
            }
            try {
                Class<?> originalBeanClass = Class.forName(originalBeanClassName);
                for (Method originalMethod : originalBeanClass.getMethods()) {
                    if (originalMethod.isAnnotationPresent(PostProxy.class)) {
                        Object proxiedBean = applicationContext.getBean(beanName);
                        Method proxiedMethod = proxiedBean.getClass().getMethod(originalMethod.getName(), originalMethod.getParameterTypes());
                        //noinspection JavaReflectionInvocation
                        proxiedMethod.invoke(proxiedBean);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to invoke @PostProxy method for bean: " + beanName, e);
            }
        }
    }
}
