package com.example.habit_tracker.repository;

import com.example.habit_tracker.data.entity.Profile;
import com.example.habit_tracker.data.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from Token t inner join Profile u\s
            on t.profile.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);

    Token findTokenByProfile(Profile profile);

    Token getTokenByProfile(Profile profile);
}
