package com.example.habit_tracker.api;

import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/api/v1/profile")
@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    @GetMapping
    public Profile get() {
        return profileService.get();
    }
}
