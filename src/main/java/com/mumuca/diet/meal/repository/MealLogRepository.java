package com.mumuca.diet.meal.repository;

import com.mumuca.diet.meal.dto.MealNutritionalInformationDTO;
import com.mumuca.diet.meal.model.MealLog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, String> {
    Optional<MealLog> findMealLogByIdAndUserId(String mealId, String userId);

    @EntityGraph(attributePaths = {"foods"})
    @Query("SELECT ml from MealLog ml WHERE ml.id = :mealId AND ml.user.id = :userId")
    Optional<MealLog> findMealLogByIdAndUserIdWithFoods(String mealId, String userId);

    @EntityGraph(attributePaths = {"meals"})
    @Query("SELECT ml from MealLog ml WHERE ml.id = :mealId AND ml.user.id = :userId")
    Optional<MealLog> findMealLogByIdAndUserIdWithMeals(String mealId, String userId);

    // TODO: Remove the Optional, because this method always returns the DTO
    @Query("""
        SELECT new com.mumuca.diet.meal.dto.MealNutritionalInformationDTO(
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
        FROM MealLog ml
        LEFT JOIN ml.foods mealLogFoods
        LEFT JOIN mealLogFoods.nutritionalInformation ni
        WHERE ml.id = :mealLogId AND ml.user.id = :userId
    """)
    Optional<MealNutritionalInformationDTO> sumFoodsNutritionalInformationByMealLogIdAndUserId(
            @Param("mealLogId") String mealLogId,
            @Param("userId") String userId
    );

    // TODO: Remove the Optional, because this method always returns the DTO
    @Query("""
        SELECT new com.mumuca.diet.meal.dto.MealNutritionalInformationDTO(
            SUM(mfi.calories),
            SUM(mfi.carbohydrates),
            SUM(mfi.protein),
            SUM(mfi.fat),
            SUM(mfi.monounsaturatedFat),
            SUM(mfi.saturatedFat),
            SUM(mfi.polyunsaturatedFat),
            SUM(mfi.transFat),
            SUM(mfi.cholesterol),
            SUM(mfi.sodium),
            SUM(mfi.potassium),
            SUM(mfi.fiber),
            SUM(mfi.sugar),
            SUM(mfi.calcium),
            SUM(mfi.iron),
            SUM(mfi.vitaminA),
            SUM(mfi.vitaminC)
        )
        FROM MealLog ml
        LEFT JOIN ml.meals mealLogMeals
        LEFT JOIN mealLogMeals.foods mealFoods
        LEFT JOIN mealFoods.nutritionalInformation mfi
        WHERE ml.id = :mealLogId AND ml.user.id = :userId
    """)
    Optional<MealNutritionalInformationDTO> sumMealsNutritionalInformationByMealLogIdAndUserId(
            @Param("mealLogId") String mealLogId,
            @Param("userId") String userId
    );

    //TODO: Test this
    @Query("""
       SELECT COALESCE(SUM(ni.calories), 0)
       FROM MealLog ml
       LEFT JOIN ml.foods mealLogFoods
       LEFT JOIN mealLogFoods.nutritionalInformation ni
       WHERE ml.id = :mealLogId AND ml.user.id = :userId
       """)
    Integer sumCaloriesFromFoodsByMealLogIdAndUserId(@Param("mealLogId") String mealLogId, @Param("userId") String userId);

    @Query("""
       SELECT COALESCE(SUM(mfi.calories), 0)
       FROM MealLog ml
       LEFT JOIN ml.meals mealLogMeals
       LEFT JOIN mealLogMeals.foods mealFoods
       LEFT JOIN mealFoods.nutritionalInformation mfi
       WHERE ml.id = :mealLogId AND ml.user.id = :userId
       """)
    Integer sumCaloriesFromMealsByMealLogIdAndUserId(@Param("mealLogId") String mealLogId, @Param("userId") String userId);

    List<MealLog> findByDateAndUserId(LocalDate date, String userId);

    boolean existsByIdAndUserId(String mealLogId, String userId);

    @Query("SELECT COUNT(f.id) > 0 FROM MealLog ml JOIN ml.foods f WHERE ml.id = :mealLogId AND ml.user.id = :userId")
    boolean existsFoodsByIdAndUserId(@Param("mealLogId") String mealLogId, @Param("userId") String userId);

    @Query("SELECT COUNT(m.id) > 0 FROM MealLog ml JOIN ml.meals m WHERE ml.id = :mealLogId AND ml.user.id = :userId")
    boolean existsMealsByIdAndUserId(@Param("mealLogId") String mealLogId, @Param("userId") String userId);
}
