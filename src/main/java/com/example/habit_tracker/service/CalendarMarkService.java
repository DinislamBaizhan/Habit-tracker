package com.example.habit_tracker.service;

import com.example.habit_tracker.repository.CalendarMarkRepository;
import org.springframework.stereotype.Service;

@Service
public class CalendarMarkService {
    private final ProfileService profileService;
    private final CalendarMarkRepository calendarMarkRepository;

    public CalendarMarkService(ProfileService profileService, CalendarMarkRepository calendarMarkRepository) {
        this.profileService = profileService;
        this.calendarMarkRepository = calendarMarkRepository;
    }

//    public List<CalendarMarkDTO> get() {
//        Profile profile = profileService.getAuthenticatedProfile();
//        CalendarMark calendarMark = calendarMarkRepository.findById(profile.getId()).get();
//
//        CalendarMarkDTO f = new CalendarMarkDTO(
//                calendarMark.getMarksDate(),
//                calendarMark.getName(),
//                calendarMark.getCounterDay()
//        );
//
//    }
}
