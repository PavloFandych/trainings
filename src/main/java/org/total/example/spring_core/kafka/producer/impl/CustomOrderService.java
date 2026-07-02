package org.total.example.spring_core.kafka.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.total.example.spring_core.kafka.model.Order;
import org.total.example.spring_core.kafka.producer.OrderProducer;
import org.total.example.spring_core.kafka.producer.OrderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOrderService implements OrderService {

    private final OrderProducer orderProducer;

    @Override
    public void save(Order order) {
        orderProducer.send(order);
    }
}
