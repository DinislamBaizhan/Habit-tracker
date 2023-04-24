package com.example.habit_tracker.repository;
import com.example.habit_tracker.data.entity.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    Optional<Profile> findByEmail(String email);
    Optional<Profile> getProfileByEmail(String email);

}
