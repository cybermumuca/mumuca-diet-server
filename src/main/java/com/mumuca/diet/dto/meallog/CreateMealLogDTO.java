package com.mumuca.diet.dto.meallog;

import com.mumuca.diet.model.MealType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateMealLogDTO(
        @NotNull(message = "Meal type cannot be null.")
        MealType type,

        @NotNull(message = "Date cannot be null.")
        LocalDate date,

        @NotNull(message = "Time cannot be null.")
        LocalTime time,

        @NotNull(message = "Calorie goal cannot be null.")
        @Min(value = 1, message = "The calorie goal should be at least 1")
        @Max(value = 10000, message = "Calorie goal cannot be greater than 10000")
        Integer caloriesGoal
) {}
