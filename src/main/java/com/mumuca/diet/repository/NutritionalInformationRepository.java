package com.mumuca.diet.repository;

import com.mumuca.diet.dto.meal.MealNutritionalInformationDTO;
import com.mumuca.diet.model.NutritionalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionalInformationRepository extends JpaRepository<NutritionalInformation, String> {
    @Query("""
        SELECT new com.mumuca.diet.dto.meal.MealNutritionalInformationDTO(
            SUM(ni.calories),
            SUM(ni.carbohydrates),
            SUM(ni.protein),
            SUM(ni.fat),
            SUM(ni.monounsaturatedFat),
            SUM(ni.saturatedFat),
            SUM(ni.polyunsaturatedFat),
            SUM(ni.transFat),
            SUM(ni.cholesterol),
            SUM(ni.sodium),
            SUM(ni.potassium),
            SUM(ni.fiber),
            SUM(ni.sugar),
            SUM(ni.calcium),
            SUM(ni.iron),
            SUM(ni.vitaminA),
            SUM(ni.vitaminC)
        )
        FROM Meal meal
        JOIN meal.foods mealFoods
        JOIN mealFoods.nutritionalInformation ni
        WHERE meal.id = :mealId AND mealFoods.user.id = :userId
    """)
    Optional<MealNutritionalInformationDTO> sumNutritionalInformationByMealIdAndUserId(
            @Param("mealId") String mealId,
            @Param("userId") String userId
    );
}
