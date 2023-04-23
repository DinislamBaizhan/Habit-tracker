package com.example.habit_tracker.api;

import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.data.entity.RegisterDTO;
import com.example.habit_tracker.data.request.AuthenticationRequest;
import com.example.habit_tracker.data.response.AuthenticationResponse;
import com.example.habit_tracker.service.AuthenticationService;
import com.example.habit_tracker.service.DecodedToken;
import com.example.habit_tracker.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "authentication controller", description = "Authentication management")
public class AuthenticationController {
    private final AuthenticationService service;
    private final ProfileService profileService;

    public AuthenticationController(AuthenticationService service,
                                    ProfileService profileService) {
        this.service = service;
        this.profileService = profileService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<String> register(
            @RequestBody @Valid RegisterDTO request
    ) throws Exception {
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Email sent to verify profile");
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate a user")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws Exception {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Verify user e-mail")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) throws Exception {
        DecodedToken decodedToken = DecodedToken.getDecoded(token);
        String email = decodedToken.sub;
        Profile profile = profileService.findByEmail(email);
        profileService.updateVerify(profile);
        return ResponseEntity.ok("your profile has been successfully activated");
    }

    @PostMapping("/again")
    @Operation(summary = "Request verification e-mail one more time")
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
    @Operation(summary = "Request for password reset e-mail")
    public ResponseEntity<String> passwordReset(@RequestBody String email) throws JsonProcessingException {
        service.resetPassword(email);
        return ResponseEntity.ok("check your email to reset your password");
    }

    @PatchMapping("/reset-password")
    @Operation(summary = "Update user profile with a new password")
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