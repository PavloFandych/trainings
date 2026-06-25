package org.total.example.spring_core.beans.advanced;

import lombok.Getter;

@Getter
// name needs to be ended with "MBean"
public class ProfilingController implements ProfilingControllerMBean {

    private boolean enabled = true;

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
