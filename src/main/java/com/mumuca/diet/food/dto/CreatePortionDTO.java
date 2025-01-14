package com.mumuca.diet.food.dto;

import com.mumuca.diet.food.model.Unit;
import jakarta.validation.constraints.NotNull;

public record CreatePortionDTO(
        @NotNull(message = "Amount cannot be null.")
        Integer amount,
        @NotNull(message = "Unit cannot be null.")
        Unit unit,
        String description
) {}
