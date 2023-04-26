package com.example.habit_tracker.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;

@Entity
public class CalendarMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate marksDate;
    private String name;

    @ManyToOne
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    @JsonIgnore
    private Profile profile;

    public CalendarMark(Long id, LocalDate marksDate, String name, Profile habit) {
        this.id = id;
        this.marksDate = marksDate;
        this.name = name;
        this.profile = habit;
    }

    public CalendarMark() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMarksDate() {
        return marksDate;
    }

    public void setMarksDate(LocalDate marksDate) {
        this.marksDate = marksDate;
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
}
