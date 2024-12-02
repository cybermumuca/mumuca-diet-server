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
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.service.MealService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MealServiceImpl implements MealService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final NutritionalInformationRepository nutritionalInformationRepository;

    @Override
    @Transactional
    public MealDTO createMeal(CreateMealDTO createMealDTO, String userId) {
        Meal meal = new Meal();

        meal.setTitle(createMealDTO.title());
        meal.setDescription(createMealDTO.description());
        meal.setType(createMealDTO.type());

        List<Food> foods = foodRepository.findAllByIdsAndUserId(createMealDTO.foodIds(), userId);

        meal.setFoods(foods);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        meal.setUser(user);

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
        return nutritionalInformationRepository
                .sumNutritionalInformationByMealIdAndUserId(mealId, userId);
    }

    @Override
    public MealDTO updateMeal(String mealId, UpdateMealDTO updateMealDTO, String userId) {
        Meal meal = mealRepository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));

        if (updateMealDTO.title() != null && !updateMealDTO.title().isBlank()) {
            meal.setTitle(updateMealDTO.title());
        }

        if (updateMealDTO.description() != null) {
            meal.setDescription(updateMealDTO.description());
        }

        if (updateMealDTO.type() != null) {
            meal.setType(updateMealDTO.type());
        }

        mealRepository.save(meal);

        return new MealDTO(meal.getId(), meal.getTitle(), meal.getDescription(), meal.getType());
    }

    @Override
    public void deleteMeal(String mealId, String userId) {
        Meal mealToDelete = mealRepository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));

        mealRepository.delete(mealToDelete);
    }

    @Override
    @Transactional
    public void removeFoodsFromMeal(String mealId, List<String> foodIds, String userId) {
        Meal meal = mealRepository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));

        List<Food> foodsToRemove = foodRepository.findAllByIdsAndUserId(foodIds, userId);

        if (foodsToRemove.isEmpty()) {
            throw new ResourceNotFoundException("Foods not found.");
        }

        meal.getFoods().removeAll(foodsToRemove);

        mealRepository.save(meal);
    }

    @Override
    public void addFoodsToMeal(String mealId, List<String> foodIds, String userId) {
        Meal meal = mealRepository.findByIdAndUserIdWithFoods(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));

        List<Food> foodsToAdd = foodRepository.findAllByIdsAndUserId(foodIds, userId);

        if (foodsToAdd.isEmpty()) {
            throw new ResourceNotFoundException("Foods not found.");
        }

        if (foodsToAdd.size() != foodIds.size()) {
            throw new ResourceNotFoundException("Some food were not found.");
        }

        foodsToAdd.stream()
                .filter(food -> !meal.getFoods().contains(food))
                .forEach(food -> meal.getFoods().add(food));

        mealRepository.save(meal);
    }
}
