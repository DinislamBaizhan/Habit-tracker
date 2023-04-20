package com.example.habit_tracker.service;

import com.example.habit_tracker.data.entity.*;
import com.example.habit_tracker.data.enums.TokenType;
import com.example.habit_tracker.data.request.AuthenticationRequest;
import com.example.habit_tracker.data.response.AuthenticationResponse;
import com.example.habit_tracker.repository.TokenRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    Logger logger = LogManager.getLogger();
    private final ProfileService profileService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(ProfileService profileService, TokenRepository tokenRepository,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.profileService = profileService;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(@Valid RegisterDTO request) {

        Profile profile = profileService.createUser(request);
        String jwtToken = jwtService.generateToken(profile);
        saveUserToken(profile, jwtToken);
        return new AuthenticationResponse(
                jwtToken
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var profile = profileService.findByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(profile);
        revokeAllUserTokens(profile);
        saveUserToken(profile, jwtToken);
        logger.info("Authentication");
        return new AuthenticationResponse(
                jwtToken
        );
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
       }catch (Exception e) {
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

