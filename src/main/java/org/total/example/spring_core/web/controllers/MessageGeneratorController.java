package org.total.example.spring_core.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.total.example.spring_core.web.message_generator.MessageGenerator;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageGeneratorController {

    private final MessageGenerator messageGenerator;

    @GetMapping("/generate")
    public String sayQuote() {
        log.info("Is current thread virtual: {}", Thread.currentThread().isVirtual());
        return messageGenerator.generateMessage();
    }
}
