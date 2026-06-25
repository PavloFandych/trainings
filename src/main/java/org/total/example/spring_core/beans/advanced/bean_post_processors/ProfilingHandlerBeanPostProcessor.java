package org.total.example.spring_core.beans.advanced.bean_post_processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.total.example.spring_core.beans.advanced.annotations.Profiling;
import org.total.example.spring_core.beans.advanced.ProfilingController;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Class<?>> map = new HashMap<>();
    private final ProfilingController controller = new ProfilingController();

    public ProfilingHandlerBeanPostProcessor() throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        if (mBeanServer != null) {
            ObjectName objectName = new ObjectName("profiling", "name", "controller");
            if (!mBeanServer.isRegistered(objectName)) {
                mBeanServer.registerMBean(controller, objectName);
            }
        }
    }

    // before init
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // remember original bean class for later use in postProcessAfterInitialization
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Profiling.class)) {
            map.put(beanName, beanClass);
        }
        return bean;
    }

    // after init
    // creates a proxy and replaces original bean from BeanDefinitions with created proxy
    // registers ProfilingController as Mbean to control profiling on/off
    //
    // 1. Open visualVm
    // 2. Run the application
    // 3. In visualVm, connect to the application process, go to MBeans tab, find "profiling" domain, and click on "controller" MBean
    // 4. In the "Attributes" section, you can toggle the "enabled" attribute to turn profiling on or off
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> beanClass = map.get(beanName);
        if (beanClass != null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(),
                    (_, method, args) -> {
                        if (controller.isEnabled()) {
                            long before = System.nanoTime();
                            Object invoked = method.invoke(bean, args);
                            log.info("Profiling method '{}' done in {} ns", method.getName(), System.nanoTime() - before);

                            return invoked;
                        } else {
                            return method.invoke(bean, args);
                        }
                    });
        }
        return bean;
    }
}
