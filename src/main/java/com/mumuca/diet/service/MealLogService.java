package com.mumuca.diet.service;

import com.mumuca.diet.dto.food.FoodDTO;
import com.mumuca.diet.dto.meal.MealDTO;
import com.mumuca.diet.dto.meal.MealNutritionalInformationDTO;
import com.mumuca.diet.dto.meal.MealWithFoodsDTO;
import com.mumuca.diet.dto.meallog.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public interface MealLogService {
    MealLogDTO createMealLog(CreateMealLogDTO createMealLogDTO, String userId);
    void deleteMealLog(String mealLogId, String userId);
    MealLogDTO getMealLog(String mealLogId, String userId);
    List<MealLogDTO> findOrCreateMealLogsByDate(LocalDate date, String userId);
    List<FoodDTO> getMealLogFoods(String mealLogId, String userId);
    List<MealWithFoodsDTO> getMealLogMeals(String mealLogId, String userId);
    MealNutritionalInformationDTO getMealLogNutritionalInformation(String mealLogId, String userId);
    void addFoodsToMealLog(String mealLogId, AddFoodsToMealLogDTO addFoodsToMealLogDTO, String userId);
    void addMealsToMealLog(String mealLogId, AddMealsToMealLogDTO addMealsToMealLogDTO, String userId);
    void updateMealLog(String mealLogId, UpdateMealLogDTO updateMealLogDTO, String userId);
    void removeMealLogFoods(String mealLogId, DeleteMealLogFoodsDTO deleteMealLogFoodsDTO, String userId);
    void removeMealLogMeals(String mealLogId, DeleteMealLogMealsDTO deleteMealLogMealsDTO, String userId);
}
