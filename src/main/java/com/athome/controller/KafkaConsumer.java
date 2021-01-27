package com.athome.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumer {
    private final Log log = LogFactory.getLog(KafkaConsumer.class);


    @KafkaListener(topics = {"test"}, containerFactory = "factory",
            topicPartitions = {@TopicPartition(topic = "test", partitions = {"0"})})
    public void reveive(ConsumerRecord<String, String> consumerRecords, Acknowledgment acknowledgment) {

        log.info("@@@@@@@" + consumerRecords);

        acknowledgment.acknowledge();
    }
}
