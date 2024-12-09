package com.mumuca.diet.dto.meallog;

import com.mumuca.diet.model.MealType;

import java.time.LocalDate;
import java.time.LocalTime;

public record MealLogWithCaloriesConsumedDTO(
        String id,
        MealType type,
        LocalDate date,
        LocalTime time,
        Integer caloriesGoal,
        Integer caloriesConsumed
) {}
