package com.mumuca.diet.food.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record UpdateNutritionalInformationDTO(
        @DecimalMin(value = "0.0", inclusive = false, message = "Calories must be greater than 0.")
        BigDecimal calories,

        @DecimalMin(value = "0.0", inclusive = false, message = "Carbohydrates must be greater than 0.")
        BigDecimal carbohydrates,

        @DecimalMin(value = "0.0", inclusive = false, message = "Protein must be greater than 0.")
        BigDecimal protein,

        @DecimalMin(value = "0.0", inclusive = false, message = "Fat must be greater than 0.")
        BigDecimal fat,

        @DecimalMin(value = "0.0", inclusive = false, message = "Monounsaturated fat must be greater than 0.")
        BigDecimal monounsaturatedFat,

        @DecimalMin(value = "0.0", inclusive = false, message = "Saturated fat must be greater than 0.")
        BigDecimal saturatedFat,

        @DecimalMin(value = "0.0", inclusive = false, message = "Polyunsaturated fat must be greater than 0.")
        BigDecimal polyunsaturatedFat,

        @DecimalMin(value = "0.0", inclusive = false, message = "Trans fat must be greater than 0.")
        BigDecimal transFat,

        @DecimalMin(value = "0.0", inclusive = false, message = "Cholesterol must be greater than 0.")
        BigDecimal cholesterol,

        @DecimalMin(value = "0.0", inclusive = false, message = "Sodium must be greater than 0.")
        BigDecimal sodium,

        @DecimalMin(value = "0.0", inclusive = false, message = "Potassium must be greater than 0.")
        BigDecimal potassium,

        @DecimalMin(value = "0.0", inclusive = false, message = "Fiber must be greater than 0.")
        BigDecimal fiber,

        @DecimalMin(value = "0.0", inclusive = false, message = "Sugar must be greater than 0.")
        BigDecimal sugar,

        @DecimalMin(value = "0.0", inclusive = false, message = "Calcium must be greater than 0.")
        BigDecimal calcium,

        @DecimalMin(value = "0.0", inclusive = false, message = "Iron must be greater than 0.")
        BigDecimal iron,

        @DecimalMin(value = "0.0", inclusive = false, message = "Vitamin A must be greater than 0.")
        BigDecimal vitaminA,

        @DecimalMin(value = "0.0", inclusive = false, message = "Vitamin C must be greater than 0.")
        BigDecimal vitaminC
) {}
