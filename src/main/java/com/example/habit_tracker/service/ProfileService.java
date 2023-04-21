package com.example.habit_tracker.service;

import com.example.habit_tracker.data.dto.ProfileDTO;
import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.data.entity.RegisterDTO;
import com.example.habit_tracker.data.enums.Role;
import com.example.habit_tracker.exception.DataNotFound;
import com.example.habit_tracker.exception.DuplicateKey;
import com.example.habit_tracker.repository.ProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    Logger logger = LogManager.getLogger();

    public ProfileService(ProfileRepository repository, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }


    public Profile getAuthenticatedProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        return repository.findByEmail(email).orElseThrow(
                () -> {
                    logger.error("profile not found", email);
                    return new DataNotFound("profile not found");
                }
        );
    }


    public Profile createUser(@Valid RegisterDTO request) {

        Optional<Profile> profile = repository.findByEmail(request.getEmail());

        if (profile.isPresent() && profile.get().getWaitingForVerification()) {
            return profile.get();
        } else if (profile.isPresent() && !profile.get().getWaitingForVerification()) {
            logger.error("Profile already registered", request.getEmail());
            throw new DuplicateKey("Profile already registered");
        }
        try {
            Profile newProfile = new Profile(
                    request.getFirstname(),
                    request.getLastname(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword()),
                    Role.USER,
                    true
            );

            return repository.save(newProfile);
        } catch (DataAccessException e) {
            logger.error("Failed to create new profile", e.getCause());
            throw new RuntimeException("Failed to create new profile", e.getCause());
        }
    }

    public void updateVerify(Profile profile) throws Exception {
        try {
            profile.setEnabled(true);
            profile.setWaitingForVerification(false);
            repository.save(profile);
        } catch (Exception e) {
            logger.error("error verify profile" + e.getMessage(), e.getCause());
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    public ProfileDTO getDTO() {
        Profile profile = getAuthenticatedProfile();
        return new ProfileDTO(
                profile.getFirstname(),
                profile.getLastname(),
                profile.getEmail(),
                profile.getIconLink(),
                profile.getLanguage(),
                profile.getColor()
        );
    }

    public Profile findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> {
                    logger.error("user not found", email);
                    return new DataNotFound("profile not found");
                }
        );
    }

    public Profile updPassword(Profile profile, String password) {
        profile.setPassword(passwordEncoder.encode(password));
        try {
            return repository.save(profile);
        } catch (Exception e) {
            throw new RuntimeException("fail to update password " + e.getCause());
        }
    }

    public ProfileDTO rename(List<String> name) {
        Profile profile = getAuthenticatedProfile();
        profile.setFirstname(name.get(0));
        profile.setLastname(name.get(1));
        logger.info("rename: firstname - " + name.get(0) + "lastname -" + name.get(1));
        repository.save(profile);
        return getDTO();
    }

    public ProfileDTO addIcon(String link) throws JsonProcessingException {
        ProfileDTO pDTO = objectMapper.readValue(link, ProfileDTO.class);
        Profile profile = getAuthenticatedProfile();
        profile.setIconLink(pDTO.getIcon());
        save(profile);
        logger.info("add icon");
        return getDTO();
    }

    public void delete() {
        Profile profile = getAuthenticatedProfile();
        try {
            repository.delete(profile);
            logger.info("profile deleted " + profile.getEmail());
        } catch (DataAccessException e) {
            logger.trace("profile not found " + e.getMessage() + "cause" + e.getCause());
            throw new DataNotFound("profile not found " + e.getMessage());
        }
    }


    public Profile save(Profile profile) {
        try {
            return repository.save(profile);
        } catch (Exception e) {
            logger.error("fail save to database " + profile);
            throw new RuntimeException("fail save to database " + profile);
        }
    }
}
