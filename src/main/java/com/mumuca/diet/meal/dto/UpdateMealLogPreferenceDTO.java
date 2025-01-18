package com.mumuca.diet.meal.dto;

import com.mumuca.diet.meal.model.MealType;

import java.time.LocalTime;

public record UpdateMealLogPreferenceDTO(
        MealType type,
        LocalTime time,
        Integer caloriesGoal
) {}
