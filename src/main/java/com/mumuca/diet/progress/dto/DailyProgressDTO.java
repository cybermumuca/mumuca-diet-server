package com.mumuca.diet.progress.dto;

import com.mumuca.diet.calculator.dto.MacronutrientDTO;

public record DailyProgressDTO(
        MacronutrientDTO macronutrientsTarget,
        MacronutrientDTO macronutrientsConsumed,
        Integer targetCalories,
        Integer caloriesConsumed,
        float waterIntakeTarget,
        float waterIngested,
        Integer timeUntilDeadline
) {}
