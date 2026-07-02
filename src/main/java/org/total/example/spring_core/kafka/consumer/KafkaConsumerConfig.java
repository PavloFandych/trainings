package org.total.example.spring_core.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import org.total.example.spring_core.kafka.model.Order;

import java.util.HashMap;
import java.util.Map;

/*
 * Same story as KafkaProducerConfig: only "spring-kafka" is on the classpath, not
 * "spring-boot-starter-kafka", so nothing here is autoconfigured from
 * "spring.kafka.consumer.*" properties. All beans required to make @KafkaListener work are
 * declared explicitly below.
 */
@EnableKafka // activates processing of @KafkaListener-annotated methods (e.g. OrderConsumer);
             // without it the annotation is inert because there's no autoconfiguration to
             // register the listener bean post-processor for us
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /*
     * Builds the actual Kafka Consumer used under the hood by every @KafkaListener in this
     * app. groupId controls partition assignment: all instances sharing the same group-id
     * split the topic's partitions between them (see NewTopic in KafkaProducerConfig for the
     * partition count), so this is also the knob that controls horizontal scale-out.
     */
    @Bean
    public ConsumerFactory<String, Order> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // if this consumer group has no committed offset yet (first run, or the topic was
        // recreated), start from the oldest available record instead of only the newest -
        // otherwise records produced before the consumer's first poll would be skipped
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // ErrorHandlingDeserializer wraps the real key/value deserializers below. If the
        // raw bytes on the topic can't be deserialized (corrupt payload, incompatible
        // schema change, etc.), it turns that failure into a DeserializationException that
        // DefaultErrorHandler can catch and route to the DLT - instead of the exception
        // escaping the poll loop and killing the whole listener container.
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);

        // the producer sends without Jackson type-info headers (see JacksonJsonSerializer.
        // ADD_TYPE_INFO_HEADERS=false in KafkaProducerConfig), so this side must be told
        // explicitly what Java type to deserialize the JSON payload into, and which
        // package(s) it's allowed to instantiate (a safety limit against deserializing
        // arbitrary classes from untrusted payloads).
        props.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, Order.class.getName());
        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, Order.class.getPackageName());

        return new DefaultKafkaConsumerFactory<>(props);
    }

    /*
     * A small, self-contained producer used exclusively by DeadLetterPublishingRecoverer
     * below to republish records that a listener couldn't process. Kept separate from the
     * "real" application producer (which lives in the producer module and isn't on this
     * app's classpath - see scanBasePackages in KafkaConsumerStringBootApplication) so the
     * consumer side stays self-sufficient.
     */
    @Bean
    public KafkaTemplate<Object, Object> dltKafkaTemplate() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    /*
     * Central resilience policy for every listener wired through
     * kafkaListenerContainerFactory below:
     * - DeadLetterPublishingRecoverer: once retries are exhausted, republishes the failing
     *   record (with the original headers plus exception info) to "<topic>.DLT" - by
     *   default "orders-topic.DLT" - instead of dropping it on the floor or blocking the
     *   partition forever.
     * - FixedBackOff(1000L, 3): retry the failing record 3 times, waiting 1 second between
     *   attempts, before giving up and handing it to the recoverer. Covers transient failures
     *   (e.g. a downstream call timing out) without paying the cost of an unbounded retry loop.
     */
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> dltKafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(dltKafkaTemplate);
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3));
    }

    /*
     * The factory @KafkaListener methods bind to by default (matched by this exact bean
     * name). Combines the consumer configuration and the error-handling policy above into
     * the container that actually polls the broker and invokes listener methods.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Order> kafkaListenerContainerFactory(
            ConsumerFactory<String, Order> consumerFactory, DefaultErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}
