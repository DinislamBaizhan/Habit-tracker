package com.example.habit_tracker.api;

import com.example.habit_tracker.data.entity.CalendarMark;
import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
public class CalendarMarkController {
    private final ProfileService profileService;

    public CalendarMarkController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<CalendarMark> get() {
        Profile profile = profileService.getAuthenticatedProfile();
        return profile.getCalendarMarks();
    }
}