package com.mumuca.diet.body.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BodyDTO(
        String id,
        BigDecimal weight,
        BigDecimal height,
        LocalDate date
) {}
