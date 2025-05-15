package com.example.service;

import com.example.dto.TaskDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Value("${spring.mail.notification-to}")
    private String emailTo;
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendStatusChangeNotification(TaskDto taskDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setSubject("Task Status Updated");
        message.setText(
                "Task ID: " + taskDto.getId() + "\n" +
                "Title: " + taskDto.getTitle() +  "\n" +
                "New Status: " + taskDto.getStatus()
        );
        mailSender.send(message);
    }
}