package com.example.gradledemo.steps;


import com.appgate.dtp.util.enums.Context;
import com.appgate.dtp.util.kafka.KafkaProducer;
import com.example.gradledemo.util.kafka.KafkaConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.appgate.dtp.util.json.JsonSupport.OBJECT_MAPPER;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

@Component
public class AbstractStep {

    private final KafkaProducer producer;
    private final KafkaConsumer consumer;
    private final Map<String, Object> context;
    private final Map<String, Object> values;

    @Autowired
    public AbstractStep(KafkaProducer producer, KafkaConsumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
        this.context = new HashMap<>();
        this.values = new HashMap<>();
    }

    public void setContext(Context key, Object value) {
        context.put(key.toString(), value);
    }

    public <T> T getContext(@NotNull Context key, @NotNull Class<T> clazz) {
        return clazz.cast(context.get(key.toString()));
    }

    public ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public void sendMessage(String nameTopic, String message) {
        producer.sendMessage(nameTopic, message);
    }

    public void sendMessage(String message) {
        producer.sendMessage(message);
    }

    public String getMessage(Integer count) {
        await().atMost(Duration.ofSeconds(10)).with().pollInterval(Duration.ofMillis(100)).until(() -> consumer.getIpCalculatedMessages().size(), equalTo(count));
        final String messageDtpV1 = consumer.getIpCalculatedMessages().poll();
        return messageDtpV1 == null ? "" : messageDtpV1.trim();
    }

    public void cleanMessagesBroker() {
        consumer.getIpCalculatedMessages().clear();
    }

}
