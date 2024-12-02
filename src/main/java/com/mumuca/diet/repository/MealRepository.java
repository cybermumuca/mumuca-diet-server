package com.mumuca.diet.repository;

import com.mumuca.diet.model.Meal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, String> {
    Optional<Meal> findByIdAndUserId(String id, String userId);

    @EntityGraph(attributePaths = {"foods"})
    @Query("SELECT m FROM Meal m WHERE m.id = :mealId AND m.user.id = :userId")
    Optional<Meal> findByIdAndUserIdWithFoods(@Param("mealId") String mealId, @Param("userId") String userId);
}
