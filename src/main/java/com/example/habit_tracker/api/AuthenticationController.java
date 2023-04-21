package com.example.habit_tracker.api;

import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.data.entity.RegisterDTO;
import com.example.habit_tracker.data.request.AuthenticationRequest;
import com.example.habit_tracker.data.response.AuthenticationResponse;
import com.example.habit_tracker.service.AuthenticationService;
import com.example.habit_tracker.service.DecodedToken;
import com.example.habit_tracker.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService service;
    private final ProfileService profileService;

    public AuthenticationController(AuthenticationService service,
                                    ProfileService profileService) {
        this.service = service;
        this.profileService = profileService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid RegisterDTO request
    ) throws Exception {
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Email sent to verify profile");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws Exception {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) throws Exception {
        DecodedToken decodedToken = DecodedToken.getDecoded(token);
        String email = decodedToken.sub;
        Profile profile = profileService.findByEmail(email);
        profileService.updateVerify(profile);
        return ResponseEntity.ok("your profile has been successfully activated");
    }

    @PostMapping("/again")
    public ResponseEntity<String> sendAgain(@RequestBody RegisterDTO registerDTO) throws Exception {


        Profile profile = profileService.findByEmail(registerDTO.getEmail());

        RegisterDTO register = new RegisterDTO(
                profile.getFirstname(),
                profile.getLastname(),
                profile.getEmail(),
                profile.getPassword()
        );

        service.register(register);
        return ResponseEntity.ok("Email sent to confirm profile again");
    }

    @PostMapping("/reset_password")
    public ResponseEntity<String> passwordReset(@RequestBody String email) throws JsonProcessingException {
        service.resetPassword(email);
        return ResponseEntity.ok("check your email to reset your password");
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> passwordReset(
            @RequestParam("token") String token,
            @RequestBody String password
    ) throws Exception {

        DecodedToken decodedToken = DecodedToken.getDecoded(token);
        String email = decodedToken.sub;
        Profile profile = profileService.findByEmail(email);

        service.updatePassword(profile, password);

        return ResponseEntity.ok("your password has been successfully updated");
    }
}