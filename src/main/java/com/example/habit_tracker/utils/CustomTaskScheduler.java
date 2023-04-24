package com.example.habit_tracker.utils;

import com.example.habit_tracker.data.entity.CalendarMark;
import com.example.habit_tracker.data.entity.Habit;
import com.example.habit_tracker.repository.CalendarMarkRepository;
import com.example.habit_tracker.repository.GoalRepository;
import com.example.habit_tracker.repository.HabitRepository;
import com.example.habit_tracker.service.HabitService;
import com.example.habit_tracker.service.ProfileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
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
    @Scheduled(cron = "0 * * * * *")
    public void myMethod() {
        List<Habit> habits = habitRepository.findAll();

        CalendarMark goalsAchievedMark = new CalendarMark();
        goalsAchievedMark.setName("goals achieved");
        CalendarMark notFoundMark = new CalendarMark();
        notFoundMark.setName("not found");

        for (Habit habit : habits) {
            CalendarMark mark = new CalendarMark();
            mark.setMarksDate(Calendar.getInstance());
            habit.addCalendarMark(mark);

            if (habit.getGoal().isAllGoalsAchievedOnTheDay()) {
                mark.setName(goalsAchievedMark.getName() + ": " + habit.getName());
                calendarMarkRepository.save(mark);
            } else {
                mark.setName(notFoundMark.getName());
                calendarMarkRepository.save(mark);
            }
        }
    }


    //    @Scheduled(cron = "0 1 1 * * *")
//    @Transactional
//    @Scheduled(cron = "0 * * * * *")
//    public void myMethod() {
//        CalendarMark everyDay = new CalendarMark();
//        Calendar currenDate = Calendar.getInstance();
//        everyDay.setMarksDate(currenDate);
//
//        List<Habit> habits = habitRepository.findAll();
//
//        for (Habit habit : habits) {
//            habit.addCalendarMark(everyDay);
//            if (habit.getGoal().isAllGoalsAchievedOnTheDay()) {
//
//                everyDay.setName("goals achieved: " + habit.getName());
//                calendarMarkRepository.save(everyDay);
//
//            } else {
//                everyDay.setName("not found");
//                calendarMarkRepository.save(everyDay);
//            }
//
//        }
//    }

}
