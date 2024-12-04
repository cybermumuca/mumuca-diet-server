package com.mumuca.diet.dto.goal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateWaterIntakeGoalDTO(
        @NotNull(message = "The water intake goal cannot be null.")
        @Positive(message = "The water intake goal must be a positive value.")
        @DecimalMin(value = "0.5", message = "The water intake goal must be at least 0.5 liters.")
        BigDecimal waterIntakeGoal
) {}
