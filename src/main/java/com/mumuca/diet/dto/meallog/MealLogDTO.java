package com.mumuca.diet.dto.meallog;

import com.mumuca.diet.model.Food;
import com.mumuca.diet.model.Meal;
import com.mumuca.diet.model.MealType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record MealLogDTO(
        String id,
        MealType type,
        LocalDate date,
        LocalTime time,
        Integer caloriesGoal,
        List<Meal> meals,
        List<Food> foods
) {}
