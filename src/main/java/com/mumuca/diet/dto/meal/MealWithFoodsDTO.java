package com.mumuca.diet.dto.meal;

import com.mumuca.diet.food.dto.FoodDTO;
import com.mumuca.diet.model.MealType;

import java.util.List;

public record MealWithFoodsDTO(
        String id,
        String title,
        String description,
        MealType type,
        List<FoodDTO> foods
) {}
