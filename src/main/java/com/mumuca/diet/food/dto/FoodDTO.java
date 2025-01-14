package com.mumuca.diet.food.dto;

public record FoodDTO(
        String id,
        String title,
        String brand,
        String description,
        PortionDTO portion,
        NutritionalInformationDTO nutritionalInformation
) {}
