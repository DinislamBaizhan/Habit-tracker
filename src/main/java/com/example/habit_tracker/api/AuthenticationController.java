package com.example.habit_tracker.api;

import com.example.habit_tracker.data.entity.RegisterDTO;
import com.example.habit_tracker.data.request.AuthenticationRequest;
import com.example.habit_tracker.data.response.AuthenticationResponse;
import com.example.habit_tracker.repository.ProfileRepository;
import com.example.habit_tracker.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService service;
    private final ProfileRepository profileRepository;

    public AuthenticationController(
            AuthenticationService service,
            ProfileRepository profileRepository) {
        this.service = service;
        this.profileRepository = profileRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterDTO request
    ) {

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}