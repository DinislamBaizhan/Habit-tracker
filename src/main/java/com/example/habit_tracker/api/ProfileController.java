package com.example.habit_tracker.api;

import com.example.habit_tracker.data.dto.ProfileDTO;
import com.example.habit_tracker.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/profile")
@RestController
@Tag(name = "profile controller", description = "Profile management")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Operation(summary = "Get user DTO")
    public ProfileDTO get() {
        return profileService.getDTO();
    }

    @PatchMapping("/name")
    @Operation(summary = "Update user name")
    public ProfileDTO rename(@RequestBody List<String> name) {
        return profileService.rename(name);
    }

    @PatchMapping("/icon")
    @Operation(summary = "Update user icon")
    public ProfileDTO addIcon(@RequestBody String link) throws JsonProcessingException {
        return profileService.addIcon(link);
    }

    @PatchMapping("/language")
    @Operation(summary = "Update user language")
    public ProfileDTO language(@RequestBody String language) throws JsonProcessingException {
        return profileService.addLanguage(language);
    }

    @PatchMapping("/color")
    @Operation(summary = "Update user color scheme")
    public ProfileDTO color(@RequestBody String color) throws JsonProcessingException {
        return profileService.addColor(color);
    }

    @DeleteMapping
    @Operation(summary = "Delete user profile")
    public void delete() {
        profileService.delete();
    }
}
