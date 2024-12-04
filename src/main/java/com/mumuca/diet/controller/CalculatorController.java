package com.mumuca.diet.controller;

import com.mumuca.diet.dto.BmiDTO;
import com.mumuca.diet.dto.MacronutrientDTO;
import com.mumuca.diet.model.ActivityLevel;
import com.mumuca.diet.model.Gender;
import com.mumuca.diet.model.GoalType;
import com.mumuca.diet.util.NutritionalCalculator;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class CalculatorController {

    @GetMapping(path = "/v1/calculator/bmi")
    public ResponseEntity<BmiDTO> calculateBMI(
            @RequestParam("weight")
            @NotNull(message = "The weight cannot be null.")
            @Positive(message = "The weight must be a positive value.")
            BigDecimal weight,

            @RequestParam("height")
            @NotNull(message = "The height cannot be null.")
            @Positive(message = "The height must be a positive value.")
            BigDecimal height
    ) {
        var bmiValue = NutritionalCalculator.calculateBMI(weight, height);
        var bmiClassification = NutritionalCalculator.classifyBMI(bmiValue);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BmiDTO(bmiValue.floatValue(), bmiClassification));
    }

    @GetMapping(path = "/v1/calculator/bmi/value")
    public ResponseEntity<Map<String, Float>> calculateBMIValue(
            @RequestParam("weight")
            @NotNull(message = "The weight cannot be null.")
            @Positive(message = "The weight must be a positive value.")
            BigDecimal weight,

            @RequestParam("height")
            @NotNull(message = "The height cannot be null.")
            @Positive(message = "The height must be a positive value.")
            BigDecimal height
    ) {
        var bmiValue = NutritionalCalculator.calculateBMI(weight, height);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("bmi", bmiValue.floatValue()));
    }

    @GetMapping(path = "/v1/calculator/bmi/classification")
    public ResponseEntity<Map<String, String>> calculateBMIClassification(
            @RequestParam("bmi") BigDecimal bmi
    ) {
        String classification = NutritionalCalculator.classifyBMI(bmi);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        Map.of("classification", classification)
                );
    }

    @GetMapping(path = "/v1/calculator/ideal-weight")
    public ResponseEntity<Map<String, Float>> calculateIdealWeight(
            @RequestParam("height")
            @NotNull(message = "The height cannot be null.")
            @Positive(message = "The height must be a positive value.")
            BigDecimal height
    ) {
        var idealWeight = NutritionalCalculator.calculateIdealWeight(height);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        Map.of(
                                "idealMinimumWeight", idealWeight.getFirst().floatValue(),
                                "idealMaximumWeight", idealWeight.getLast().floatValue()
                        )
                );
    }

    @GetMapping(path = "/v1/calculator/bmr")
    public ResponseEntity<Map<String, Integer>> calculateBMR(
            @RequestParam("weight")
            @NotNull(message = "The weight cannot be null.")
            @Positive(message = "The weight must be a positive value.")
            BigDecimal weight,

            @RequestParam("height")
            @NotNull(message = "The height cannot be null.")
            @Positive(message = "The height must be a positive value.")
            BigDecimal height,

            @RequestParam("age")
            @NotNull(message = "The age cannot be null.")
            int age,

            @RequestParam("gender")
            @NotNull(message = "The gender cannot be null.")
            Gender gender,

            @RequestParam("activityLevel")
            @NotNull(message = "The activity level cannot be null.")
            ActivityLevel activity
    ) {
        BigDecimal bmr = NutritionalCalculator.calculateBMR(weight, height, age, gender);
        BigDecimal bmrWithActivity = NutritionalCalculator.adjustCaloriesForActivity(bmr, activity);
        BigDecimal caloriesToLoseWeight = NutritionalCalculator.adjustCaloriesForGoal(bmrWithActivity, GoalType.LOSE_WEIGHT);
        BigDecimal caloriesToGainWeight = NutritionalCalculator.adjustCaloriesForGoal(bmrWithActivity, GoalType.GAIN_WEIGHT);
        BigDecimal caloriesToMantainWeight = NutritionalCalculator.adjustCaloriesForGoal(bmrWithActivity, GoalType.MAINTAIN_WEIGHT);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        Map.of(
                                "bmr", bmr.intValue(),
                                "bmrWithActivity", bmrWithActivity.intValue(),
                                "caloriesToLoseWeight", caloriesToLoseWeight.intValue(),
                                "caloriesToGainWeight", caloriesToGainWeight.intValue(),
                                "caloriesToMantainWeight", caloriesToMantainWeight.intValue()
                        )
                );
    }

    @GetMapping(path = "/v1/calculator/body-fat")
    public ResponseEntity<Map<String, Float>> calculateBodyFat(
            @RequestParam("weight")
            @NotNull(message = "The weight cannot be null.")
            @Positive(message = "The weight must be a positive value.")
            BigDecimal weight,

            @RequestParam("height")
            @NotNull(message = "The height cannot be null.")
            @Positive(message = "The height must be a positive value.")
            BigDecimal height,

            @RequestParam("age")
            @NotNull(message = "The age cannot be null.")
            int age,

            @RequestParam("gender")
            @NotNull(message = "The gender cannot be null.")
            Gender gender
    ) {
        BigDecimal bodyFat = NutritionalCalculator.calculateBodyFat(weight, height, age, gender);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("bodyFat", bodyFat.floatValue()));
    }

    @GetMapping(path = "/v1/calculator/macronutrient")
    public ResponseEntity<MacronutrientDTO> calculateMacronutrients(
            @RequestParam("targetCalories")
            @NotNull(message = "Target calories cannot be null.")
            @Positive(message = "The target calories must be a positive value.")
            BigDecimal targetCalories,

            @RequestParam("proteinPercentage")
            @NotNull(message = "Protein percentage cannot be null.")
            @Positive(message = "The protein percentage must be a positive value.")
            BigDecimal proteinPercentage,

            @RequestParam("carbsPercentage")
            @NotNull(message = "Carbohydrates percentage cannot be null.")
            @Positive(message = "The carbohydrates percentage must be a positive value.")
            BigDecimal carbsPercentage,

            @RequestParam("fatPercentage")
            @NotNull(message = "Fat percentage cannot be null.")
            @Positive(message = "The fat percentage must be a positive value.")
            BigDecimal fatPercentage
    ) {
        var macronutrientDTO = NutritionalCalculator.calculateMacronutrients(
                targetCalories,
                proteinPercentage,
                carbsPercentage,
                fatPercentage
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(macronutrientDTO);
    }

    @GetMapping(path = "/v1/calculator/water-intake")
    public ResponseEntity<Map<String, Float>> calculateWaterIntake(
            @RequestParam("weight")
            @NotNull(message = "The weight cannot be null.")
            @Positive(message = "The weight must be a positive value.")
            BigDecimal weight
    ) {
        var waterIntake = NutritionalCalculator.calculateWaterIntake(weight);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("waterIntake", waterIntake.floatValue()));
    }
}
