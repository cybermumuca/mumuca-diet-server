package com.mumuca.diet.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String apiPath,
        int statusCode,
        String errorMessage,
        LocalDateTime errorTime
) {}
