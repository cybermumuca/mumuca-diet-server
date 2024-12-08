package com.mumuca.diet.controller;

import com.mumuca.diet.dto.meal.*;
import com.mumuca.diet.service.MealService;
import com.mumuca.diet.validator.ValidUUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class MealController {

    private final MealService mealService;

    @PostMapping(path = "/v1/meals")
    public ResponseEntity<MealDTO> createMeal(
            @Valid @RequestBody CreateMealDTO createMealDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        MealDTO mealDTO = mealService.createMeal(createMealDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mealDTO);
    }

    @GetMapping(path = "/v1/meals/{id}")
    public ResponseEntity<MealWithFoodsDTO> getMeal(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        MealWithFoodsDTO mealDTO = mealService.getMeal(mealId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealDTO);
    }

    @GetMapping(path = "/v1/meals/{id}/nutritional-information")
    public ResponseEntity<MealNutritionalInformationDTO> getMealNutritionalInformation(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return mealService.getMealNutritionalInformation(mealId, jwt.getSubject())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping(path = "/v1/meals/{id}")
    public ResponseEntity<MealDTO> updateMeal(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @Valid @RequestBody UpdateMealDTO updateMealDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealService.updateMeal(mealId, updateMealDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PatchMapping(path = "/v1/meals/{id}/foods")
    public ResponseEntity<Void> addFoodsToMeal(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @Valid @RequestBody AddFoodsToMealDTO addFoodsToMealDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealService.addFoodsToMeal(mealId, addFoodsToMealDTO.foodIds(), jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/meals/{id}/foods")
    public ResponseEntity<Void> removeFoodsFromMeal(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @Valid @RequestBody RemoveFoodsFromMealDTO removeFoodsFromMealDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealService.removeFoodsFromMeal(mealId, removeFoodsFromMealDTO.foodIds(), jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/meals/{id}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealService.deleteMeal(mealId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
