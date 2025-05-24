package com.example.kafka;

import com.example.dto.TaskDto;
import com.example.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.support.Acknowledgment;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class KafkaTaskConsumerSpringTest {

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private Acknowledgment ack;

    @Autowired
    private KafkaTaskConsumer kafkaTaskConsumer;

    @Test
    void listenTest() {
        TaskDto taskDto = new TaskDto();
        kafkaTaskConsumer.listen(taskDto, ack);
        verify(notificationService).sendStatusChangeNotification(taskDto);
        verify(ack).acknowledge();
    }
}