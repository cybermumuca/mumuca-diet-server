package com.mumuca.diet.dto.food;

public record FoodDTO(
        String id,
        String title,
        String brand,
        String description,
        NutritionalInformationDTO nutritionalInformation
) {}
