package com.mumuca.diet.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record HeightRegistryDTO(
        @NotNull(message = "The height record cannot be null.")
        @Positive(message = "The height record must be a positive value.")
        @DecimalMin(value = "0.5", message = "The height must be at least 0.5 meters.")
        @DecimalMax(value = "3.0", message = "The height must be at most 3.0 meters.")
        Double registry,

        @NotNull(message = "Date and time cannot be null.")
        @PastOrPresent(message = "The date and time must be in the past or present.")
        LocalDateTime dateTime
) {}
