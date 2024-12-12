package com.mumuca.diet.controller;

import com.mumuca.diet.dto.progress.DailyProgressDTO;
import com.mumuca.diet.service.ProgressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
@Slf4j
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping(path = "/v1/progress/daily")
    public ResponseEntity<DailyProgressDTO> getDailyProgress(
            @RequestParam(
                    value = "date",
                    required = false,
                    defaultValue = "#{T(java.time.LocalDate).now()}"
            )
            LocalDate date,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is getting daily progress", jwt.getSubject());

        DailyProgressDTO dailyProgressDTO = progressService.getDailyProgress(date, jwt.getSubject());

        log.info("Daily progress fetched successfully to user [{}]", jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dailyProgressDTO);
    }

}
