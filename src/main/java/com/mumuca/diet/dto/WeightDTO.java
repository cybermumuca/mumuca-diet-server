package com.mumuca.diet.dto;

import java.time.LocalDateTime;

public record WeightDTO(
        String id,
        double registry,
        LocalDateTime dateTime
) {}
