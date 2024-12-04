package com.mumuca.diet.dto;

public record DiagnosisDTO(
        BmiDTO bmi,
        float idealMinimumWeight,
        float idealMaximumWeight,
        String fatRate
) {}
