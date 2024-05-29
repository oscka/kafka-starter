package com.example.demo;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

// ConsumerApplication01 의 KafkaConsumerService01
@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test-topic", groupId = "consumer_group01")
    public void consume(String message) throws IOException {
        log.info("Consumed Message : {}", message);
    }
}