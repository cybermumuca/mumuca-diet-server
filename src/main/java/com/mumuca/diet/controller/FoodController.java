package com.mumuca.diet.controller;


import com.mumuca.diet.dto.food.CreateFoodDTO;
import com.mumuca.diet.dto.food.FoodDTO;
import com.mumuca.diet.dto.food.NutritionalInformationDTO;
import com.mumuca.diet.dto.food.UpdateFoodDTO;
import com.mumuca.diet.dto.meal.MealDTO;
import com.mumuca.diet.service.FoodService;
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
public class FoodController {

    private final FoodService foodService;

    @PostMapping(path = "/v1/foods")
    public ResponseEntity<FoodDTO> createFood(
            @Valid @RequestBody CreateFoodDTO createFoodDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        FoodDTO foodDTO = foodService.createFood(createFoodDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(foodDTO);
    }

    @GetMapping(path = "/v1/foods/{id}")
    public ResponseEntity<FoodDTO> getFood(
            @PathVariable("id") String foodId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        FoodDTO foodDTO = foodService.getFood(foodId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foodDTO);
    }

    @GetMapping(path = "/v1/foods/{id}/nutritional-info")
    public ResponseEntity<NutritionalInformationDTO> getFoodNutritionalInformation(
            @PathVariable("id") String foodId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        NutritionalInformationDTO nutritionalInformationDTO = foodService.getFoodNutritionalInformation(foodId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(nutritionalInformationDTO);
    }

    @GetMapping(path = "/v1/foods/{id}/meals")
    public ResponseEntity<List<MealDTO>> getFoodMeals(
            @PathVariable("id") String foodId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        List<MealDTO> mealDTOList = foodService.getFoodMeals(foodId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mealDTOList);
    }

    @PutMapping(path = "/v1/foods/{id}")
    public ResponseEntity<FoodDTO> updateFood(
            @PathVariable("id") String foodId,
            @Valid @RequestBody UpdateFoodDTO updateFoodDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        FoodDTO foodDTO = foodService.updateFood(foodId, updateFoodDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foodDTO);
    }

    @DeleteMapping(path = "/v1/foods/{id}")
    public ResponseEntity<Void> deleteFood(
            @PathVariable("id") String foodId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        foodService.deleteFood(foodId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
