package com.mumuca.diet.meal.repository;

import com.mumuca.diet.meal.model.MealLogPreference;
import com.mumuca.diet.meal.model.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealLogPreferenceRepository extends JpaRepository<MealLogPreference, String> {
    Optional<MealLogPreference> findMealLogPreferenceByIdAndUserId(String mealLogPreferenceId, String userId);
    List<MealLogPreference> findAllMealLogPreferencesByUserId(String userId);
    boolean existsByTypeAndUserId(MealType mealType, String userId);
}
