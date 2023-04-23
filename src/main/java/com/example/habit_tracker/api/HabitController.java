package com.example.habit_tracker.api;

import com.example.habit_tracker.data.entity.Habit;
import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.exception.DataNotFound;
import com.example.habit_tracker.repository.HabitRepository;
import com.example.habit_tracker.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habit")
public class HabitController {
    private final ProfileService profileService;
    private final HabitRepository habitRepository;

    public HabitController(ProfileService profileService, HabitRepository habitRepository) {
        this.profileService = profileService;
        this.habitRepository = habitRepository;
    }

    @PostMapping
    public Habit post(@RequestBody Habit habit) {
        Profile profile = profileService.getAuthenticatedProfile();
         profile.addHabits(habit);
         habitRepository.save(habit);
         return habit;
    }
    @GetMapping
    public List<Habit> get() {
        Profile profile = profileService.getAuthenticatedProfile();
        return profile.getHabits();
    }
    @GetMapping("/{id}")
    public Habit get(@PathVariable Long id) {
        Profile profile = profileService.getAuthenticatedProfile();
        return habitRepository.findByIdAndProfileId(id, profile.getId());
    }
}
