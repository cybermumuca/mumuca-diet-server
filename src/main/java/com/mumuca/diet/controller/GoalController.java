package com.mumuca.diet.controller;

import com.mumuca.diet.dto.goal.GoalDTO;
import com.mumuca.diet.dto.goal.UpdateMacronutrientGoalDTO;
import com.mumuca.diet.dto.goal.UpdateWaterIntakeGoalDTO;
import com.mumuca.diet.service.GoalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
@Slf4j
public class GoalController {

    private final GoalService goalService;

    @GetMapping(path = "/v1/goals")
    public ResponseEntity<GoalDTO> getUserGoal(
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting your goal", jwt.getSubject());

        GoalDTO goalDTO = goalService.getUserGoal(jwt.getSubject());

        log.info("Goal returned for user [{}]. Goal id: [{}]", jwt.getSubject(), goalDTO.id());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(goalDTO);
    }

    @PatchMapping(path = "/v1/goals/macronutrients")
    public ResponseEntity<GoalDTO> updateUserMacronutrientGoal(
            @Valid @RequestBody UpdateMacronutrientGoalDTO updateMacronutrientGoalDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is updating macronutrients goal with payload [{}]", jwt.getSubject(), updateMacronutrientGoalDTO);

        GoalDTO goalDTO = goalService.updateUserMacronutrientGoal(
                updateMacronutrientGoalDTO,
                jwt.getSubject()
        );

        log.info("Macronutrients Goal updated successfully. Goal id: [{}], User: [{}]", goalDTO.id(), jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(goalDTO);
    }

    @PatchMapping(path = "/v1/goals/water-intake")
    public ResponseEntity<GoalDTO> updateUserWaterIntakeGoal(
            @Valid @RequestBody UpdateWaterIntakeGoalDTO updateWaterIntakeGoalDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is updating water intake goal with payload [{}]", jwt.getSubject(), updateWaterIntakeGoalDTO);

        GoalDTO goalDTO = goalService.updateUserWaterIntakeGoal(
                updateWaterIntakeGoalDTO,
                jwt.getSubject()
        );

        log.info("Water intake Goal updated successfully. Goal id: [{}], User: [{}]", goalDTO.id(), jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(goalDTO);
    }
}
