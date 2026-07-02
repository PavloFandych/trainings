package org.total.example.spring_core.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
        "org.total.example.spring_core.advanced.bean_post_processors", // for profiling bpp
        "org.total.example.spring_core.advanced.profiling", // for profiling
        "org.total.example.spring_core.web" // for controllers
})
public class WebSpringBootApplication {

    static void main(String[] args) {
        new SpringApplicationBuilder(WebSpringBootApplication.class)
                .profiles("prod")
                .properties("server.port=8080")
                .run(args);
    }
}
