package com.mumuca.diet.controller;

import com.mumuca.diet.dto.meallog.CreateMealLogDTO;
import com.mumuca.diet.dto.meallog.MealLogDTO;
import com.mumuca.diet.dto.meallog.UpdateMealLogDTO;
import com.mumuca.diet.service.MealLogService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class MealLogController {

    private final MealLogService mealLogService;

    @PostMapping(path = "/meal-logs")
    public ResponseEntity<MealLogDTO> createMealLog(
            @Valid @RequestBody CreateMealLogDTO createMealLogDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        MealLogDTO mealLogDTO = mealLogService.createMealLog(createMealLogDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mealLogDTO);
    }

    @GetMapping(path = "/meal-logs/{id}")
    public ResponseEntity<MealLogDTO> getMealLog(
            @PathVariable("id") String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        MealLogDTO mealLogDTO = mealLogService.getMealLog(mealLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealLogDTO);
    }

    @PatchMapping(path = "/meal-logs/{id}")
    public ResponseEntity<MealLogDTO> updateMealLog(
            @PathVariable("id") String mealLogId,
            @Valid @RequestBody UpdateMealLogDTO updateMealLogDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        MealLogDTO mealLogDTO = mealLogService.updateMealLog(mealLogId, updateMealLogDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealLogDTO);
    }

    @DeleteMapping(path = "/meal-logs/{id}")
    public ResponseEntity<Void> deleteMealLog(
            @PathVariable("id") String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealLogService.deleteMealLog(mealLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
