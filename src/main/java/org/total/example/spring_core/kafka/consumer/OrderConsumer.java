package org.total.example.spring_core.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.total.example.spring_core.kafka.model.Order;

@Slf4j
@Component
public class OrderConsumer {

    // container/deserialization/consumer settings all come from KafkaConsumerConfig -
    // "kafkaListenerContainerFactory" is picked up automatically because it matches the
    // default factory bean name. If this method throws, DefaultErrorHandler (also configured
    // there) retries it 3 times before publishing the record to the dead-letter topic.
    @KafkaListener(topics = "${kafka.topic.orders}")
    public void consume(Order order) {
        log.info("received order from kafka: {}", order);
    }
}
