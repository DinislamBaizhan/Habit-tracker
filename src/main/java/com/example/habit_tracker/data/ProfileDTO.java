package com.example.habit_tracker.data;

import com.example.habit_tracker.data.entity.Profile;

public class ProfileDTO {
    private String firstname;

    private String lastname;

    private String email;

    public ProfileDTO(Profile profile) {
        this.firstname = profile.getFirstname();
        this.lastname = profile.getLastname();
        this.email = profile.getEmail();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
