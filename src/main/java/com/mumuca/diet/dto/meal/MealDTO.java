package com.mumuca.diet.dto.meal;

import com.mumuca.diet.model.MealType;

public record MealDTO(
        String id,
        String title,
        String description,
        MealType type
) {}
