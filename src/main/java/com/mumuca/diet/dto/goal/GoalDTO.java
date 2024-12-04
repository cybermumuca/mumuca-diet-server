package com.mumuca.diet.dto.goal;

import com.mumuca.diet.dto.MacronutrientDTO;
import com.mumuca.diet.model.GoalType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalDTO(
        String id,
        GoalType goalType,
        int targetCalories,
        MacronutrientDTO macronutrientsTarget,
        BigDecimal targetWeight,
        BigDecimal waterIntakeTarget,
        LocalDate deadline
) {}
