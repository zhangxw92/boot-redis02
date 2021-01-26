package com.athome.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private final Log log = LogFactory.getLog(KafkaConsumer.class);


    @KafkaListener(topics = "test")
    public void reveive(ConsumerRecord consumerRecord) {

        Object value = consumerRecord.value();
        log.info(value);
    }
}
