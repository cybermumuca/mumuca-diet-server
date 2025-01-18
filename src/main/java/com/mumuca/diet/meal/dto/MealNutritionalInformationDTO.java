package com.mumuca.diet.meal.dto;

import java.math.BigDecimal;

public record MealNutritionalInformationDTO(
        BigDecimal calories,
        BigDecimal carbohydrates,
        BigDecimal protein,
        BigDecimal fat,
        BigDecimal monounsaturatedFat,
        BigDecimal saturatedFat,
        BigDecimal polyunsaturatedFat,
        BigDecimal transFat,
        BigDecimal cholesterol,
        BigDecimal sodium,
        BigDecimal potassium,
        BigDecimal fiber,
        BigDecimal sugar,
        BigDecimal calcium,
        BigDecimal iron,
        BigDecimal vitaminA,
        BigDecimal vitaminC
) {}
