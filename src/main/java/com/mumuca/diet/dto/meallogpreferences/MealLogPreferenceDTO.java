package com.mumuca.diet.dto.meallogpreferences;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mumuca.diet.model.MealType;

import java.time.LocalTime;

public record MealLogPreferenceDTO(
        String id,
        MealType type,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime time,
        Integer caloriesGoal
) {}
