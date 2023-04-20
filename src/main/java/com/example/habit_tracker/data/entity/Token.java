package com.example.habit_tracker.data.entity;
import com.example.habit_tracker.data.enums.TokenType;
import jakarta.persistence.*;

@Entity
public class Token extends BaseEntity {

    @Id
    @GeneratedValue
    public Long id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    public Profile profile;

    public Token(String token, TokenType tokenType, boolean revoked, boolean expired, Profile profile) {
        this.token = token;
        this.tokenType = tokenType;
        this.revoked = revoked;
        this.expired = expired;
        this.profile = profile;
    }

    public Token() {

    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

}
