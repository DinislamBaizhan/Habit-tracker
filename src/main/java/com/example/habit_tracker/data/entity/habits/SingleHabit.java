package com.example.habit_tracker.data.entity.habits;

import com.example.habit_tracker.data.enums.RepeatType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "habits")
public class SingleHabit extends Habit {

    public SingleHabit(Integer userId, String name, String description, String category,
                       RepeatType repeatType, int repeatTarget, LocalDate endDate) {
        super(userId, name, description, category, repeatType, repeatTarget, endDate);
    }
}
