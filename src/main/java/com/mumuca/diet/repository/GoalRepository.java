package com.mumuca.diet.repository;

import com.mumuca.diet.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, String> {
    @Query("SELECT g FROM Goal g WHERE g.user.id = :userId")
    Optional<Goal> findByUserId(@Param("userId") String userId);
    @Query("SELECT g.targetCalories FROM Goal g WHERE g.user.id = :userId")
    Optional<Integer> findTargetCaloriesByUserId(@Param("userId") String userId);
    void deleteByUserId(@Param("userId") String userId);
}
