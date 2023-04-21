package com.example.habit_tracker.api;

import com.example.habit_tracker.data.dto.ProfileDTO;
import com.example.habit_tracker.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/profile")
@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ProfileDTO get() {
        return profileService.getDTO();
    }

    @PatchMapping("name")
    public ProfileDTO rename(@RequestBody List<String> name) {
        return profileService.rename(name);
    }

    @PatchMapping("icon")
    public ProfileDTO addIcon(@RequestBody String link) throws JsonProcessingException {
        return profileService.addIcon(link);
    }

    @DeleteMapping
    public void delete() {
        profileService.delete();
    }
}
