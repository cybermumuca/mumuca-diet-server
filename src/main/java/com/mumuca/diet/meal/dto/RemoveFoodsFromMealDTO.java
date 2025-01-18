package com.mumuca.diet.meal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RemoveFoodsFromMealDTO(
        @NotEmpty(message = "Food IDs cannot be null or empty")
        List<@NotBlank(message = "Food ID cannot be null or blank") String> foodIds
) {}
