package com.example.habit_tracker.utils;

import com.example.habit_tracker.data.entity.CalendarMark;
import com.example.habit_tracker.data.entity.Habit;
import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.exception.DataNotFound;
import com.example.habit_tracker.repository.CalendarMarkRepository;
import com.example.habit_tracker.repository.GoalRepository;
import com.example.habit_tracker.repository.HabitRepository;
import com.example.habit_tracker.repository.ProfileRepository;
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
    private final ProfileRepository profileRepository;
    private final HabitService habitService;
    private final HabitRepository habitRepository;
    private final GoalRepository goalRepository;
    private final CalendarMarkRepository calendarMarkRepository;

    public CustomTaskScheduler(ProfileService profileService,
                               ProfileRepository profileRepository, HabitService habitService,
                               HabitRepository habitRepository,
                               GoalRepository goalRepository,
                               CalendarMarkRepository calendarMarkRepository) {
        this.profileService = profileService;
        this.profileRepository = profileRepository;
        this.habitService = habitService;
        this.habitRepository = habitRepository;
        this.goalRepository = goalRepository;
        this.calendarMarkRepository = calendarMarkRepository;
    }

    @Transactional
//    @Scheduled(cron = "0 1 1 * * *")
    @Scheduled(cron = "0 * * * * *")
    public void calendarScheduler() {
        LocalDate today = LocalDate.now();
        today = today.plusDays(1);

        CalendarMark complete = new CalendarMark();
        CalendarMark notComplete = new CalendarMark();
        CalendarMark notHabit = new CalendarMark();

        complete.setMarksDate(today);
        notComplete.setMarksDate(today);
        notHabit.setMarksDate(today);

        Iterable<Profile> profiles = profileRepository.findAll();

        for (Profile profile : profiles) {

            List<Habit> habits = habitRepository.findHabitsByProfileId(profile.getId())
                    .orElseThrow(() -> new DataNotFound("habits not found for profile: " + profile.getEmail()));

            if (habits.isEmpty()) {
                profile.addCalendarMark(notHabit);
                calendarMarkRepository.save(notHabit);
            }

            for (Habit habit : habits) {

                if (habit.getStartDate().isBefore(today) && habit.getEndDate().isAfter(today)) {

                    if (habit.getGoal().isAllGoalsAchievedOnTheDay()) {

                        profile.addCounterDay();
                        complete.setName("Today we mastered the habit with the name: "
                                + habit.getName() + " and id: "
                                + habit.getId());

                        profile.addCalendarMark(complete);
                        profile.addCounterDay();
                        calendarMarkRepository.save(complete);
                    } else {
                        notComplete.setName("Today you haven't mastered the habit");
                        profile.addCalendarMark(notComplete);
                        calendarMarkRepository.save(notComplete);
                    }
                }
            }
        }
    }
}
