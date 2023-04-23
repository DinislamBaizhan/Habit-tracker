package com.example.habit_tracker.data.entity;

import com.example.habit_tracker.data.Password;
import com.example.habit_tracker.data.enums.Color;
import com.example.habit_tracker.data.enums.Language;
import com.example.habit_tracker.data.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "profiles")
public class Profile implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 2, max = 20)
    private String firstname;
    @NotNull
    @Size(min = 2, max = 20)
    private String lastname;
    @Email
    @NotNull
    @Column(unique = true, nullable = false)
    private String email;
    @NonNull
    @Password
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonIgnore
    @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Token> tokens = new ArrayList<>();
    @JsonIgnore
    private Boolean isEnabled;
    @JsonIgnore
    private Boolean waitingForVerification = true;
    private String iconLink;
    @Enumerated(EnumType.STRING)
    private Language language;
    @Enumerated(EnumType.STRING)
    private Color color;
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habit> habits = new ArrayList<>();

    public Profile(String firstname,
                   String lastname,
                   String email, @NonNull
                   String password,
                   Role role,
                   Boolean waitingForVerification) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.waitingForVerification = waitingForVerification;
        isEnabled = false;
    }

    public Profile() {

    }

    public List<Habit> getHabits() {
        return habits;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
    }

    public void addHabits(Habit habit) {
        habits.add(habit);
        habit.setProfile(this);
    }

    public void deleteHabit(Habit habit) {
        habits.remove(habit);
        habit.setProfile(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @NonNull
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
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

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        this.isEnabled = enabled;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Boolean getWaitingForVerification() {
        return waitingForVerification;
    }

    public void setWaitingForVerification(Boolean waitingForVerification) {
        this.waitingForVerification = waitingForVerification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
