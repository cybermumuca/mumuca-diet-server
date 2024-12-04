package com.mumuca.diet.controller;

import com.mumuca.diet.dto.goal.GoalDTO;
import com.mumuca.diet.dto.goal.UpdateMacronutrientGoalDTO;
import com.mumuca.diet.dto.goal.UpdateWaterIntakeGoalDTO;
import com.mumuca.diet.service.GoalService;
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
public class GoalController {

    private final GoalService goalService;

    @GetMapping(path = "/v1/goals")
    public ResponseEntity<GoalDTO> getUserGoal(
            @AuthenticationPrincipal Jwt jwt
    ) {
        GoalDTO goalDTO = goalService.getUserGoal(jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(goalDTO);
    }

    @PatchMapping(path = "/v1/goals/macronutrients")
    public ResponseEntity<GoalDTO> updateUserMacronutrientGoal(
            @Valid @RequestBody UpdateMacronutrientGoalDTO updateMacronutrientGoalDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        GoalDTO goalDTO = goalService.updateUserMacronutrientGoal(
                updateMacronutrientGoalDTO,
                jwt.getSubject()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(goalDTO);
    }

    @PatchMapping(path = "/v1/goals/water-intake")
    public ResponseEntity<GoalDTO> updateUserWaterIntakeGoal(
            @Valid @RequestBody UpdateWaterIntakeGoalDTO updateWaterIntakeGoalDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        GoalDTO goalDTO = goalService.updateUserWaterIntakeGoal(
                updateWaterIntakeGoalDTO,
                jwt.getSubject()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(goalDTO);
    }
}
