package com.mumuca.diet.meal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mumuca.diet.meal.model.MealType;

import java.time.LocalDate;
import java.time.LocalTime;

public record MealLogDTO(
        String id,
        MealType type,
        LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime time,
        Integer caloriesGoal
) {}
