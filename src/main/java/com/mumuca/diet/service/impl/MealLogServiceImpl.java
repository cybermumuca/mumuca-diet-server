package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.food.FoodDTO;
import com.mumuca.diet.dto.food.NutritionalInformationDTO;
import com.mumuca.diet.dto.meal.MealNutritionalInformationDTO;
import com.mumuca.diet.dto.meal.MealWithFoodsDTO;
import com.mumuca.diet.dto.meallog.*;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.model.*;
import com.mumuca.diet.repository.*;
import com.mumuca.diet.service.MealLogService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
    private final UserRepository userRepository;

    @Override
    public MealLogDTO createMealLog(CreateMealLogDTO createMealLogDTO, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not Found."));

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
                    var caloriesConsumed = mealLogRepository
                            .sumFoodsAndMealsCaloriesByMealLogIdAndUserId(mealLog.getId(), userId)
                            .orElse(0);

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

                    return new FoodDTO(
                            food.getId(),
                            food.getTitle(),
                            food.getBrand(),
                            food.getDescription(),
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

                                    return new FoodDTO(
                                            food.getId(),
                                            food.getTitle(),
                                            food.getBrand(),
                                            food.getDescription(),
                                            nutritionalInformationDTO
                                    );
                        }).toList()
                ))
                .toList();
    }

    // TODO: Fix NullPointerException exception when any query field is null
    @Override
    public MealNutritionalInformationDTO getMealLogNutritionalInformation(String mealLogId, String userId) {
        try (var virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            var foodsFuture = CompletableFuture.supplyAsync(() ->
                            mealLogRepository.sumFoodsNutritionalInformationByMealLogIdAndUserId(mealLogId, userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found.")),
                    virtualThreadExecutor
            );

            var mealsFuture = CompletableFuture.supplyAsync(() ->
                            mealLogRepository.sumMealsNutritionalInformationByMealLogIdAndUserId(mealLogId, userId)
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

            throw new RuntimeException("Unexpected error occurred while fetching meal data.", e);
        }
    }

    @Override
    public void addFoodsToMealLog(String mealLogId, AddFoodsToMealLogDTO addFoodsToMealLogDTO, String userId) {
        MealLog mealLogToAddFoods = mealLogRepository
                .findMealLogByIdAndUserIdWithFoods(mealLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log not found."));

        List<Food> foodsToAdd = foodRepository
                .findAllByIdsAndUserId(addFoodsToMealLogDTO.foodIds(), userId);

        if (foodsToAdd.size() != addFoodsToMealLogDTO.foodIds().size()) {
            throw new ResourceNotFoundException("Some foods were not found.");
        }

        Set<Food> existingFoods = new HashSet<>(mealLogToAddFoods.getFoods());

        List<Food> uniqueFoodsToAdd = foodsToAdd.stream()
                .filter(food -> !existingFoods.contains(food))
                .toList();

        if (!uniqueFoodsToAdd.isEmpty()) {
            mealLogToAddFoods.getFoods().addAll(uniqueFoodsToAdd);
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
