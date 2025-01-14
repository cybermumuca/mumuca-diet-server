package com.mumuca.diet.food.dto;

public record UpdateFoodDTO(
        String title,
        String brand,
        String description,
        UpdatePortionDTO portion,
        UpdateNutritionalInformationDTO nutritionalInformation
) {}
