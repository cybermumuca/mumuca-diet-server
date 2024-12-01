package com.mumuca.diet.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record WeightUpdateDTO(
        @Positive(message = "The weight record must be a positive value.")
        Double registry,

        @PastOrPresent(message = "The date and time must be in the past or present.")
        LocalDateTime dateTime
) {}

