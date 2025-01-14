package com.mumuca.diet.food.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFoodDTO(
        @NotBlank(message = "Title cannot be null or blank")
        String title,
        String brand,
        String description,
        CreatePortionDTO portion,
        @NotNull(message = "nutritionalInformation cannot be null")
        CreateNutritionalInformationDTO nutritionalInformation
) {}
