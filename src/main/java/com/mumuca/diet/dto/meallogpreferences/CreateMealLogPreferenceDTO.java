package com.mumuca.diet.dto.meallogpreferences;

import com.mumuca.diet.model.MealType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record CreateMealLogPreferenceDTO(
        @NotNull(message = "The type cannot be null")
        MealType type,
        @NotNull(message = "The time cannot be null")
        LocalTime time
) {}
