package com.mumuca.diet.dto.meallog;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record DeleteMealLogMealsDTO(
        @NotNull(message = "mealIds cannot be null.")
        @NotEmpty(message = "mealIds cannot be empty.")
        Set<String> mealIds
) {
}
