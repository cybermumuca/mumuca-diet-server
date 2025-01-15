package com.mumuca.diet.service.impl;

import com.mumuca.diet.food.dto.FoodDTO;
import com.mumuca.diet.food.dto.NutritionalInformationDTO;
import com.mumuca.diet.food.dto.PortionDTO;
import com.mumuca.diet.dto.meal.MealNutritionalInformationDTO;
import com.mumuca.diet.dto.meal.MealWithFoodsDTO;
import com.mumuca.diet.dto.meallog.*;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.food.model.Food;
import com.mumuca.diet.food.model.NutritionalInformation;
import com.mumuca.diet.food.repository.FoodRepository;
import com.mumuca.diet.model.*;
import com.mumuca.diet.repository.*;
import com.mumuca.diet.service.MealLogService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.mumuca.diet.util.UpdateUtils.updateIfDifferent;

@Service
@AllArgsConstructor
public class MealLogServiceImpl implements MealLogService {

    private final MealLogRepository mealLogRepository;
    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;

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

    private boolean isDefaultNutritionalInformation(MealNutritionalInformationDTO info) {
        return info.calories().equals(BigDecimal.ZERO)
                && info.carbohydrates().equals(BigDecimal.ZERO)
                && info.protein().equals(BigDecimal.ZERO)
                && info.fat().equals(BigDecimal.ZERO)
                && info.monounsaturatedFat().equals(BigDecimal.ZERO)
                && info.saturatedFat().equals(BigDecimal.ZERO)
                && info.polyunsaturatedFat().equals(BigDecimal.ZERO)
                && info.transFat().equals(BigDecimal.ZERO)
                && info.cholesterol().equals(BigDecimal.ZERO)
                && info.sodium().equals(BigDecimal.ZERO)
                && info.potassium().equals(BigDecimal.ZERO)
                && info.fiber().equals(BigDecimal.ZERO)
                && info.sugar().equals(BigDecimal.ZERO)
                && info.calcium().equals(BigDecimal.ZERO)
                && info.iron().equals(BigDecimal.ZERO)
                && info.vitaminA().equals(BigDecimal.ZERO)
                && info.vitaminC().equals(BigDecimal.ZERO);
    }

    @Override
    public MealLogDTO createMealLog(CreateMealLogDTO createMealLogDTO, String userId) {
        User user = new User(userId);

        MealLog mealLog = new MealLog();

        mealLog.setDate(createMealLogDTO.date());
        mealLog.setTime(createMealLogDTO.time());
        mealLog.setCaloriesGoal(createMealLogDTO.caloriesGoal());
        mealLog.setType(createMealLogDTO.type());
        mealLog.setUser(user);

        mealLogRepository.save(mealLog);

        return new MealLogDTO(
                mealLog.getId(),
                mealLog.getType(),
                mealLog.getDate(),
                mealLog.getTime(),
                mealLog.getCaloriesGoal()
        );
    }

