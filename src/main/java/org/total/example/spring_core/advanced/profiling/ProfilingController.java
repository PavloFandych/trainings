package org.total.example.spring_core.advanced.profiling;

import lombok.extern.slf4j.Slf4j;

// name needs to be ended with "MBean"
@Slf4j
public class ProfilingController implements ProfilingControllerMBean {

    private volatile boolean enabled = true;

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        log.debug("Setting enabled flag: enabled={}", enabled);
        this.enabled = enabled;
    }
}
