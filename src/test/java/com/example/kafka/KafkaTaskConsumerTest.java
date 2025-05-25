package com.example.kafka;

import com.example.dto.TaskDto;
import com.example.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaTaskConsumerTest {

    @Mock
    private NotificationService notificationService;

    @Test
    void listen() {
        KafkaTaskConsumer consumer = new KafkaTaskConsumer(notificationService);
        Acknowledgment ack = mock(Acknowledgment.class);
        TaskDto taskDto = new TaskDto();
        consumer.listen(taskDto, ack);
        verify(notificationService).sendStatusChangeNotification(taskDto);
    }
}