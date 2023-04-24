package com.example.habit_tracker.api;

import com.example.habit_tracker.data.entity.Goal;
import com.example.habit_tracker.data.entity.Habit;
import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.service.HabitService;
import com.example.habit_tracker.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.Hibernate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habit")
@Tag(name = "habit controller", description = "Habit management")
public class HabitController {
    private final ProfileService profileService;
    private final HabitService habitService;

    public HabitController(ProfileService profileService, HabitService habitService) {
        this.profileService = profileService;

        this.habitService = habitService;
    }

    @PostMapping
    @Operation(summary = "Create new habit")
    public Habit post(@RequestBody Habit habit) throws Exception {
        habit.getGoal().setRequiredRepetitionsPerDay(1);
        return habitService.post(habit);
    }

    @GetMapping
    @Operation(summary = "Get the list of existing habits for the profile")
    public List<Habit> get() {
        return habitService.get();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the habit via habit ID")
    public Habit get(@PathVariable Long id) {
        return habitService.get(id);
    }

    @PostMapping("/{habitId}/goal")
    @Operation(summary = "Update habit goal with new value")
    public Goal increaseAchievedGoals(
            @PathVariable Long habitId,
            @RequestBody int value) throws Exception {

        return habitService.increaseGoals(habitId, value);
    }
}
