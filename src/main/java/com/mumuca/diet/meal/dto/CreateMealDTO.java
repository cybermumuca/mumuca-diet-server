package com.mumuca.diet.meal.dto;

import com.mumuca.diet.meal.model.MealType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateMealDTO(
        @NotBlank(message = "Title cannot be null or blank")
        String title,
        String description,
        @NotNull(message = "Meal type cannot be null")
        MealType type,
        List<@NotBlank(message = "Food ID cannot be null or blank") String> foodIds
) {}
