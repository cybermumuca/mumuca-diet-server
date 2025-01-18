package com.mumuca.diet.meal.dto;

import com.mumuca.diet.meal.model.MealType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalTime;

public record UpdateMealLogDTO(
        MealType type,
        LocalTime time,
        @Min(value = 1, message = "The calorie goal should be at least 1")
        @Max(value = 10000, message = "Calorie goal cannot be greater than 10000")
        Integer caloriesGoal
) {}
