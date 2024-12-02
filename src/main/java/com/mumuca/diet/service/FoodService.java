package com.mumuca.diet.service;

import com.mumuca.diet.dto.food.CreateFoodDTO;
import com.mumuca.diet.dto.food.FoodDTO;
import com.mumuca.diet.dto.food.NutritionalInformationDTO;
import com.mumuca.diet.dto.food.UpdateFoodDTO;
import com.mumuca.diet.dto.meal.MealDTO;

import java.util.List;

public interface FoodService {
    FoodDTO createFood(CreateFoodDTO createFoodDTO, String userId);
    FoodDTO getFood(String foodId, String userId);
    NutritionalInformationDTO getFoodNutritionalInformation(String foodId, String userId);
    FoodDTO updateFood(String foodId, UpdateFoodDTO updateFoodDTO, String userId);
    void deleteFood(String foodId, String userId);
    List<MealDTO> getFoodMeals(String foodId, String userId);
}
