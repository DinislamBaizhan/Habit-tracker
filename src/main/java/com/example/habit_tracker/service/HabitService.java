package com.example.habit_tracker.service;

import com.example.habit_tracker.data.entity.Goal;
import com.example.habit_tracker.data.entity.Habit;
import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.exception.DataNotFound;
import com.example.habit_tracker.repository.GoalRepository;
import com.example.habit_tracker.repository.HabitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final ProfileService profileService;
    private final GoalRepository goalRepository;

    public HabitService(HabitRepository habitRepository, ProfileService profileService, GoalRepository goalRepository) {
        this.habitRepository = habitRepository;
        this.profileService = profileService;
        this.goalRepository = goalRepository;
    }

    public Habit post(Habit habit) throws Exception {
        Profile profile = profileService.getAuthenticatedProfile();
        habit.setProfile(profile);
        try {
            return habitRepository.save(habit);
        } catch (Exception e) {
            throw new Exception("fail save habit " + e);
        }
    }

    public List<Habit> get() {
        Profile profile = profileService.getAuthenticatedProfile();
        return habitRepository.findHabitsByProfileId(profile.getId())
                .orElseThrow(()
                -> new DataNotFound("data not found"));
    }

    public Habit get(Long habitId) {
        Profile profile = profileService.getAuthenticatedProfile();
        return habitRepository.findByIdAndProfileId(habitId, profile.getId())
                .orElseThrow(() -> new DataNotFound("habit not found for Id " + habitId));
    }


    public Goal increaseGoals(Long habitId, int value) throws Exception {
        Habit habit = get(habitId);

        LocalDate today = LocalDate.now();

        if (habit.getStartDate().isAfter(today)
                && habit.getEndDate().isBefore(today)) {
            throw new Exception("enter data between " + habit.getStartDate()
                    + " and " + habit.getEndDate() + " days");
        }

        if(habit.getGoal().isAllGoalsAchievedOnTheDay()) {
            throw new Exception("All goals for today have been achieved!");
        } else {
            Goal goal = goalRepository.findByHabitId(habitId).orElseThrow(() ->
                    new DataNotFound("goal not found for id: " + habit));
            goal.addAchievedGoals(value);
            return goalRepository.save(goal);
        }

    }
}
