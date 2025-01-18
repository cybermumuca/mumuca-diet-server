package com.mumuca.diet.meal.dto;

import com.mumuca.diet.food.dto.FoodDTO;
import com.mumuca.diet.meal.model.MealType;

import java.util.List;

public record MealWithFoodsDTO(
        String id,
        String title,
        String description,
        MealType type,
        List<FoodDTO> foods
) {}
