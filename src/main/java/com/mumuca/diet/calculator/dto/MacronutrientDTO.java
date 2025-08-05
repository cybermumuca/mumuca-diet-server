package com.mumuca.diet.calculator.dto;

import lombok.Builder;

@Builder
public record MacronutrientDTO(
        float protein,
        float carbs,
        float fat
) {}
