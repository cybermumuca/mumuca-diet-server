package com.mumuca.diet.dto;

import lombok.Builder;

@Builder
public record MacronutrientDTO(
        float protein,
        float carbs,
        float fat
) {}
