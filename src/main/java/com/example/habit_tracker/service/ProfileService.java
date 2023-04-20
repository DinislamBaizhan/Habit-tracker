package com.example.habit_tracker.service;

import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.data.entity.RegisterDTO;
import com.example.habit_tracker.data.enums.Role;
import com.example.habit_tracker.exception.DataNotFound;
import com.example.habit_tracker.exception.UnauthorizedException;
import com.example.habit_tracker.repository.ProfileRepository;
import org.jboss.logging.Log4j2LoggerProvider;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.habit_tracker.exception.*;

import java.util.Optional;

@Service
public class ProfileService {
//    private static Log4j2LoggerProvider;
    private final ProfileRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(ProfileRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    private Profile getAuthenticatedProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return repository.findByEmail(login).orElseThrow(
                () -> {
//                    log.error("user not found");
                    throw new DataNotFound("profile not found");
                }
        );
    }


    public Profile createUser(RegisterDTO request) {

        Optional<Profile> profile = repository.findByEmail(request.getEmail());
        if (profile.isPresent()) {
            throw new DuplicateKey("Profile already registered");
        }

        try {
            var newProfile = new Profile(
                    request.getFirstname(),
                    request.getLastname(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword()),
                    Role.USER);

            return repository.save(newProfile);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to create new profile", e.getCause());
        }

    }

    public Profile get() {
        return getAuthenticatedProfile();
    }

    public Optional<Profile> findByEmail(String email) {
        return repository.findByEmail(email);
    }

//    public User edit(Long id, User user) {
//        Optional<User> optionalUser = repository.findById(id);
//        if (optionalUser.isPresent()) {
//            optionalUser.get().setFirstname(user.getFirstname());
//            optionalUser.get().setLastname(user.getLastname());
//
//            return repository.save(optionalUser.get());
//        }
//        return null;
//    }
}
