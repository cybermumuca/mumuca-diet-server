package com.mumuca.diet.dto;

import com.mumuca.diet.model.ActivityLevel;
import com.mumuca.diet.model.Gender;
import com.mumuca.diet.model.GoalType;

import java.time.LocalDate;

public record RegistrationCompletedDTO(
        float weight,
        float height,
        int age,
        Gender gender,
        GoalType goalType,
        ActivityLevel activityLevel,
        float targetWeight,
        int targetCalories,
        float proteinTarget,
        float carbsTarget,
        float fatTarget,
        float waterIntakeTarget,
        LocalDate deadline
) {}
