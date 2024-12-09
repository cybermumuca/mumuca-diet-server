package com.mumuca.diet.dto.meallogpreferences;

import com.mumuca.diet.model.MealType;

import java.time.LocalTime;

public record MealLogPreferenceDTO(
        String id,
        MealType type,
        LocalTime time,
        Integer caloriesGoal
) {}
