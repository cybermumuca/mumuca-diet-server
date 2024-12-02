package com.mumuca.diet.dto.meal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AddFoodsToMealDTO(
        @NotEmpty(message = "Food IDs cannot be null or empty")
        List<@NotBlank(message = "Food ID cannot be null or blank") String> foodIds
) {}
