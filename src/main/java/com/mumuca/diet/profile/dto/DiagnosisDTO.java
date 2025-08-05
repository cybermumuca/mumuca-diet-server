package com.mumuca.diet.profile.dto;

import com.mumuca.diet.calculator.dto.BmiDTO;

public record DiagnosisDTO(
        BmiDTO bmi,
        float idealMinimumWeight,
        float idealMaximumWeight,
        String fatRate
) {}
