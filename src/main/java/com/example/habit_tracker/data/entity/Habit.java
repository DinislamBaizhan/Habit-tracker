package com.example.habit_tracker.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;


    //    //////////////////////////////////////////////////////////////////
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @OneToMany(mappedBy = "habit", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CalendarMark> calendarMarks;

    private LocalDate startDate;
    private LocalDate endDate;

    public Habit(Long id, String name, Profile profile, Goal goal, List<CalendarMark> calendarMarks, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.goal = goal;
        this.calendarMarks = calendarMarks;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Habit() {
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    //    ////////////////////////////////////////////////////////////////


    public List<CalendarMark> getCalendarMarks() {
        return calendarMarks;
    }

    public void setCalendarMarks(List<CalendarMark> calendarMarks) {
        this.calendarMarks = calendarMarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}