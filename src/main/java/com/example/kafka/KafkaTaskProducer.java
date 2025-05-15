package com.example.kafka;

import com.example.dto.TaskDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaTaskProducer {
    private static final String TOPIC = "task-updates";

    private final KafkaTemplate<String, TaskDto> kafkaTemplate;

    public KafkaTaskProducer(KafkaTemplate<String, TaskDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskUpdate(TaskDto taskDto) {
        kafkaTemplate.send(TOPIC, taskDto);
    }
}