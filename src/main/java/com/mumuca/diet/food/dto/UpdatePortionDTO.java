package com.mumuca.diet.food.dto;

import com.mumuca.diet.food.model.Unit;

public record UpdatePortionDTO(
        Integer amount,
        Unit unit,
        String description
) {}
