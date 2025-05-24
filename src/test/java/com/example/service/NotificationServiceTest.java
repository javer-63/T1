package com.example.service;

import com.example.dto.TaskDto;
import com.example.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Test
    void sendNotificationTest() {
        NotificationService notificationService = new NotificationService(mailSender);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("Test Task");
        taskDto.setDescription("Test Description");
        taskDto.setUserId(1L);
        taskDto.setStatus(TaskStatus.COMPLETED);
        notificationService.sendStatusChangeNotification(taskDto);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}