package com.mumuca.diet.controller;

import com.mumuca.diet.dto.meal.*;
import com.mumuca.diet.service.MealService;
import com.mumuca.diet.validator.ValidUUID;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
@Slf4j
public class MealController {

    private final MealService mealService;

    @PostMapping(path = "/v1/meals")
    public ResponseEntity<MealDTO> createMeal(
            @Valid @RequestBody CreateMealDTO createMealDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is creating a meal with payload [{}]", jwt.getSubject(), createMealDTO);

        MealDTO mealDTO = mealService.createMeal(createMealDTO, jwt.getSubject());

        log.info("Meal created successfully. Meal id: [{}], User: [{}]", mealDTO.id(), jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mealDTO);
    }

    @GetMapping(path = "/v1/meals/{id}")
    public ResponseEntity<MealWithFoodsDTO> getMeal(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting details of meal with id [{}]", jwt.getSubject(), mealId);

        MealWithFoodsDTO mealDTO = mealService.getMeal(mealId, jwt.getSubject());

        log.info("Meal details returned for user [{}]. Meal id: [{}]", jwt.getSubject(), mealId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealDTO);
    }

    @GetMapping(path = "/v1/meals/{id}/nutritional-information")
    public ResponseEntity<MealNutritionalInformationDTO> getMealNutritionalInformation(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting nutritional information of meal [{}]", jwt.getSubject(), mealId);

        Optional<MealNutritionalInformationDTO> nutritionalInformationDTO =  mealService.getMealNutritionalInformation(mealId, jwt.getSubject());

        if (nutritionalInformationDTO.isEmpty()) {
            log.info(
                    "Meal [{}] nutritional information returned empty to the user [{}]",
                    mealId,
                    jwt.getSubject()
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        log.info("Meal nutritional information returned for user [{}]. Meal id: [{}]", jwt.getSubject(), mealId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(nutritionalInformationDTO.get());
    }

    @PutMapping(path = "/v1/meals/{id}")
    public ResponseEntity<MealDTO> updateMeal(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @Valid @RequestBody UpdateMealDTO updateMealDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is updating meal [{}] with payload [{}]", jwt.getSubject(), mealId, updateMealDTO);

        mealService.updateMeal(mealId, updateMealDTO, jwt.getSubject());

        log.info("Meal updated successfully. Meal id: [{}], User: [{}]", mealId, jwt.getSubject());

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
        log.info("User [{}] is adding foods with payload [{}] to meal [{}]", jwt.getSubject(), addFoodsToMealDTO, mealId);

        mealService.addFoodsToMeal(mealId, addFoodsToMealDTO.foodIds(), jwt.getSubject());

        log.info("Foods added to meal successfully. Meal id: [{}], User: [{}]", mealId, jwt.getSubject());

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
        log.info("User [{}] is removing foods with payload [{}] from meal [{}]", jwt.getSubject(), removeFoodsFromMealDTO, mealId);

        mealService.removeFoodsFromMeal(mealId, removeFoodsFromMealDTO.foodIds(), jwt.getSubject());

        log.info("Foods removed successfully. Meal id: [{}], User: [{}]", mealId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/meals/{id}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable("id") @Valid @ValidUUID String mealId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is deleting food [{}]", jwt.getSubject(), mealId);

        mealService.deleteMeal(mealId, jwt.getSubject());

        log.info("Meal deleted successfully. Meal id: [{}], User: [{}]", mealId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
