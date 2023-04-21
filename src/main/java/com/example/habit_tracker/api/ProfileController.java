package com.example.habit_tracker.api;

import com.example.habit_tracker.data.dto.ProfileDTO;
import com.example.habit_tracker.service.ProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/profile")
@RestController
@PreAuthorize("isAuthenticated()")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ProfileDTO get() {
        return profileService.getDTO();
    }

    @DeleteMapping
    public void delete() {
        profileService.delete();
    }
}
