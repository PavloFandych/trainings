package org.total.example.spring_core.advanced;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.total.example.spring_core.advanced.annotations.InjectRandomInt;
import org.total.example.spring_core.advanced.annotations.PostProxy;
import org.total.example.spring_core.advanced.annotations.Profiling;

@Profiling
@Slf4j
@Setter
@Component
public class TerminatorQuoter implements Quoter {

    static {
        log.info("[static block] in action");
    }

    {
        log.info("[dynamic block] in action");
    }

    @InjectRandomInt(min = 2, max = 7)
    private int repeat;
    private String message;

    public TerminatorQuoter() {
        log.info("[constructor PHASE 1] repeat: {}", repeat);
        this.message = "I'll be back";
    }

    // no proxies at all
    @PostConstruct // acts on original bean before it is being "proxied"
    public void init() {
        log.info("[init PHASE 2] repeat: {}", repeat);
    }

    @PreDestroy
    public void shutdown() {
        log.info("[shutdown] in action");
    }

    @Override
    public void sayQuote() {
        for (int i = 0; i < repeat; i++) {
            log.info("[sayQuote] in action; index: {}, message: {}", i, message);
        }
    }

    // acts on proxied bean after it is being "proxied"
    // it means that profiling happens
    @PostProxy
    @Override
    public void postProxy() {
        log.info("[postProxy] PHASE 3");
    }
}
