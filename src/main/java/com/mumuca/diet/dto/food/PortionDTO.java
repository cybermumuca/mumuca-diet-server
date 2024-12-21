package com.mumuca.diet.dto.food;

import com.mumuca.diet.model.Unit;

public record PortionDTO(
        String id,
        Integer amount,
        Unit unit,
        String description
) {}
