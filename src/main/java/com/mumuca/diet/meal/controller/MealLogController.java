package com.mumuca.diet.meal.controller;

import com.mumuca.diet.food.dto.FoodDTO;
import com.mumuca.diet.meal.dto.*;
import com.mumuca.diet.meal.service.MealLogService;
import com.mumuca.diet.validator.ValidUUID;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
@Slf4j
public class MealLogController {

    private final MealLogService mealLogService;

    @GetMapping(path = "/v1/meal-logs/{id}")
    public ResponseEntity<MealLogDTO> getMealLog(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting meal log [{}]", jwt.getSubject(), mealLogId);

        MealLogDTO mealLogDTO = mealLogService.getMealLog(mealLogId, jwt.getSubject());

        log.info("Meal log returned for user [{}]. Meal log id: [{}]", jwt.getSubject(), mealLogId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealLogDTO);
    }


    @GetMapping(path = "/v1/meal-logs")
    public ResponseEntity<List<MealLogWithCaloriesConsumedDTO>> getMealLogsByDate(
            @RequestParam(
                    value = "date",
                    required = false,
                    defaultValue = "#{T(java.time.LocalDate).now()}"
            )
            LocalDate date,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting meal logs on date [{}]", jwt.getSubject(), date);

        List<MealLogWithCaloriesConsumedDTO> mealLogWithCaloriesConsumedDTOList =
                mealLogService.findMealLogsByDate(date, jwt.getSubject());

        if (mealLogWithCaloriesConsumedDTOList.isEmpty()) {
            log.info("No meal logs found for user [{}] on date [{}]", jwt.getSubject(), date);
            return ResponseEntity
                    .noContent()
                    .build();
        }

        log.info("Meal logs found for user [{}] on date [{}]", jwt.getSubject(), date);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealLogWithCaloriesConsumedDTOList);
    }

    @GetMapping(path = "/v1/meal-logs/{id}/foods")
    public ResponseEntity<List<FoodDTO>> getMealLogFoods(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting foods from meal log [{}]", jwt.getSubject(), mealLogId);

        List<FoodDTO> foodDTOList = mealLogService.getMealLogFoods(mealLogId, jwt.getSubject());

        if (foodDTOList.isEmpty()) {
            log.info("No foods found for meal log [{}] for user [{}]", mealLogId, jwt.getSubject());

            return ResponseEntity
                    .noContent()
                    .build();
        }

        log.info("Meal log foods returned for user [{}]. Meal log id: [{}]", jwt.getSubject(), mealLogId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foodDTOList);
    }

    @GetMapping(path = "/v1/meal-logs/{id}/meals")
    public ResponseEntity<List<MealWithFoodsDTO>> getMealLogMeals(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting meals from meal log [{}]", jwt.getSubject(), mealLogId);

        List<MealWithFoodsDTO> mealWithFoodsDTOList = mealLogService.getMealLogMeals(mealLogId, jwt.getSubject());

        if (mealWithFoodsDTOList.isEmpty()) {
            log.info("No meals found for meal log [{}] for user [{}]", mealLogId, jwt.getSubject());

            return ResponseEntity
                    .noContent()
                    .build();
        }

        log.info("Meal log meals returned for user [{}]. Meal log id: [{}]", jwt.getSubject(), mealLogId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealWithFoodsDTOList);
    }

    @GetMapping(path = "/v1/meal-logs/{id}/nutritional-info")
    public ResponseEntity<MealNutritionalInformationDTO> getMealLogNutritionalInformation(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting nutritional information of meal log [{}]", jwt.getSubject(), mealLogId);

        Optional<MealNutritionalInformationDTO> mealNutritionalInformationDTO = mealLogService
                .getMealLogNutritionalInformation(mealLogId, jwt.getSubject());

        if (mealNutritionalInformationDTO.isEmpty()) {
           log.info("There is no nutritional information available for meal log [{}]. User: [{}]", mealLogId, jwt.getSubject());

           return ResponseEntity.noContent().build();
        }

        log.info("Meal log nutritional information returned for user [{}]. Meal Log id: [{}]", jwt.getSubject(), mealLogId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealNutritionalInformationDTO.get());
    }


    @PostMapping(path = "/v1/meal-logs")
    public ResponseEntity<MealLogDTO> createMealLog(
            @Valid @RequestBody CreateMealLogDTO createMealLogDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is creating a meal log with payload [{}]", jwt.getSubject(), createMealLogDTO);

        MealLogDTO mealLogDTO = mealLogService.createMealLog(createMealLogDTO, jwt.getSubject());

        log.info("Meal log created successfully. Meal Log Id: [{}], User: [{}]", mealLogDTO.id(), jwt.getSubject());

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
        log.info(
                "User [{}] is adding foods with payload [{}] to meal log [{}]",
                jwt.getSubject(),
                addFoodsToMealLogDTO,
                mealLogId
        );

        mealLogService.addFoodsToMealLog(mealLogId, addFoodsToMealLogDTO, jwt.getSubject());

        log.info("Foods added to meal log successfully. Meal log id: [{}], User: [{}]", mealLogId, jwt.getSubject());

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
        log.info(
                "User [{}] is adding meals with payload [{}] to meal log [{}]",
                jwt.getSubject(),
                addMealsToMealLogDTO,
                mealLogId
        );

        mealLogService.addMealsToMealLog(mealLogId, addMealsToMealLogDTO, jwt.getSubject());

        log.info("Meals added to meal log successfully. Meal log id: [{}], User: [{}]", mealLogId, jwt.getSubject());

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
        log.info("User [{}] is updating meal log [{}] with payload [{}]", jwt.getSubject(), mealLogId, updateMealLogDTO);

        mealLogService.updateMealLog(mealLogId, updateMealLogDTO, jwt.getSubject());

        log.info("Meal log updated successfully. Meal log id: [{}], User: [{}]", mealLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/meal-logs/{id}")
    public ResponseEntity<Void> deleteMealLog(
            @PathVariable("id") @Valid @ValidUUID String mealLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is deleting meal log [{}]", jwt.getSubject(), mealLogId);

        mealLogService.deleteMealLog(mealLogId, jwt.getSubject());

        log.info("Meal log deleted successfully. Meal log id: [{}], User: [{}]", mealLogId, jwt.getSubject());

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
        log.info("User [{}] is removing foods with payload [{}] from meal log [{}]", jwt.getSubject(), deleteMealLogFoodsDTO, mealLogId);

        mealLogService.removeMealLogFoods(mealLogId, deleteMealLogFoodsDTO, jwt.getSubject());

        log.info("Foods removed from Meal log successfully. Meal log id: [{}], User: [{}]", mealLogId, jwt.getSubject());

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
        log.info("User [{}] is removing meals with payload [{}] from meal log [{}]", jwt.getSubject(), deleteMealLogMealsDTO, mealLogId);

        mealLogService.removeMealLogMeals(mealLogId, deleteMealLogMealsDTO, jwt.getSubject());

        log.info("Meals removed from Meal log successfully. Meal log id: [{}], User: [{}]", mealLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
