package com.mumuca.diet.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO<T>(
        String apiPath,
        int statusCode,
        String errorMessage,
        T details,
        LocalDateTime errorTime
) {}
