package com.athome.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;

@Configuration
public class KafkaConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;
    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;
    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    @Bean
    public KafkaTemplate kafkaTemplate() {
        HashMap<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        DefaultKafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory(configs);
        return new KafkaTemplate<String, String>(producerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory factory(ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory();
        concurrentKafkaListenerContainerFactory.setConcurrency(10);
        //concurrentKafkaListenerContainerFactory.setBatchListener(true);
        concurrentKafkaListenerContainerFactory.getContainerProperties().setPollTimeout(1000);
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        concurrentKafkaListenerContainerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return concurrentKafkaListenerContainerFactory;
    }
}