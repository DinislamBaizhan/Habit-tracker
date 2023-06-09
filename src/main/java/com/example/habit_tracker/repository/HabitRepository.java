package com.example.habit_tracker.repository;

import com.example.habit_tracker.data.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    Optional<Habit> findByIdAndProfileId(Long habitId, Long profileId);

    Optional<List<Habit>> findHabitsByProfileId(Long profileId);
}
