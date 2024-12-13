package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.food.FoodDTO;
import com.mumuca.diet.dto.food.NutritionalInformationDTO;
import com.mumuca.diet.dto.meal.*;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.model.Food;
import com.mumuca.diet.model.Meal;
import com.mumuca.diet.model.NutritionalInformation;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.FoodRepository;
import com.mumuca.diet.repository.MealRepository;
import com.mumuca.diet.repository.NutritionalInformationRepository;
import com.mumuca.diet.service.MealService;
import com.mumuca.diet.util.UpdateUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MealServiceImpl implements MealService {

    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;
    private final NutritionalInformationRepository nutritionalInformationRepository;

    @Override
    @Transactional
    public MealDTO createMeal(CreateMealDTO createMealDTO, String userId) {
        Meal meal = new Meal();

        meal.setTitle(createMealDTO.title());
        meal.setDescription(createMealDTO.description());
        meal.setType(createMealDTO.type());
        meal.setUser(new User(userId));

        foodRepository
                .findAllIdsByIdsAndUserId(createMealDTO.foodIds(), userId)
                .stream()
                .map(Food::new)
                .forEach(meal.getFoods()::add);

        mealRepository.save(meal);

        return new MealDTO(meal.getId(), meal.getTitle(), meal.getDescription(), meal.getType());
    }

    @Override
    public MealWithFoodsDTO getMeal(String mealId, String userId) {
        return mealRepository.findByIdAndUserIdWithFoods(mealId, userId)
                .map(meal -> new MealWithFoodsDTO(
                        meal.getId(),
                        meal.getTitle(),
                        meal.getDescription(),
                        meal.getType(),
                        meal.getFoods().stream().map(food -> {
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
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));
    }

    @Override
    public Optional<MealNutritionalInformationDTO> getMealNutritionalInformation(String mealId, String userId) {
        boolean mealExists = mealRepository.existsByIdAndUserId(mealId, userId);

        if (!mealExists) {
            throw new ResourceNotFoundException("Meal not found.");
        }

        boolean mealHasFood = mealRepository.existsFoodsByMealIdAndUserId(mealId, userId);

        if (!mealHasFood) {
            return Optional.empty();
        }

        return Optional.ofNullable(mealRepository
                .sumNutritionalInformationByMealIdAndUserId(mealId, userId));
    }

    @Override
    public MealDTO updateMeal(String mealId, UpdateMealDTO updateMealDTO, String userId) {
        Meal mealToUpdate = mealRepository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));

        boolean updated = false;

        updated |= UpdateUtils.updateIfDifferent(
                mealToUpdate::getTitle,
                mealToUpdate::setTitle,
                updateMealDTO.title()
        );

        updated |= UpdateUtils.updateIfDifferent(
                mealToUpdate::getDescription,
                mealToUpdate::setTitle,
                updateMealDTO.description()
        );

        updated |= UpdateUtils.updateIfDifferent(
                mealToUpdate::getTitle,
                mealToUpdate::setTitle,
                updateMealDTO.title()
        );

        if (updated) {
            mealRepository.save(mealToUpdate);
        }


        return new MealDTO(
                mealToUpdate.getId(),
                mealToUpdate.getTitle(),
                mealToUpdate.getDescription(),
                mealToUpdate.getType()
        );
    }

    @Override
    public void deleteMeal(String mealId, String userId) {
        Meal mealToDelete = mealRepository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));

        mealRepository.delete(mealToDelete);
    }

    @Override
    public void removeFoodsFromMeal(String mealId, List<String> foodIds, String userId) {
        Meal mealToRemoveFoods = mealRepository
                .findByIdAndUserIdWithFoods(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));

        long matchingFoodsCount = foodRepository.countByIdsAndUserId(foodIds, userId);

        if (matchingFoodsCount != foodIds.size()) {
            throw new ResourceNotFoundException("Some foods were not found or do not belong to the user.");
        }

        mealToRemoveFoods.getFoods()
                .removeIf(food -> foodIds.contains(food.getId()));

        mealRepository.save(mealToRemoveFoods);
    }

    @Override
    @Transactional
    public void addFoodsToMeal(String mealId, List<String> foodIds, String userId) {
        Meal mealToAddFoods = mealRepository.findByIdAndUserIdWithFoods(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));

        List<String> foodIdsToAdd = foodRepository
                .findAllIdsByIdsAndUserId(foodIds, userId);

        if (foodIdsToAdd.size() != foodIds.size()) {
            throw new ResourceNotFoundException("Some food were not found.");
        }

        Set<String> existingFoodIds = mealToAddFoods.getFoods().stream()
                .map(Food::getId)
                .collect(Collectors.toSet());

        List<String> uniqueFoodIdsToAdd = foodIdsToAdd.stream()
                .filter(foodId -> !existingFoodIds.contains(foodId))
                .toList();

        if (!uniqueFoodIdsToAdd.isEmpty()) {
            uniqueFoodIdsToAdd
                    .forEach(foodId -> mealToAddFoods.getFoods().add(new Food(foodId)));
            mealRepository.save(mealToAddFoods);
        }
    }

    @Override
    public Page<MealDTO> getMeals(Pageable pageable, String userId) {
        return mealRepository.findByUserId(pageable, userId)
                .map((meal -> new MealDTO(meal.getId(), meal.getTitle(), meal.getDescription(), meal.getType())));
    }
}
