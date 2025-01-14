package com.mumuca.diet.food.service;

import com.mumuca.diet.food.dto.CreateFoodDTO;
import com.mumuca.diet.food.dto.FoodDTO;
import com.mumuca.diet.food.dto.NutritionalInformationDTO;
import com.mumuca.diet.food.dto.UpdateFoodDTO;
import com.mumuca.diet.dto.meal.MealDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodService {
    FoodDTO createFood(CreateFoodDTO createFoodDTO, String userId);
    FoodDTO getFood(String foodId, String userId);
    NutritionalInformationDTO getFoodNutritionalInformation(String foodId, String userId);
    FoodDTO updateFood(String foodId, UpdateFoodDTO updateFoodDTO, String userId);
    void deleteFood(String foodId, String userId);
    List<MealDTO> getFoodMeals(String foodId, String userId);
    Page<FoodDTO> getFoods(Pageable pageable, String userId);
}
