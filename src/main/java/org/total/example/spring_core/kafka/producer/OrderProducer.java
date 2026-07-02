package org.total.example.spring_core.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.total.example.spring_core.kafka.model.Order;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Value("${kafka.topic.orders}")
    private String ordersTopic;

    public void send(Order order) {
        // orderId is used as the record key, not just an id: Kafka routes records with the
        // same key to the same partition, so all events for one order stay in order relative
        // to each other even though the topic has multiple partitions (see NewTopic in
        // KafkaProducerConfig).
        //
        // send() is fire-and-forget by default - it returns a CompletableFuture instead of
        // blocking, so without the callback below the app would have no signal at all about
        // whether the broker actually accepted the record.
        kafkaTemplate.send(ordersTopic, order.orderId(), order)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("failed to send order to kafka: {}", order, ex);
                    } else {
                        log.info("order sent to kafka: topic={}, partition={}, offset={}, order={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset(),
                                order);
                    }
                });
    }
}