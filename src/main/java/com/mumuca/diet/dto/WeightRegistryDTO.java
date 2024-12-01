package com.mumuca.diet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record WeightRegistryDTO(
        @NotNull(message = "The weight record cannot be null.")
        @Positive(message = "The weight record must be a positive value.")
        Double registry,

        @NotNull(message = "Date and time cannot be null.")
        @PastOrPresent(message = "The date and time must be in the past or present.")
        LocalDateTime dateTime
) {}
