package com.example.habit_tracker.data.entity;

import com.example.habit_tracker.data.Password;
import jakarta.validation.constraints.Email;

public class RegisterDTO {
    private String firstname;
    private String lastname;
    @Email
    private String email;
    @Password
    private String password;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public RegisterDTO(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }
}