package com.mumuca.diet.service;

import com.mumuca.diet.dto.meal.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MealService {
    MealDTO createMeal(CreateMealDTO createMealDTO, String userId);
    MealWithFoodsDTO getMeal(String mealId, String userId);
    Optional<MealNutritionalInformationDTO> getMealNutritionalInformation(String mealId, String userId);
    MealDTO updateMeal(String mealId, UpdateMealDTO updateMealDTO, String userId);
    void deleteMeal(String mealId, String userId);
    void removeFoodsFromMeal(String mealId, List<String> foodIds, String userId);
    void addFoodsToMeal(String mealId, List<String> foodIds, String userId);

    Page<MealDTO> getMeals(Pageable pageable, String userId);
}
