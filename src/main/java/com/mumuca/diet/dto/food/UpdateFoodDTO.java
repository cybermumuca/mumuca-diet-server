package com.mumuca.diet.dto.food;

public record UpdateFoodDTO(
        String title,
        String brand,
        String description,
        UpdateNutritionalInformationDTO nutritionalInformation
) {}
