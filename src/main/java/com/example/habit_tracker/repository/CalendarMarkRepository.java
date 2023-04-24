package com.example.habit_tracker.repository;

import com.example.habit_tracker.data.entity.CalendarMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarMarkRepository extends JpaRepository<CalendarMark, Long> {
}
