package com.mumuca.diet.controller;

import com.mumuca.diet.dto.food.FoodDTO;
import com.mumuca.diet.dto.meal.MealNutritionalInformationDTO;
import com.mumuca.diet.dto.meal.MealWithFoodsDTO;
import com.mumuca.diet.dto.meallog.*;
import com.mumuca.diet.service.MealLogService;
import com.mumuca.diet.validator.ValidUUID;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class MealLogController {

    private final MealLogService mealLogService;

    @GetMapping(path = "/v1/meal-logs/{id}")
    public ResponseEntity<MealLogDTO> getMealLog(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        MealLogDTO mealLogDTO = mealLogService.getMealLog(mealLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealLogDTO);
    }

    @GetMapping(path = "/v1/meal-logs")
    public ResponseEntity<List<MealLogDTO>> getMealLogsByDate(
            @RequestParam(
                    value = "date",
                    required = false,
                    defaultValue = "#{T(java.time.LocalDate).now()}"
            )
            LocalDate date,
            @AuthenticationPrincipal Jwt jwt
    ) {
        List<MealLogDTO> mealLogDTOList = mealLogService
                .findMealLogsByDate(date, jwt.getSubject());

        if (mealLogDTOList.isEmpty()) {
            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealLogDTOList);
    }


    @GetMapping(path = "/v1/meal-logs/{id}/foods")
    public ResponseEntity<List<FoodDTO>> getMealLogFoods(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        List<FoodDTO> foodDTOList = mealLogService.getMealLogFoods(mealLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foodDTOList);
    }

    @GetMapping(path = "/v1/meal-logs/{id}/meals")
    public ResponseEntity<List<MealWithFoodsDTO>> getMealLogMeals(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        List<MealWithFoodsDTO> mealWithFoodsDTOList = mealLogService.getMealLogMeals(mealLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealWithFoodsDTOList);
    }

    @GetMapping(path = "/v1/meal-logs/{id}/nutritional-info")
    public ResponseEntity<MealNutritionalInformationDTO> getMealLogNutritionalInformation(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        MealNutritionalInformationDTO mealNutritionalInformationDTO = mealLogService.getMealLogNutritionalInformation(mealLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealNutritionalInformationDTO);
    }


    @PostMapping(path = "/v1/meal-logs")
    public ResponseEntity<MealLogDTO> createMealLog(
            @Valid @RequestBody CreateMealLogDTO createMealLogDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        MealLogDTO mealLogDTO = mealLogService.createMealLog(createMealLogDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mealLogDTO);
    }

    @PostMapping(path = "/v1/meal-logs/{id}/foods")
    public ResponseEntity<Void> addFoodsToMealLog(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @Valid @RequestBody AddFoodsToMealLogDTO addFoodsToMealLogDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealLogService.addFoodsToMealLog(mealLogId, addFoodsToMealLogDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(path = "/v1/meal-logs/{id}/meals")
    public ResponseEntity<Void> addMealsToMealLog(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @Valid @RequestBody AddMealsToMealLogDTO addMealsToMealLogDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealLogService.addMealsToMealLog(mealLogId, addMealsToMealLogDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @PutMapping(path = "/v1/meal-logs/{id}")
    public ResponseEntity<Void> updateMealLog(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @Valid @RequestBody UpdateMealLogDTO updateMealLogDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealLogService.updateMealLog(mealLogId, updateMealLogDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/meal-logs/{id}")
    public ResponseEntity<Void> deleteMealLog(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealLogService.deleteMealLog(mealLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/meal-logs/{id}/foods")
    public ResponseEntity<Void> deleteMealLogFoods(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @Valid @RequestBody DeleteMealLogFoodsDTO deleteMealLogFoodsDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealLogService.removeMealLogFoods(mealLogId, deleteMealLogFoodsDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/meal-logs/{id}/meals")
    public ResponseEntity<Void> deleteMealLogMeals(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @Valid @RequestBody DeleteMealLogMealsDTO deleteMealLogMealsDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        mealLogService.removeMealLogMeals(mealLogId, deleteMealLogMealsDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
