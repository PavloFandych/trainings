package org.total.example.spring_core.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = {
        "org.total.example.spring_core.advanced.bean_post_processors", // for profiling bpp
        "org.total.example.spring_core.advanced.profiling", // for profiling
        "org.total.example.spring_core.web" // for controllers
})
public class WebSpringBootApplication {

    static void main(String[] args) {
        SpringApplication.run(WebSpringBootApplication.class, args);
    }
}
