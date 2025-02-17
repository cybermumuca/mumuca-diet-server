package com.mumuca.diet.meal.service;

import com.mumuca.diet.food.dto.FoodDTO;
import com.mumuca.diet.meal.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealLogService {
    MealLogDTO createMealLog(CreateMealLogDTO createMealLogDTO, String userId);
    void deleteMealLog(String mealLogId, String userId);
    MealLogDTO getMealLog(String mealLogId, String userId);
    List<MealLogWithCaloriesConsumedDTO> findMealLogsByDate(LocalDate date, String userId);
    List<FoodDTO> getMealLogFoods(String mealLogId, String userId);
    List<MealWithFoodsDTO> getMealLogMeals(String mealLogId, String userId);
    Optional<MealNutritionalInformationDTO> getMealLogNutritionalInformation(String mealLogId, String userId);
    void addFoodsToMealLog(String mealLogId, AddFoodsToMealLogDTO addFoodsToMealLogDTO, String userId);
    void addMealsToMealLog(String mealLogId, AddMealsToMealLogDTO addMealsToMealLogDTO, String userId);
    void updateMealLog(String mealLogId, UpdateMealLogDTO updateMealLogDTO, String userId);
    void removeMealLogFoods(String mealLogId, DeleteMealLogFoodsDTO deleteMealLogFoodsDTO, String userId);
    void removeMealLogMeals(String mealLogId, DeleteMealLogMealsDTO deleteMealLogMealsDTO, String userId);
}
