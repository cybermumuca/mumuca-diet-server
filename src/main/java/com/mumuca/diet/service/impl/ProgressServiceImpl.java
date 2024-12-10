package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.MacronutrientDTO;
import com.mumuca.diet.dto.meal.MealNutritionalInformationDTO;
import com.mumuca.diet.dto.progress.DailyProgressDTO;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.exception.UserNotRegisteredYetException;
import com.mumuca.diet.model.Goal;
import com.mumuca.diet.repository.GoalRepository;
import com.mumuca.diet.repository.MealLogRepository;
import com.mumuca.diet.service.ProgressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final GoalRepository goalRepository;
    private final MealLogRepository mealLogRepository;

    // TODO: Test this
    // TODO: When any field in Food or Meal NutritionalInformation is null it throws NullPointerException, fix this
    @Override
    public DailyProgressDTO getDailyProgress(LocalDate date, String userId) {
        // TODO: this query is fetching user too
        Goal userGoal = goalRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotRegisteredYetException("Goal not found."));

        MacronutrientDTO macronutrientsTarget = MacronutrientDTO.builder()
                .protein(userGoal.getProteinTarget().floatValue())
                .fat(userGoal.getFatTarget().floatValue())
                .carbs(userGoal.getCarbsTarget().floatValue())
                .build();

        MealNutritionalInformationDTO nutrientsConsumed = mealLogRepository.findByDateAndUserId(date, userId)
                .stream()
                .map(mealLog -> {
                    try (var virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
                        var foodsFuture = CompletableFuture.supplyAsync(() ->
                                        mealLogRepository.sumFoodsNutritionalInformationByMealLogIdAndUserId(mealLog.getId(), userId)
                                                .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found.")),
                                virtualThreadExecutor
                        );

                        var mealsFuture = CompletableFuture.supplyAsync(() ->
                                        mealLogRepository.sumMealsNutritionalInformationByMealLogIdAndUserId(mealLog.getId(), userId)
                                                .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found.")),
                                virtualThreadExecutor
                        );

                        var foodsNutritionalInformation = foodsFuture.join();
                        var mealsNutritionalInformation = mealsFuture.join();

                        return new MealNutritionalInformationDTO(
                                foodsNutritionalInformation.calories().add(mealsNutritionalInformation.calories()),
                                foodsNutritionalInformation.carbohydrates().add(mealsNutritionalInformation.carbohydrates()),
                                foodsNutritionalInformation.protein().add(mealsNutritionalInformation.protein()),
                                foodsNutritionalInformation.fat().add(mealsNutritionalInformation.fat()),
                                foodsNutritionalInformation.monounsaturatedFat().add(mealsNutritionalInformation.monounsaturatedFat()),
                                foodsNutritionalInformation.saturatedFat().add(mealsNutritionalInformation.saturatedFat()),
                                foodsNutritionalInformation.polyunsaturatedFat().add(mealsNutritionalInformation.polyunsaturatedFat()),
                                foodsNutritionalInformation.transFat().add(mealsNutritionalInformation.transFat()),
                                foodsNutritionalInformation.cholesterol().add(mealsNutritionalInformation.cholesterol()),
                                foodsNutritionalInformation.sodium().add(mealsNutritionalInformation.sodium()),
                                foodsNutritionalInformation.potassium().add(mealsNutritionalInformation.potassium()),
                                foodsNutritionalInformation.fiber().add(mealsNutritionalInformation.fiber()),
                                foodsNutritionalInformation.sugar().add(mealsNutritionalInformation.sugar()),
                                foodsNutritionalInformation.calcium().add(mealsNutritionalInformation.calcium()),
                                foodsNutritionalInformation.iron().add(mealsNutritionalInformation.iron()),
                                foodsNutritionalInformation.vitaminA().add(mealsNutritionalInformation.vitaminA()),
                                foodsNutritionalInformation.vitaminC().add(mealsNutritionalInformation.vitaminC())
                        );
                    } catch (CompletionException e) {
                        Throwable cause = e.getCause();

                        if (cause instanceof ResourceNotFoundException) {
                            throw (ResourceNotFoundException) cause;
                        }

                        throw new RuntimeException("Unexpected error occurred while getting daily progress.", e);
                    }
                })
                .reduce((dto1, dto2) -> {
                    return new MealNutritionalInformationDTO(
                            dto1.calories().add(dto2.calories()),
                            dto1.carbohydrates().add(dto2.carbohydrates()),
                            dto1.protein().add(dto2.protein()),
                            dto1.fat().add(dto2.fat()),
                            dto1.monounsaturatedFat().add(dto2.monounsaturatedFat()),
                            dto1.saturatedFat().add(dto2.saturatedFat()),
                            dto1.polyunsaturatedFat().add(dto2.polyunsaturatedFat()),
                            dto1.transFat().add(dto2.transFat()),
                            dto1.cholesterol().add(dto2.cholesterol()),
                            dto1.sodium().add(dto2.sodium()),
                            dto1.potassium().add(dto2.potassium()),
                            dto1.fiber().add(dto2.fiber()),
                            dto1.sugar().add(dto2.sugar()),
                            dto1.calcium().add(dto2.calcium()),
                            dto1.iron().add(dto2.iron()),
                            dto1.vitaminA().add(dto2.vitaminA()),
                            dto1.vitaminC().add(dto2.vitaminC())
                    );
                })
                .orElse(new MealNutritionalInformationDTO(
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO
                        )
                );

        float proteinsConsumed = nutrientsConsumed.protein().floatValue();
        float carbsConsumed = nutrientsConsumed.carbohydrates().floatValue();

        // TODO: check this statement
        //  "my grandfather: Do not include trans fat in the calculation, it is not absorbed by the body."
        float totalFatConsumed = nutrientsConsumed.fat()
                .add(nutrientsConsumed.polyunsaturatedFat())
                .add(nutrientsConsumed.saturatedFat())
                .add(nutrientsConsumed.monounsaturatedFat())
                .floatValue();

        MacronutrientDTO macronutrientsConsumed = MacronutrientDTO.builder()
                .protein(proteinsConsumed)
                .carbs(carbsConsumed)
                .fat(totalFatConsumed)
                .build();

        return new DailyProgressDTO(
                macronutrientsTarget,
                macronutrientsConsumed,
                userGoal.getTargetCalories(),
                nutrientsConsumed.calories().intValue(),
                userGoal.getWaterIntakeTarget().floatValue(),
                0, // TODO: add water ingested feature
                0 // TODO: maybe it's irrelevant
        );
    }
}
