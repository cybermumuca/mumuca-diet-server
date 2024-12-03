package com.mumuca.diet.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponseDTO(
        String apiPath,
        int statusCode,
        String errorMessage,
        LocalDateTime errorTime
) {}
