package com.example.habit_tracker.api;

import com.example.habit_tracker.data.entity.CalendarMark;
import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
@Tag(name = "calendar controller", description = "Calendar days management")
public class CalendarMarkController {
    private final ProfileService profileService;

    public CalendarMarkController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Operation(summary = "Get all calendarMarks for a user")
    public List<CalendarMark> get() {
        Profile profile = profileService.getAuthenticatedProfile();
        return profile.getCalendarMarks();
    }
}
