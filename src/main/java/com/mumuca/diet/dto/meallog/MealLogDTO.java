package com.mumuca.diet.dto.meallog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mumuca.diet.model.MealType;

import java.time.LocalDate;
import java.time.LocalTime;

public record MealLogDTO(
        String id,
        MealType type,
        LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm")
        LocalTime time,
        Integer caloriesGoal
) {}
