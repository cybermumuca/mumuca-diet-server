package com.mumuca.diet.dto;

import com.mumuca.diet.model.ActivityLevel;
import com.mumuca.diet.model.Gender;

public record ProfileDTO(
        String id,
        String firstName,
        String lastName,
        Gender gender,
        String photoUrl,
        ActivityLevel activityLevel,
        int age
) {}
