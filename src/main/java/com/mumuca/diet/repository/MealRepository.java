package com.mumuca.diet.repository;

import com.mumuca.diet.dto.meal.MealNutritionalInformationDTO;
import com.mumuca.diet.model.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, String> {
    Optional<Meal> findByIdAndUserId(String id, String userId);

    Page<Meal> findByUserId(Pageable pageable, String userId);

    @EntityGraph(attributePaths = {"foods"})
    @Query("SELECT m FROM Meal m WHERE m.id = :mealId AND m.user.id = :userId")
    Optional<Meal> findByIdAndUserIdWithFoods(@Param("mealId") String mealId, @Param("userId") String userId);

    List<Meal> findByFoodsIdAndUserId(String foodId, String userId);

    @EntityGraph(attributePaths = {"foods"})
    List<Meal> findAllByMealLogsIdAndUserId(String mealLogId, String userId);

    @Query("SELECT m FROM Meal m WHERE m.id in :ids AND m.user.id = :userId")
    List<Meal> findAllByIdsAndUserId(Iterable<String> ids, String userId);

    @Query("SELECT COUNT(m) FROM Meal m WHERE m.id IN :ids AND m.user.id = :userId")
    long countByIdsAndUserId(@Param("ids") Iterable<String> ids, @Param("userId") String userId);

    @Query("SELECT COUNT(f.id) > 0 FROM Meal m JOIN m.foods f WHERE m.id = :mealId AND m.user.id = :userId")
    boolean existsFoodsByMealIdAndUserId(@Param("mealId") String mealId, @Param("userId") String userId);

    boolean existsByIdAndUserId(String mealId, String userId);

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
    MealNutritionalInformationDTO sumNutritionalInformationByMealIdAndUserId(
            @Param("mealId") String mealId,
            @Param("userId") String userId
    );
}
