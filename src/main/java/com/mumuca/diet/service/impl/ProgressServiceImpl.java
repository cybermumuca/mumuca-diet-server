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

    private MealNutritionalInformationDTO getDefaultNutritionalInformation() {
        return new MealNutritionalInformationDTO(
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    private BigDecimal safeAdd(BigDecimal value1, BigDecimal value2) {
        return (value1 == null ? BigDecimal.ZERO : value1)
                .add(value2 == null ? BigDecimal.ZERO : value2);
    }

    // TODO: Test this
    @Override
    public DailyProgressDTO getDailyProgress(LocalDate date, String userId) {
        Goal userGoal = goalRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotRegisteredYetException("Goal not found."));

        MacronutrientDTO macronutrientsTarget = MacronutrientDTO.builder()
                .protein(userGoal.getProteinTarget().floatValue())
                .fat(userGoal.getFatTarget().floatValue())
                .carbs(userGoal.getCarbsTarget().floatValue())
                .build();

        MealNutritionalInformationDTO nutrientsConsumed = mealLogRepository
                .findByDateAndUserId(date, userId)
                .stream()
                .map(mealLog -> {
                    try (var virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
                        var mealLogId = mealLog.getId();

                        var mealLogHasFoodsFuture = CompletableFuture.supplyAsync(
                                () -> mealLogRepository.existsFoodsByIdAndUserId(mealLogId, userId),
                                virtualThreadExecutor
                        );

                        var mealLogHasMealsFuture = CompletableFuture.supplyAsync(
                                () -> mealLogRepository.existsMealsByIdAndUserId(mealLogId, userId),
                                virtualThreadExecutor
                        );

                        CompletableFuture<MealNutritionalInformationDTO> foodsFuture = mealLogHasFoodsFuture
                                .thenCompose(hasFoods -> {
                                    if (!hasFoods) {
                                        return CompletableFuture.completedFuture(getDefaultNutritionalInformation());
                                    }

                                    return CompletableFuture.supplyAsync(() -> {
                                        return mealLogRepository
                                                .sumFoodsNutritionalInformationByMealLogIdAndUserId(mealLogId, userId)
                                                .orElseGet(this::getDefaultNutritionalInformation);
                                    }, virtualThreadExecutor);
                                });

                        CompletableFuture<MealNutritionalInformationDTO> mealsFuture = mealLogHasMealsFuture
                                .thenCompose(hasMeals -> {
                                    if (!hasMeals) {
                                        return CompletableFuture.completedFuture(getDefaultNutritionalInformation());
                                    }

                                    return CompletableFuture.supplyAsync(() -> {
                                        return mealLogRepository
                                                .sumMealsNutritionalInformationByMealLogIdAndUserId(mealLogId, userId)
                                                .orElseGet(this::getDefaultNutritionalInformation);
                                    }, virtualThreadExecutor);
                                });

                        var nutritionalInformationFuture = foodsFuture
                                .thenCombine(mealsFuture, (foodsNI, mealsNI) -> new MealNutritionalInformationDTO(
                                                safeAdd(foodsNI.calories(), mealsNI.calories()),
                                                safeAdd(foodsNI.carbohydrates(), mealsNI.carbohydrates()),
                                                safeAdd(foodsNI.protein(), mealsNI.protein()),
                                                safeAdd(foodsNI.fat(), mealsNI.fat()),
                                                safeAdd(foodsNI.monounsaturatedFat(), mealsNI.monounsaturatedFat()),
                                                safeAdd(foodsNI.saturatedFat(), mealsNI.saturatedFat()),
                                                safeAdd(foodsNI.polyunsaturatedFat(), mealsNI.polyunsaturatedFat()),
                                                safeAdd(foodsNI.transFat(), mealsNI.transFat()),
                                                safeAdd(foodsNI.cholesterol(), mealsNI.cholesterol()),
                                                safeAdd(foodsNI.sodium(), mealsNI.sodium()),
                                                safeAdd(foodsNI.potassium(), mealsNI.potassium()),
                                                safeAdd(foodsNI.fiber(), mealsNI.fiber()),
                                                safeAdd(foodsNI.sugar(), mealsNI.sugar()),
                                                safeAdd(foodsNI.calcium(), mealsNI.calcium()),
                                                safeAdd(foodsNI.iron(), mealsNI.iron()),
                                                safeAdd(foodsNI.vitaminA(), mealsNI.vitaminA()),
                                                safeAdd(foodsNI.vitaminC(), mealsNI.vitaminC())
                                ));

                        return nutritionalInformationFuture.join();
                    } catch (CompletionException e) {
                        Throwable cause = e.getCause();

                        if (cause instanceof ResourceNotFoundException) {
                            throw (ResourceNotFoundException) cause;
                        }

                        throw new RuntimeException("Unexpected error occurred while getting daily progress.", e);
                    }
                })
                .reduce(getDefaultNutritionalInformation(), (dto1, dto2) -> new MealNutritionalInformationDTO(
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
                ));

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
