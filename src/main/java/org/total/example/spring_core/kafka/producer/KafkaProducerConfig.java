package org.total.example.spring_core.kafka.producer;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.total.example.spring_core.kafka.model.Order;

import java.util.HashMap;
import java.util.Map;

/*
 * The module depends only on "spring-kafka" (not "spring-boot-starter-kafka"), so there is
 * no Spring Boot autoconfiguration that reads "spring.kafka.*" and builds KafkaTemplate/
 * KafkaAdmin beans for us. Everything below is therefore wired by hand, reading only the
 * handful of properties from application-kafka.yaml that we actually need.
 */
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic.orders}")
    private String ordersTopic;

    /*
     * KafkaAdmin talks to the broker's AdminClient API at application startup and creates
     * every NewTopic bean found in the context (see ordersTopic() below). Without this bean
     * NewTopic beans are simply ignored, and the topic would only appear the moment the
     * producer or consumer first touched it via broker auto-creation - with whatever default
     * partition count the broker happens to have configured, not the one we ask for here.
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    /*
     * Declarative topic provisioning: instead of letting the broker auto-create the topic
     * with 1 partition on first use, we own its shape from the code.
     * - partitions(3): allows up to 3 consumer instances in the same group to read the topic
     *   in parallel (Kafka only ever assigns one consumer per partition within a group).
     * - replicas(1): fine for a single-broker local/dev cluster; a real multi-broker cluster
     *   should use >= 2 for fault tolerance (a broker going down would lose the partition
     *   otherwise).
     * If the topic already exists, KafkaAdmin silently leaves it as-is - this does NOT
     * repartition an existing topic, it only affects topics that don't exist yet.
     */
    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder.name(ordersTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    /*
     * Low-level factory that knows how to open a connection to the broker and produce
     * String-keyed, Order-valued records. KafkaTemplate (below) delegates to this factory
     * for every send().
     */
    @Bean
    public ProducerFactory<String, Order> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Jackson 3 based serializer (JsonSerializer is deprecated since spring-kafka 4.0)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        // durability: "all" makes the broker wait for every in-sync replica to acknowledge
        // the write before the send() future completes, so a leader crash right after ack
        // can't silently lose the record. enable.idempotence stamps each batch with a
        // producer id + sequence number so that broker-side retries (network blips, etc.)
        // can be deduplicated instead of writing the same record twice.
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        // we don't ship Jackson's "@class" type-info headers on the wire - the consumer
        // already knows the concrete type to deserialize into (see JacksonJsonDeserializer
        // config in KafkaConsumerConfig), so the headers would just be dead weight.
        props.put(JacksonJsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaProducerFactory<>(props);
    }

    /*
     * The bean application code actually injects and calls .send(...) on (see OrderProducer).
     * Thin wrapper around ProducerFactory that adds convenience methods and, in this app,
     * per-send observability via the returned CompletableFuture.
     */
    @Bean
    public KafkaTemplate<String, Order> kafkaTemplate(ProducerFactory<String, Order> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
