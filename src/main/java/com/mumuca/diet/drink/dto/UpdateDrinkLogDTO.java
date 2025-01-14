package com.mumuca.diet.drink.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateDrinkLogDTO(
        LocalTime time,
        LocalDate date,
        @DecimalMin(value = "0.1", message = "Liquid intake should be at least 0.1 liters")
        @DecimalMax(value = "10", message = "Liquid intake cannot be greater than 10 liters")
        BigDecimal liquidIntake
) {}
