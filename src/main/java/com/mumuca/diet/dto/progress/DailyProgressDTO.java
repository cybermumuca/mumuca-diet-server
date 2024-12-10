package com.mumuca.diet.dto.progress;

import com.mumuca.diet.dto.MacronutrientDTO;

public record DailyProgressDTO(
        MacronutrientDTO macronutrientsTarget,
        MacronutrientDTO macronutrientsConsumed,
        Integer targetCalories,
        Integer caloriesConsumed,
        float waterIntakeTarget,
        float waterIngested,
        Integer timeUntilDeadline
) {}