    @Override
    public void deleteMealLog(String mealLogId, String userId) {
        MealLog mealLogToDelete = mealLogRepository.findMealLogByIdAndUserId(mealLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log Not Found."));

        mealLogRepository.deleteById(mealLogToDelete.getId());
    }

    @Override
    public MealLogDTO getMealLog(String mealLogId, String userId) {
        return mealLogRepository.findMealLogByIdAndUserId(mealLogId, userId)
                .map(mealLog -> new MealLogDTO(
                        mealLog.getId(),
                        mealLog.getType(),
                        mealLog.getDate(),
                        mealLog.getTime(),
                        mealLog.getCaloriesGoal()
                ))
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found."));
    }

    @Override
    @Transactional
    public List<MealLogWithCaloriesConsumedDTO> findMealLogsByDate(LocalDate date, String userId) {
        List<MealLog> mealLogs = mealLogRepository.findByDateAndUserId(date, userId);

        return mealLogs
                .stream()
                .map(mealLog -> {
                    var foodsCalories = mealLogRepository
                            .sumCaloriesFromFoodsByMealLogIdAndUserId(mealLog.getId(), userId);

                    var mealCalories = mealLogRepository
                            .sumCaloriesFromMealsByMealLogIdAndUserId(mealLog.getId(), userId);

                    var caloriesConsumed = foodsCalories + mealCalories;

                    return new MealLogWithCaloriesConsumedDTO(
                            mealLog.getId(),
                            mealLog.getType(),
                            mealLog.getDate(),
                            mealLog.getTime(),
                            mealLog.getCaloriesGoal(),
                            caloriesConsumed
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FoodDTO> getMealLogFoods(String mealLogId, String userId) {
        return foodRepository
                .findAllByMealLogsIdAndUserId(mealLogId, userId)
                .stream()
                .map(food -> {
                    NutritionalInformationDTO nutritionalInformationDTO = null;

                    if (food.getNutritionalInformation() != null) {
                        NutritionalInformation ni = food.getNutritionalInformation();
                        nutritionalInformationDTO = new NutritionalInformationDTO(
                                ni.getId(),
                                ni.getCalories(),
                                ni.getCarbohydrates(),
                                ni.getProtein(),
                                ni.getFat(),
                                ni.getMonounsaturatedFat(),
                                ni.getSaturatedFat(),
                                ni.getPolyunsaturatedFat(),
                                ni.getTransFat(),
                                ni.getCholesterol(),
                                ni.getSodium(),
                                ni.getPotassium(),
                                ni.getFiber(),
                                ni.getSugar(),
                                ni.getCalcium(),
                                ni.getIron(),
                                ni.getVitaminA(),
                                ni.getVitaminC()
                        );
                    }
                    var portionDTO = new PortionDTO(
                            food.getPortion().getId(),
                            food.getPortion().getAmount(),
                            food.getPortion().getUnit(),
                            food.getPortion().getDescription()
                    );

                    return new FoodDTO(
                            food.getId(),
                            food.getTitle(),
                            food.getBrand(),
                            food.getDescription(),
                            portionDTO,
                            nutritionalInformationDTO
                    );
                })
                .toList();
    }

    @Override
    public List<MealWithFoodsDTO> getMealLogMeals(String mealLogId, String userId) {
        return mealRepository
                .findAllByMealLogsIdAndUserId(mealLogId, userId)
                .stream()
                .map(meal -> new MealWithFoodsDTO(
                        meal.getId(),
                        meal.getTitle(),
                        meal.getDescription(),
                        meal.getType(),
                        meal.getFoods()
                                .stream()
                                .map(food -> {
                                    NutritionalInformationDTO nutritionalInformationDTO = null;

                                    if (food.getNutritionalInformation() != null) {
                                        NutritionalInformation ni = food.getNutritionalInformation();
                                        nutritionalInformationDTO = new NutritionalInformationDTO(
                                                ni.getId(),
                                                ni.getCalories(),
                                                ni.getCarbohydrates(),
                                                ni.getProtein(),
                                                ni.getFat(),
                                                ni.getMonounsaturatedFat(),
                                                ni.getSaturatedFat(),
                                                ni.getPolyunsaturatedFat(),
                                                ni.getTransFat(),
                                                ni.getCholesterol(),
                                                ni.getSodium(),
                                                ni.getPotassium(),
                                                ni.getFiber(),
                                                ni.getSugar(),
                                                ni.getCalcium(),
                                                ni.getIron(),
                                                ni.getVitaminA(),
                                                ni.getVitaminC()
                                        );
                                    }

                                    var portionDTO = new PortionDTO(
                                            food.getPortion().getId(),
                                            food.getPortion().getAmount(),
                                            food.getPortion().getUnit(),
                                            food.getPortion().getDescription()
                                    );

                                    return new FoodDTO(
                                            food.getId(),
                                            food.getTitle(),
                                            food.getBrand(),
                                            food.getDescription(),
                                            portionDTO,
                                            nutritionalInformationDTO
                                    );
                        }).toList()
                ))
                .toList();
    }

    // TODO: TEST THIS
    @Override
    public Optional<MealNutritionalInformationDTO> getMealLogNutritionalInformation(String mealLogId, String userId) {
        boolean mealLogExists = mealLogRepository.existsByIdAndUserId(mealLogId, userId);

        if (!mealLogExists) {
            throw new ResourceNotFoundException("Meal Log not found.");
        }

        try (var virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
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
                    }
            );

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
                    )
            );

            var nutritionalInformation = nutritionalInformationFuture.join();

            if (isDefaultNutritionalInformation(nutritionalInformation)) {
                return Optional.empty();
            }

            return Optional.of(nutritionalInformation);
        } catch (CompletionException e) {
            Throwable cause = e.getCause();

            if (cause instanceof ResourceNotFoundException) {
                throw (ResourceNotFoundException) cause;
            }

            throw new RuntimeException("Failed to calculate nutritional information for Meal Log with ID: " + mealLogId, e);
        }
    }

    @Override
    public void addFoodsToMealLog(String mealLogId, AddFoodsToMealLogDTO addFoodsToMealLogDTO, String userId) {
        MealLog mealLogToAddFoods = mealLogRepository
                .findMealLogByIdAndUserIdWithFoods(mealLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found."));

        List<String> foodIdsToAdd = foodRepository
                .findAllIdsByIdsAndUserId(addFoodsToMealLogDTO.foodIds(), userId);

        if (foodIdsToAdd.size() != addFoodsToMealLogDTO.foodIds().size()) {
            throw new ResourceNotFoundException("Some foods were not found.");
        }

        Set<String> existingFoodIds = mealLogToAddFoods.getFoods().stream()
                .map(Food::getId)
                .collect(Collectors.toSet());

        List<String> uniqueFoodIdsToAdd = foodIdsToAdd.stream()
                .filter(foodId -> !existingFoodIds.contains(foodId))
                .toList();

        if (!uniqueFoodIdsToAdd.isEmpty()) {
            uniqueFoodIdsToAdd.forEach(foodId -> mealLogToAddFoods.getFoods().add(new Food(foodId)));
            mealLogRepository.save(mealLogToAddFoods);
        }
    }

    @Override
    public void addMealsToMealLog(String mealLogId, AddMealsToMealLogDTO addMealsToMealLogDTO, String userId) {
        MealLog mealLogToAddMeals = mealLogRepository
                .findMealLogByIdAndUserIdWithMeals(mealLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found."));

        List<Meal> mealsToAdd = mealRepository.findAllByIdsAndUserId(addMealsToMealLogDTO.mealIds(), userId);

        if (mealsToAdd.size() != addMealsToMealLogDTO.mealIds().size()) {
            throw new ResourceNotFoundException("Some meals were not found.");
        }

        Set<Meal> existingMeals = new HashSet<>(mealLogToAddMeals.getMeals());

        List<Meal> uniqueMealsToAdd = mealsToAdd.stream()
                .filter(meal -> !existingMeals.contains(meal))
                .toList();

        if (!uniqueMealsToAdd.isEmpty()) {
            mealLogToAddMeals
                    .getMeals()
                    .addAll(uniqueMealsToAdd);
            mealLogRepository.save(mealLogToAddMeals);
        }
    }

    @Override
    public void updateMealLog(String mealLogId, UpdateMealLogDTO updateMealLogDTO, String userId) {
        MealLog mealLogToUpdate = mealLogRepository.findMealLogByIdAndUserId(mealLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found."));

        boolean updated = false;

        updated |= updateIfDifferent(
                mealLogToUpdate::getTime,
                mealLogToUpdate::setTime,
                updateMealLogDTO.time()
        );

        updated |= updateIfDifferent(
                mealLogToUpdate::getType,
                mealLogToUpdate::setType,
                updateMealLogDTO.type()
        );

        updated |= updateIfDifferent(
                mealLogToUpdate::getCaloriesGoal,
                mealLogToUpdate::setCaloriesGoal,
                updateMealLogDTO.caloriesGoal()
        );

        if (updated) {
            mealLogRepository.save(mealLogToUpdate);
        }
    }

    @Override
    public void removeMealLogFoods(String mealLogId, DeleteMealLogFoodsDTO deleteMealLogFoodsDTO, String userId) {
        MealLog mealLog = mealLogRepository
                .findMealLogByIdAndUserIdWithFoods(mealLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found."));

        long matchingFoodsCount = foodRepository.countByIdsAndUserId(deleteMealLogFoodsDTO.foodIds(), userId);

        if (matchingFoodsCount != deleteMealLogFoodsDTO.foodIds().size()) {
            throw new ResourceNotFoundException("Some foods were not found.");
        }

        mealLog.getFoods()
                .removeIf(food -> deleteMealLogFoodsDTO.foodIds().contains(food.getId()));

        mealLogRepository.save(mealLog);
    }

    @Override
    public void removeMealLogMeals(String mealLogId, DeleteMealLogMealsDTO deleteMealLogMealsDTO, String userId) {
        MealLog mealLog = mealLogRepository
                .findMealLogByIdAndUserIdWithMeals(mealLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found."));

        long matchingMealsCount = mealRepository.countByIdsAndUserId(deleteMealLogMealsDTO.mealIds(), userId);

        if (matchingMealsCount != deleteMealLogMealsDTO.mealIds().size()) {
            throw new ResourceNotFoundException("Some meals were not found.");
        }

        mealLog.getMeals()
                .removeIf(meal -> deleteMealLogMealsDTO.mealIds().contains(meal.getId()));

        mealLogRepository.save(mealLog);
    }
}
