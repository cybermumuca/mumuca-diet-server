package com.mumuca.diet.meal.dto;

import com.mumuca.diet.meal.model.MealType;

public record UpdateMealDTO(
        String title,
        String description,
        MealType type
) {}
