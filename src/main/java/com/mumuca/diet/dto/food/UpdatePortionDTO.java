package com.mumuca.diet.dto.food;

import com.mumuca.diet.model.Unit;

public record UpdatePortionDTO(
        Integer amount,
        Unit unit,
        String description
) {}
