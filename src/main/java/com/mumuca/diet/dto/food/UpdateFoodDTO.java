package com.mumuca.diet.dto.food;

public record UpdateFoodDTO(
        String title,
        String brand,
        String description,
        UpdatePortionDTO portion,
        UpdateNutritionalInformationDTO nutritionalInformation
) {}
