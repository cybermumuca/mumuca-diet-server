package com.mumuca.diet.dto.food;

import com.mumuca.diet.model.Unit;
import jakarta.validation.constraints.NotNull;

public record CreatePortionDTO(
        @NotNull(message = "Amount cannot be null.")
        Integer amount,
        @NotNull(message = "Unit cannot be null.")
        Unit unit,
        String description
) {}