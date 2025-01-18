package com.mumuca.diet.meal.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AddFoodsToMealLogDTO(
        @NotNull(message = "foodIds cannot be null.")
        @NotEmpty(message = "foodIds cannot be empty.")
        Set<String> foodIds
) {}
