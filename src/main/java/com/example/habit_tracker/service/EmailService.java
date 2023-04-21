package com.example.habit_tracker.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    Logger logger = LogManager.getLogger();

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String email, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("com.example.habit_tracker");
            message.setTo(email);
            message.setSubject(subject);
            message.setText(content);
            javaMailSender.send(message);
            logger.info("send message to + " + email);
        } catch (Exception e) {
            logger.error("Failed to send email for: " + email + " " + e);
            throw new IllegalArgumentException("Failed to send email for: " + email);
        }
    }
}

