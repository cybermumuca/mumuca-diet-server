package com.mumuca.diet.dto.drinklog;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateDrinkLogDTO(
        @NotNull(message = "Date cannot be null.")
        LocalDate date,
        @NotNull(message = "Time cannot be null.")
        LocalTime time,
        @NotNull(message = "Liquid intake cannot be null.")
        @DecimalMin(value = "0.1", message = "Liquid intake should be at least 0.1 liters")
        @DecimalMax(value = "10", message = "Liquid intake cannot be greater than 10 liters")
        BigDecimal liquidIntake
) {
}
