package com.example.gradledemo.util.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KafkaConsumer {

    private final Queue<String> messagesEvent = new LinkedList<>();


    @KafkaListener(topics = "${kafka.consumer.topic}", groupId = "ip-calculator-consumer")
    public void listenIpCalculatorEvent(String message) {
        messagesEvent.offer(message);
    }

    public Queue<String> getIpCalculatedMessages() {
        return messagesEvent;
    }
}
