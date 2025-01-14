package com.mumuca.diet.food.dto;

import com.mumuca.diet.model.Unit;

public record PortionDTO(
        String id,
        Integer amount,
        Unit unit,
        String description
) {}
