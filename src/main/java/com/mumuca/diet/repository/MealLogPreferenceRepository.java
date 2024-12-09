package com.mumuca.diet.repository;

import com.mumuca.diet.model.MealLogPreference;
import com.mumuca.diet.model.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealLogPreferenceRepository extends JpaRepository<MealLogPreference, String> {
    Optional<MealLogPreference> findMealLogPreferenceByIdAndUserId(String mealLogPreferenceId, String userId);
    List<MealLogPreference> findAllMealLogPreferencesByUserId(String userId);
    // TODO: JPA Query can be improved if written manually
    //  select mlp1_0.id from meal_log_preferences mlp1_0 where mlp1_0.type=? and mlp1_0.user_id=? fetch first ? rows only
    boolean existsByTypeAndUserId(MealType mealType, String userId);

}
