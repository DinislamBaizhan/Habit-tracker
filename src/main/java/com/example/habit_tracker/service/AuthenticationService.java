package com.example.habit_tracker.service;

import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.data.entity.RegisterDTO;
import com.example.habit_tracker.data.entity.Token;
import com.example.habit_tracker.data.enums.TokenType;
import com.example.habit_tracker.data.request.AuthenticationRequest;
import com.example.habit_tracker.data.response.AuthenticationResponse;
import com.example.habit_tracker.repository.TokenRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final ProfileService profileService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    Logger logger = LogManager.getLogger();

    public AuthenticationService(ProfileService profileService, TokenRepository tokenRepository,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager, EmailService emailService) {
        this.profileService = profileService;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public void register(@Valid RegisterDTO registerDTO) throws Exception {

        logger.info("register profile" + registerDTO.getEmail());
        Profile profile = profileService.createUser(registerDTO);
        String jwtToken = jwtService.generateToken(profile);

        String link = "http://localhost:8080/api/v1/auth/verify-email?token=" + jwtToken;
        emailService.sendEmail(profile.getEmail(), "Email Verification", "Click on this link to verify your email: " + link);

        saveUserToken(profile, jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {

        Profile profile = profileService.findByEmail(request.getEmail());
        if (profile.isEnabled()) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var jwtToken = jwtService.generateToken(profile);
            revokeAllUserTokens(profile);
            saveUserToken(profile, jwtToken);
            logger.info("Authentication");
            return new AuthenticationResponse(
                    jwtToken
            );
        } else {
            throw new Exception("Verify email");
        }
    }

    public void saveUserToken(Profile profile, String jwtToken) {
        var token = new Token(
                jwtToken,
                TokenType.BEARER,
                false,
                false, profile
        );
        logger.info("save token");
        try {
            tokenRepository.save(token);
        } catch (Exception e) {
            logger.error("Failed to save new token", e.getCause());
            throw new RuntimeException("Failed to save new token", e.getCause());
        }
    }

    public void revokeAllUserTokens(Profile profile) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(profile.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        try {
            tokenRepository.saveAll(validUserTokens);
        } catch (Exception e) {
            logger.error("Failed to save all tokens\", e.getCause()");
            throw new RuntimeException("Failed to save all tokens", e.getCause());
        }
    }
}

