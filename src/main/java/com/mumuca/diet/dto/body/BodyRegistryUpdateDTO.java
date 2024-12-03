package com.mumuca.diet.dto.body;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BodyRegistryUpdateDTO(
        @Positive(message = "The weight must be a positive value.")
        BigDecimal weight,

        @Positive(message = "The height must be a positive value.")
        @DecimalMin(value = "0.5", message = "The height must be at least 0.5 meters.")
        @DecimalMax(value = "3.0", message = "The height must be at most 3.0 meters.")
        BigDecimal height,

        @PastOrPresent(message = "The date must be in the past or present.")
        LocalDate date
) {}

