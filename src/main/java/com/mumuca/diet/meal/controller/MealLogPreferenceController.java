package com.mumuca.diet.meal.controller;

import com.mumuca.diet.meal.dto.CreateMealLogPreferenceDTO;
import com.mumuca.diet.meal.dto.MealLogPreferenceDTO;
import com.mumuca.diet.meal.dto.UpdateMealLogPreferenceDTO;
import com.mumuca.diet.meal.service.MealLogPreferenceService;
import com.mumuca.diet.validator.ValidUUID;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
@Slf4j
public class MealLogPreferenceController {

    private final MealLogPreferenceService mealLogPreferenceService;

    @GetMapping(path = "/v1/meal-log-preferences")
    public ResponseEntity<List<MealLogPreferenceDTO>> getMealLogPreferences(
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting meal log preferences", jwt.getSubject());

        List<MealLogPreferenceDTO> mealLogPreferenceDTOList = mealLogPreferenceService
                .getUserMealLogPreferences(jwt.getSubject());

        log.info("Meal Log Preferences returned to user [{}]", jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealLogPreferenceDTOList);
    }

    @PostMapping(path = "/v1/meal-log-preferences")
    public ResponseEntity<List<MealLogPreferenceDTO>> createMealLogPreferences(
            // TODO: Nest this object with a "data" field that will be of type List<T>
            @RequestBody @Valid List<CreateMealLogPreferenceDTO> createMealLogPreferenceDTOList,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info(
                "User [{}] is creating meal log preferences with payload: [{}]",
                jwt.getSubject(),
                createMealLogPreferenceDTOList
        );

        List<MealLogPreferenceDTO> mealLogPreferenceDTOList = mealLogPreferenceService
                .createMealLogPreference(createMealLogPreferenceDTOList, jwt.getSubject());

        log.info(
                "Meal log preferences created successfully to user: [{}]",
                jwt.getSubject()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mealLogPreferenceDTOList);
    }

    @PatchMapping(path = "/v1/meal-log-preferences/{id}")
    public ResponseEntity<MealLogPreferenceDTO> updateMealLogPreference(
            @PathVariable("id") @Valid @ValidUUID String mealLogPreferenceId,
            @RequestBody @Valid UpdateMealLogPreferenceDTO updateMealLogPreferenceDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info(
                "User [{}] is updating Meal Log Preference [{}] with payload [{}]",
                jwt.getSubject(),
                mealLogPreferenceId,
                updateMealLogPreferenceDTO
        );

        MealLogPreferenceDTO mealLogPreferenceDTO = mealLogPreferenceService
                .updateMealLogPreference(
                        mealLogPreferenceId,
                        updateMealLogPreferenceDTO,
                        jwt.getSubject()
                );

        log.info(
                "Meal Log Preference updated successfully. Meal Log Preference id: [{}], User: [{}]",
                mealLogPreferenceId,
                jwt.getSubject()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealLogPreferenceDTO);
    }


    @DeleteMapping(path = "/v1/meal-log-preferences/{id}")
    public ResponseEntity<MealLogPreferenceDTO> deleteMealLogPreference(
            @PathVariable("id") @Valid @ValidUUID String mealLogPreferenceId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is deleting Meal Log Preference [{}]", jwt.getSubject(), mealLogPreferenceId);

        mealLogPreferenceService
                .deleteMealLogPreference(mealLogPreferenceId, jwt.getSubject());

        log.info(
                "Meal Log Preference deleted successfully. Meal Log Preference id: [{}], User: [{}]",
                mealLogPreferenceId,
                jwt.getSubject()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
