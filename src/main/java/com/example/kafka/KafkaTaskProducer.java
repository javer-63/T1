package com.example.kafka;

import com.example.dto.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaTaskProducer {
    @Value("${spring.kafka.topic}")
    private String topic;
    private final KafkaTemplate<String, TaskDto> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaTaskProducer.class);

    public KafkaTaskProducer(KafkaTemplate<String, TaskDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskUpdate(TaskDto taskDto) {
        try {
            log.info("Sending task update with id {}", taskDto.getId());
            kafkaTemplate.send(topic, taskDto);
        } catch (Exception e) {
            log.error("Error while sending task update with id {}: ", taskDto.getId(), e);
        }

    }
}