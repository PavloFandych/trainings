package org.total.example.spring_core.kafka.model;

public record Order(
        String orderId,
        String productName,
        Integer quantity
) {
}
