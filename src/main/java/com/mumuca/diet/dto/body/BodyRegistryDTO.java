package com.mumuca.diet.dto.body;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BodyRegistryDTO(
        @NotNull(message = "The weight cannot be null.")
        @Positive(message = "The weight must be a positive value.")
        BigDecimal weight,

        @NotNull(message = "The height cannot be null.")
        @Positive(message = "The height must be a positive value.")
        @DecimalMin(value = "0.5", message = "The height must be at least 0.5 meters.")
        @DecimalMax(value = "3.0", message = "The height must be at most 3.0 meters.")
        BigDecimal height,

        @NotNull(message = "Date cannot be null.")
        @PastOrPresent(message = "The date must be in the past or present.")
        LocalDate date
) {}
