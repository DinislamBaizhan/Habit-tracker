package com.example.habit_tracker.data.dto;

import com.example.habit_tracker.data.enums.Color;
import com.example.habit_tracker.data.enums.Language;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String icon;
    private Language language;
    @Schema(description = "User-defined color scheme for the app")
    private Color color;

    public ProfileDTO(String firstname, String lastname, String email, String icon, Language language, Color color) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.icon = icon;
        this.language = language;
        this.color = color;
    }

    public ProfileDTO() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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
