package com.mumuca.diet.food.dto;

import com.mumuca.diet.food.model.Unit;

public record PortionDTO(
        String id,
        Integer amount,
        Unit unit,
        String description
) {}
