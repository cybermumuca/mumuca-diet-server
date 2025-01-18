package com.mumuca.diet.meal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mumuca.diet.meal.model.MealType;

import java.time.LocalTime;

public record MealLogPreferenceDTO(
        String id,
        MealType type,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime time,
        Integer caloriesGoal
) {}
