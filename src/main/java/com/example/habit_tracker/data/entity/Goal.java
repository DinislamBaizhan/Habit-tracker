package com.example.habit_tracker.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int requiredRepetitionsPerDay;
    private int performedRepetitionsPerDay;

    @Value("${some.key:false}")
    private boolean allGoalsAchievedOnTheDay;
    private String unit;
    @OneToOne(mappedBy = "goal")
    @JsonIgnore
    private Habit habit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRequiredRepetitionsPerDay() {
        return requiredRepetitionsPerDay;
    }

    public void setRequiredRepetitionsPerDay(int quantity) {
        this.requiredRepetitionsPerDay = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public int getPerformedRepetitionsPerDay() {
        return performedRepetitionsPerDay;
    }

    public void addAchievedGoals(Integer achievedGoals) {
        this.performedRepetitionsPerDay += achievedGoals;
        if (this.performedRepetitionsPerDay == requiredRepetitionsPerDay) {
            allGoalsAchievedOnTheDay = true;
        }
    }

    public boolean isAllGoalsAchievedOnTheDay() {
        return allGoalsAchievedOnTheDay;
    }

    public void setAllGoalsAchievedOnTheDay(boolean allGoalsAchievedOnTheDay) {
        this.allGoalsAchievedOnTheDay = allGoalsAchievedOnTheDay;
    }
}
