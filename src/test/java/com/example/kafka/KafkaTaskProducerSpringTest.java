package com.example.kafka;

import com.example.AbstractContainerTest;
import com.example.dto.TaskDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class KafkaTaskProducerSpringTest extends AbstractContainerTest {

    @MockBean
    private KafkaTemplate<String, TaskDto> kafkaTemplate;

    @Autowired
    private KafkaTaskProducer kafkaTaskProducer;

    @Test
    @DisplayName("Отправка сообщения в kafka")
    void sendMessageToKafka() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        kafkaTaskProducer.sendTaskUpdate(taskDto);

        verify(kafkaTemplate).send(eq("test-tasks"), any());
    }
}