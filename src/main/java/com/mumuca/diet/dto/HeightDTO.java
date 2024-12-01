package com.mumuca.diet.dto;

import java.time.LocalDateTime;

public record HeightDTO(
        String id,
        double registry,
        LocalDateTime dateTime
) {}
