package com.example.habit_tracker.service;

import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.data.dto.RegisterDTO;
import com.example.habit_tracker.data.entity.Token;
import com.example.habit_tracker.data.enums.TokenType;
import com.example.habit_tracker.data.request.AuthenticationRequest;
import com.example.habit_tracker.data.response.AuthenticationResponse;
import com.example.habit_tracker.repository.TokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final ProfileService profileService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    @PersistenceContext
    private final EntityManager entityManager;
    Logger logger = LogManager.getLogger();

    public AuthenticationService(ProfileService profileService,
                                 TokenRepository tokenRepository,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager,
                                 EmailService emailService,
                                 ObjectMapper objectMapper, EntityManager entityManager) {
        this.profileService = profileService;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
        this.entityManager = entityManager;
    }
    @Transactional
    public void register(@Valid RegisterDTO registerDTO) {

        logger.info("register profile" + registerDTO.getEmail());
        Profile profile = profileService.createUser(registerDTO);
        int tokenExpiredDate = 1000 * 60 * 60 * 24;
        String jwtToken = jwtService.generateToken(profile, tokenExpiredDate);
        saveUserToken(profile, jwtToken);

        String link = "http://localhost:8080/api/v1/auth/verify-email?token=" + jwtToken;

        String emailContent = "<html><body><p>Нажмите на кнопку, чтобы подтвердить свой аккаунт:</p>" +
                "<form method='POST' action='" + link + "'><input type='submit' value='Verify Email'/>"
                + "</form></body></html>";

        emailService.sendEmail(profile.getEmail(), "Email Verification", emailContent);
    }
    @Transactional
    public void resetPassword(String email) throws JsonProcessingException {

        AuthenticationRequest mappedEmail = objectMapper.readValue(email, AuthenticationRequest.class);

        int tokenExpiredDate = 1000 * 60 * 30;
        Profile profile = profileService.findByEmail(mappedEmail.getEmail());

        String jwtToken = jwtService.generateToken(profile, tokenExpiredDate);
        revokeAllUserTokens(profile);
        saveUserToken(profile, jwtToken);


        String link = "http://localhost:8080/api/v1/auth/reset-password?token=" + jwtToken;
        emailService.sendEmail(profile.getEmail(), "Password Reset", "Click on this link to reset tour password: " + link);
    }
    @Transactional
    public void updatePassword(Profile profile, String password) throws Exception {

        AuthenticationRequest mappedPassword = objectMapper.readValue(password, AuthenticationRequest.class);

        int tokenExpiredDate = 1000 * 60 * 60 * 24;
        Profile updatedProfile = profileService.updPassword(profile, mappedPassword.getPassword());
        String jwtToken = jwtService.generateToken(updatedProfile, tokenExpiredDate);
        revokeAllUserTokens(profile);
        saveUserToken(profile, jwtToken);
    }
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {

        Profile profile = profileService.findByEmail(request.getEmail());
        if (profile.isEnabled()) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            int tokenExpiredDate = 1000 * 60 * 60 * 24;
            var jwtToken = jwtService.generateToken(profile, tokenExpiredDate);
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
//    @Transactional
    public void saveUserToken(Profile profile, String jwtToken) {
        var token = new Token(
                jwtToken,
                TokenType.BEARER,
                false,
                false, profile
        );
        logger.info("save token");
        try {
            if (!entityManager.contains(token)){
                token = entityManager.merge(token);
            }
            entityManager.flush();
        } catch (Exception e) {
            logger.error("Failed to save new token", e.getCause());
            throw new RuntimeException("Failed to save new token", e.getCause());
        }
    }

    public void revokeAllUserTokens(Profile profile) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(profile.getId());
        if (validUserTokens.isEmpty()) {
            logger.info("tokens not found");
            return;
        }
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

