package com.example.kafka;

import com.example.dto.TaskDto;
import com.example.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaTaskConsumer {
    private final NotificationService notificationService;

    public KafkaTaskConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @KafkaListener(topics = "task-updates", groupId = "task-group")
    public void listen(TaskDto taskDto) {
        notificationService.sendStatusChangeNotification(taskDto);
    }
}