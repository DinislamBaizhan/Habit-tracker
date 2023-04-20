package com.example.habit_tracker.demo;

import com.example.habit_tracker.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

    private final EmailService emailService;

    public DemoController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @GetMapping("/email")
    public void sentEmail() {
        emailService.sendEmail("aspanadam@mail.com", "Test", "SSSSSSSSSSSSSSSSS");
    }

}
