package com.example.service;

import com.example.dto.TaskDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendStatusChangeNotification(TaskDto taskDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("user@example.com");
        message.setSubject("Task Status Updated");
        message.setText(
                "Task ID: " + taskDto.getId() + "\n" +
                "Title: " + taskDto.getTitle() +  "\n" +
                "New Status: " + taskDto.getStatus()
        );
        mailSender.send(message);
    }
}