package com.mumuca.diet.repository;

import com.mumuca.diet.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, String> {
    Optional<Goal> findByUserId(String userId);
}
