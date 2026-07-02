package org.total.example.spring_core.kafka.producer;

import org.total.example.spring_core.kafka.model.Order;

public interface OrderService {

    void save(Order order);
}
