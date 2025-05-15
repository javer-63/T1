package com.example.kafka;

import com.example.dto.TaskDto;
import com.example.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaTaskConsumer {
    private final NotificationService notificationService;

    public KafkaTaskConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(TaskDto taskDto, Acknowledgment ack) {
        notificationService.sendStatusChangeNotification(taskDto);
        ack.acknowledge();
    }
}