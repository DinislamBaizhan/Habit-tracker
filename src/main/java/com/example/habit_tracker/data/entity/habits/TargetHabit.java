package com.example.habit_tracker.data.entity.habits;

import com.example.habit_tracker.data.enums.RepeatType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "habits")
public class TargetHabit extends Habit {
    private int dailyTarget;
    private int dailyActual;
    private String unit;

    public TargetHabit(Integer userId, String name, String description, String category,
                       RepeatType repeatType, int repeatTarget, LocalDate endDate,
                       int dailyTarget, String unit) {
        super(userId, name, description, category, repeatType, repeatTarget, endDate);
        this.dailyTarget = dailyTarget;
        this.dailyActual = 0;
        this.unit = unit;

    }

}
