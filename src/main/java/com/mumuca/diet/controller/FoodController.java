package com.mumuca.diet.controller;


import com.mumuca.diet.dto.food.CreateFoodDTO;
import com.mumuca.diet.dto.food.FoodDTO;
import com.mumuca.diet.dto.food.NutritionalInformationDTO;
import com.mumuca.diet.dto.food.UpdateFoodDTO;
import com.mumuca.diet.dto.meal.MealDTO;
import com.mumuca.diet.service.FoodService;
import com.mumuca.diet.validator.ValidUUID;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
public class FoodController {

    private final FoodService foodService;

    @PostMapping(path = "/v1/foods")
    public ResponseEntity<FoodDTO> createFood(
            @Valid @RequestBody CreateFoodDTO createFoodDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is creating a food with payload [{}]", jwt.getSubject(), createFoodDTO);

        FoodDTO foodDTO = foodService.createFood(createFoodDTO, jwt.getSubject());

        log.info("Food created successfully. Food Id: [{}], User: [{}]", foodDTO.id(), jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(foodDTO);
    }

    @GetMapping(path = "/v1/foods")
    public ResponseEntity<Page<FoodDTO>> getFoods(
            @PageableDefault(sort = "title", size = 20) Pageable pageable,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting a paginated list of foods. Pageable: [{}]", jwt.getSubject(), pageable);

        int maxPageSize = 100;

        if (pageable.getPageSize() > maxPageSize) {
            pageable = PageRequest.of(pageable.getPageNumber(), maxPageSize, pageable.getSort());
            log.warn("Page size too large. Limiting to [{}] items per page.", maxPageSize);
        }

        Page<FoodDTO> foodPage = foodService.getFoods(pageable, jwt.getSubject());

        log.info(
                "Paginated list of foods returned for user [{}]. Total elements: [{}], Total pages: [{}]",
                jwt.getSubject(),
                foodPage.getTotalElements(),
                foodPage.getTotalPages()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foodPage);
    }

    @GetMapping(path = "/v1/foods/{id}")
    public ResponseEntity<FoodDTO> getFood(
            @PathVariable("id") @Valid @ValidUUID String foodId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting details of food with id [{}]", jwt.getSubject(), foodId);

        FoodDTO foodDTO = foodService.getFood(foodId, jwt.getSubject());

        log.info("Food details returned for user [{}]. Food id: [{}]", jwt.getSubject(), foodId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foodDTO);
    }

    @GetMapping(path = "/v1/foods/{id}/nutritional-info")
    public ResponseEntity<NutritionalInformationDTO> getFoodNutritionalInformation(
            @PathVariable("id") @Valid @ValidUUID String foodId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting nutritional information of food with ID [{}]", jwt.getSubject(), foodId);

        NutritionalInformationDTO nutritionalInformationDTO = foodService.getFoodNutritionalInformation(foodId, jwt.getSubject());

        log.info("Food nutritional information returned for user [{}]. Food ID: [{}]", jwt.getSubject(), foodId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(nutritionalInformationDTO);
    }

    @GetMapping(path = "/v1/foods/{id}/meals")
    public ResponseEntity<List<MealDTO>> getFoodMeals(
            @PathVariable("id") @Valid @ValidUUID String foodId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting meals that contain the food with id [{}]", jwt.getSubject(), foodId);

        List<MealDTO> mealDTOList = foodService.getFoodMeals(foodId, jwt.getSubject());

        log.info("Meals containing the food [{}] returned to the user [{}]", foodId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealDTOList);
    }

    @PutMapping(path = "/v1/foods/{id}")
    public ResponseEntity<FoodDTO> updateFood(
            @PathVariable("id") @Valid @ValidUUID String foodId,
            @Valid @RequestBody UpdateFoodDTO updateFoodDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is updating food [{}] with payload [{}]", jwt.getSubject(), foodId, updateFoodDTO);

        FoodDTO foodDTO = foodService.updateFood(foodId, updateFoodDTO, jwt.getSubject());

        log.info("Food updated sucessfully. Food id: [{}], User: [{}]", foodId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foodDTO);
    }

    @DeleteMapping(path = "/v1/foods/{id}")
    public ResponseEntity<Void> deleteFood(
            @PathVariable("id") @Valid @ValidUUID String foodId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is deleting food [{}]", jwt.getSubject(), foodId);

        foodService.deleteFood(foodId, jwt.getSubject());

        log.info("Food deleted sucessfully. Food id: [{}], User: [{}]", foodId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
