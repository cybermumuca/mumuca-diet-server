package com.mumuca.diet.dto.meal;

import com.mumuca.diet.model.MealType;

public record UpdateMealDTO(
        String title,
        String description,
        MealType type
) {}
