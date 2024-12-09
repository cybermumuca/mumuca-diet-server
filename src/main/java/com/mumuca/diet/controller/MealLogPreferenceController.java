package com.mumuca.diet.controller;

import com.mumuca.diet.dto.meallogpreferences.CreateMealLogPreferenceDTO;
import com.mumuca.diet.dto.meallogpreferences.MealLogPreferenceDTO;
import com.mumuca.diet.dto.meallogpreferences.UpdateMealLogPreferenceDTO;
import com.mumuca.diet.service.MealLogPreferenceService;
import com.mumuca.diet.validator.ValidUUID;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class MealLogPreferenceController {

    private final MealLogPreferenceService mealLogPreferenceService;

    @GetMapping(path = "/v1/meal-log-preferences")
    public ResponseEntity<List<MealLogPreferenceDTO>> getMealLogPreferences(
            @AuthenticationPrincipal Jwt jwt
    ) {
        List<MealLogPreferenceDTO> mealLogPreferenceDTOList = mealLogPreferenceService
                .getUserMealLogPreferences(jwt.getSubject());

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
        List<MealLogPreferenceDTO> mealLogPreferenceDTOList = mealLogPreferenceService
                .createMealLogPreference(createMealLogPreferenceDTOList, jwt.getSubject());

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
        MealLogPreferenceDTO mealLogPreferenceDTO = mealLogPreferenceService
                .updateMealLogPreference(
                        mealLogPreferenceId,
                        updateMealLogPreferenceDTO,
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
        mealLogPreferenceService
                .deleteMealLogPreference(mealLogPreferenceId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
