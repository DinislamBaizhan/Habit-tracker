package com.example.habit_tracker.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Calendar;

@Entity
public class CalendarMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "EEEE dd MMMM, a z")
    private Calendar marksDate;
    private String name;
    private int counterDay;

    @ManyToOne
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "habit_id")
    @JsonIgnore
    private Habit habit;

    public CalendarMark(Long id, Calendar marksDate, String name, Habit habit) {
        this.id = id;
        this.marksDate = marksDate;
        this.name = name;
        this.habit = habit;
    }

    public CalendarMark() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getMarksDate() {
        return marksDate;
    }

    public void setMarksDate(Calendar marksDate) {
        this.marksDate = marksDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public int getCounterDay() {
        return counterDay;
    }

    public void addCounterDay() {
        counterDay++;
    }
}
