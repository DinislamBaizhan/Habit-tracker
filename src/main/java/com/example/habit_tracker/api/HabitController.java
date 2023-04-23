package com.example.habit_tracker.api;

import com.example.habit_tracker.data.entity.Goal;
import com.example.habit_tracker.data.entity.Habit;
import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.service.HabitService;
import com.example.habit_tracker.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habit")
public class HabitController {
    private final ProfileService profileService;
    private final HabitService habitService;

    public HabitController(ProfileService profileService, HabitService habitService) {
        this.profileService = profileService;

        this.habitService = habitService;
    }

    @PostMapping
    public Habit post(@RequestBody Habit habit) throws Exception {
        habit.getGoal().setRequiredRepetitionsPerDay(1);
        return habitService.post(habit);
    }

    @GetMapping
    public List<Habit> get() {
        Profile profile = profileService.getAuthenticatedProfile();
        return profile.getHabits();
    }

    @GetMapping("/{id}")
    public Habit get(@PathVariable Long id) {
        return habitService.get(id);
    }

    @PostMapping("/{habitId}/goal")
    public Goal increaseAchievedGoals(@PathVariable Long habitId, @RequestBody int value) {
        return habitService.increaseGoals(habitId, value);
    }
}
