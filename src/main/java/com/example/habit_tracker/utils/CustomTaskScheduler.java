package com.example.habit_tracker.utils;

import com.example.habit_tracker.data.entity.CalendarMark;
import com.example.habit_tracker.data.entity.Habit;
import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.repository.CalendarMarkRepository;
import com.example.habit_tracker.repository.GoalRepository;
import com.example.habit_tracker.repository.HabitRepository;
import com.example.habit_tracker.service.HabitService;
import com.example.habit_tracker.service.ProfileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class CustomTaskScheduler {

    private final ProfileService profileService;
    private final HabitService habitService;
    private final HabitRepository habitRepository;
    private final GoalRepository goalRepository;
    private final CalendarMarkRepository calendarMarkRepository;

    public CustomTaskScheduler(ProfileService profileService,
                               HabitService habitService,
                               HabitRepository habitRepository,
                               GoalRepository goalRepository,
                               CalendarMarkRepository calendarMarkRepository) {
        this.profileService = profileService;
        this.habitService = habitService;
        this.habitRepository = habitRepository;
        this.goalRepository = goalRepository;
        this.calendarMarkRepository = calendarMarkRepository;
    }

    @Transactional
//    @Scheduled(cron = "0 1 1 * * *")
    @Scheduled(cron = "0 * * * * *")
    public void myMethod() {
        LocalDate counter = LocalDate.now();

        CalendarMark complete = new CalendarMark();
        CalendarMark notComplete = new CalendarMark();
        CalendarMark notHabit = new CalendarMark();

        complete.setMarksDate(counter);
        notComplete.setMarksDate(counter);
        notHabit.setMarksDate(counter);

        List<Habit> habits = habitRepository.findAll();

        for (Habit habit : habits) {
            LocalDate today = LocalDate.now();
            if (habit.getStartDate().isBefore(today) && habit.getEndDate().isAfter(today)) {

                if (habit.getGoal().isAllGoalsAchievedOnTheDay()) {

                    complete.addCounterDay();
                    complete.setName("Today we mastered the habit with the name: "
                            + habit.getName() + " and id: "
                            + habit.getId());
                    Profile profile = profileService.findByEmail(habit.getProfile().getEmail());
                    profile.addCalendarMark(complete);
                    complete.addCounterDay();
                    calendarMarkRepository.save(complete);
                } else {

                    notComplete.setName("Today you haven't mastered the habit");
                    Profile profile = profileService.findByEmail(habit.getProfile().getEmail());
                    profile.addCalendarMark(notComplete);
                    calendarMarkRepository.save(notComplete);
                }
            } else {
                Profile profile = profileService.findByEmail(habit.getProfile().getEmail());
                profile.addCalendarMark(notHabit);
                calendarMarkRepository.save(notHabit);
            }
        }
    }
}
