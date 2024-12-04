package com.mumuca.diet.dto.goal;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateMacronutrientGoalDTO(
        @Positive(message = "The protein target must be a positive value.")
        BigDecimal proteinTarget,
        @Positive(message = "The carbs target must be a positive value.")
        BigDecimal carbsTarget,
        @Positive(message = "The fat target must be a positive value.")
        BigDecimal fatTarget
) {}
