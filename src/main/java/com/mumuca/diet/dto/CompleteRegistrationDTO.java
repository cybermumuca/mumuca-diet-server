package com.mumuca.diet.dto;

import com.mumuca.diet.model.ActivityLevel;
import com.mumuca.diet.model.Gender;
import com.mumuca.diet.model.GoalType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CompleteRegistrationDTO(
        @NotNull(message = "The weight cannot be null.")
        @Positive(message = "The weight must be a positive value.")
        BigDecimal weight,
        @NotNull(message = "The height cannot be null.")
        @Positive(message = "The height must be a positive value.")
        @DecimalMin(value = "0.5", message = "The height must be at least 0.5 meters.")
        @DecimalMax(value = "3.0", message = "The height must be at most 3.0 meters.")
        BigDecimal height,
        @NotNull(message = "The gender cannot be null.")
        Gender gender,
        @NotNull(message = "The birth date cannot be null")
        @Past(message = "The birth date must be in the past to be valid.")
        LocalDate birthDate,
        @NotNull(message = "The goal cannot be null")
        GoalType goal,
        @NotNull(message = "The target weight cannot be null.")
        @Positive(message = "The target weight must be a positive value.")
        BigDecimal targetWeight,
        @NotNull(message = "The activity level cannot be null.")
        ActivityLevel activityLevel
) {}
