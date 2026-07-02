package org.total.example.spring_core.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.total.example.spring_core.kafka.model.Order;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final AtomicInteger orderIdCounter = new AtomicInteger(0);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Order order) {
        int orderId = orderIdCounter.incrementAndGet();
        String productName = order.productName()
                + ThreadLocalRandom.current().nextInt(100);

        orderService.save(
                new Order(Integer.toString(orderId), productName, order.quantity())
        );
    }
}
