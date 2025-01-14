package com.mumuca.diet.food.service.impl;

import com.mumuca.diet.dto.meal.MealDTO;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.food.dto.*;
import com.mumuca.diet.food.mapper.NutritionalInformationMapper;
import com.mumuca.diet.food.mapper.PortionMapper;
import com.mumuca.diet.food.repository.FoodRepository;
import com.mumuca.diet.food.repository.NutritionalInformationRepository;
import com.mumuca.diet.food.repository.PortionRepository;
import com.mumuca.diet.food.model.Food;
import com.mumuca.diet.food.model.NutritionalInformation;
import com.mumuca.diet.food.model.Portion;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.*;
import com.mumuca.diet.food.service.FoodService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mumuca.diet.util.UpdateUtils.updateIfDifferent;

@Service
@AllArgsConstructor
@Slf4j
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final NutritionalInformationRepository nutritionalInformationRepository;
    private final PortionRepository portionRepository;
    private final MealRepository mealRepository;

    private final NutritionalInformationMapper nutritionalInformationMapper;
    private final PortionMapper portionMapper;

    @Override
    @Transactional
    public FoodDTO createFood(CreateFoodDTO createFoodDTO, String userId) {
        User user = new User(userId);

        Food food = new Food();

        food.setTitle(createFoodDTO.title());
        food.setDescription(createFoodDTO.description());
        food.setBrand(createFoodDTO.brand());
        food.setUser(user);

        foodRepository.save(food);

        NutritionalInformation nutritionalInformation = nutritionalInformationMapper.fromCreateNIDTOToNI(createFoodDTO.nutritionalInformation());

        nutritionalInformation.setFood(food);
        food.setNutritionalInformation(nutritionalInformation);

        nutritionalInformationRepository.save(nutritionalInformation);

        NutritionalInformationDTO nutritionalInformationDTO = nutritionalInformationMapper.fromNIToNIDTO(nutritionalInformation);

        Portion portion = portionMapper.fromCreatePortionDTOToPortion(createFoodDTO.portion());

        portion.setFood(food);
        food.setPortion(portion);

        portionRepository.save(portion);

        PortionDTO portionDTO = portionMapper.fromPortionToPortionDTO(portion);

        return new FoodDTO(
                food.getId(),
                food.getTitle(),
                food.getBrand(),
                food.getDescription(),
                portionDTO,
                nutritionalInformationDTO
        );
    }

    @Override
    public FoodDTO getFood(String foodId, String userId) {
        Food food = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        NutritionalInformationDTO nutritionalInformationDTO = nutritionalInformationMapper.fromNIToNIDTO(food.getNutritionalInformation());

        PortionDTO portionDTO = portionMapper.fromPortionToPortionDTO(food.getPortion());

        return new FoodDTO(
                food.getId(),
                food.getTitle(),
                food.getBrand(),
                food.getDescription(),
                portionDTO,
                nutritionalInformationDTO
        );
    }

    @Override
    public NutritionalInformationDTO getFoodNutritionalInformation(String foodId, String userId) {
        Food food = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        NutritionalInformation ni = food.getNutritionalInformation();

        if (ni == null) {
            throw new ResourceNotFoundException("Nutritional information not found for the specified food.");
        }

        return nutritionalInformationMapper.fromNIToNIDTO(ni);
    }

    // TODO: test this
    @Override
    @Transactional
    public FoodDTO updateFood(String foodId, UpdateFoodDTO updateFoodDTO, String userId) {
        Food foodToUpdate = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        boolean updated = false;

        updated |= updateIfDifferent(
                foodToUpdate::getTitle,
                foodToUpdate::setTitle,
                updateFoodDTO.title()
        );

        updated |= updateIfDifferent(
                foodToUpdate::getDescription,
                foodToUpdate::setDescription,
                updateFoodDTO.description()
        );

        updated |= updateIfDifferent(
                foodToUpdate::getBrand,
                foodToUpdate::setBrand,
                updateFoodDTO.brand()
        );

        if (updateFoodDTO.nutritionalInformation() != null) {
            if (foodToUpdate.getNutritionalInformation() == null) {
                NutritionalInformation nutritionalInformation = new NutritionalInformation();

                updated |= updateNutritionalInformationFields(
                        nutritionalInformation,
                        updateFoodDTO.nutritionalInformation(),
                        updated
                );

                nutritionalInformation.setFood(foodToUpdate);
                foodToUpdate.setNutritionalInformation(nutritionalInformation);
            } else {
                updated |= updateNutritionalInformationFields(
                        foodToUpdate.getNutritionalInformation(),
                        updateFoodDTO.nutritionalInformation(),
                        updated
                );
            }
        }

        if (updateFoodDTO.portion() != null) {
            updated |= updateIfDifferent(
                    foodToUpdate.getPortion()::getDescription,
                    foodToUpdate.getPortion()::setDescription,
                    updateFoodDTO.portion().description()
            );

            updated |= updateIfDifferent(
                    foodToUpdate.getPortion()::getAmount,
                    foodToUpdate.getPortion()::setAmount,
                    updateFoodDTO.portion().amount()
            );

            updated |= updateIfDifferent(
                    foodToUpdate.getPortion()::getUnit,
                    foodToUpdate.getPortion()::setUnit,
                    updateFoodDTO.portion().unit()
            );
        }

        if (updated) {
            foodRepository.save(foodToUpdate);
        }

        NutritionalInformationDTO nutritionalInformationDTO = nutritionalInformationMapper.fromNIToNIDTO(foodToUpdate.getNutritionalInformation());

        PortionDTO portionDTO = portionMapper.fromPortionToPortionDTO(foodToUpdate.getPortion());

        return new FoodDTO(
                foodToUpdate.getId(),
                foodToUpdate.getTitle(),
                foodToUpdate.getBrand(),
                foodToUpdate.getDescription(),
                portionDTO,
                nutritionalInformationDTO
        );
    }

    private boolean updateNutritionalInformationFields(NutritionalInformation nutritionalInformation, UpdateNutritionalInformationDTO updateNutritionalInfoDTO, boolean updated) {
        updated |= updateIfDifferent(
                nutritionalInformation::getCalories,
                nutritionalInformation::setCalories,
                updateNutritionalInfoDTO.calories()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getCarbohydrates,
                nutritionalInformation::setCarbohydrates,
                updateNutritionalInfoDTO.carbohydrates()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getProtein,
                nutritionalInformation::setProtein,
                updateNutritionalInfoDTO.protein()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getFat,
                nutritionalInformation::setFat,
                updateNutritionalInfoDTO.fat()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getMonounsaturatedFat,
                nutritionalInformation::setMonounsaturatedFat,
                updateNutritionalInfoDTO.monounsaturatedFat()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getSaturatedFat,
                nutritionalInformation::setSaturatedFat,
                updateNutritionalInfoDTO.saturatedFat()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getPolyunsaturatedFat,
                nutritionalInformation::setPolyunsaturatedFat,
                updateNutritionalInfoDTO.polyunsaturatedFat()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getTransFat,
                nutritionalInformation::setTransFat,
                updateNutritionalInfoDTO.transFat()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getCholesterol,
                nutritionalInformation::setCholesterol,
                updateNutritionalInfoDTO.cholesterol()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getSodium,
                nutritionalInformation::setSodium,
                updateNutritionalInfoDTO.sodium()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getPotassium,
                nutritionalInformation::setPotassium,
                updateNutritionalInfoDTO.potassium()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getFiber,
                nutritionalInformation::setFiber,
                updateNutritionalInfoDTO.fiber()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getSugar,
                nutritionalInformation::setSugar,
                updateNutritionalInfoDTO.sugar()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getCalcium,
                nutritionalInformation::setCalcium,
                updateNutritionalInfoDTO.calcium()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getIron,
                nutritionalInformation::setIron,
                updateNutritionalInfoDTO.iron()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getVitaminA,
                nutritionalInformation::setVitaminA,
                updateNutritionalInfoDTO.vitaminA()
        );

        updated |= updateIfDifferent(
                nutritionalInformation::getVitaminC,
                nutritionalInformation::setVitaminC,
                updateNutritionalInfoDTO.vitaminC()
        );

        return updated;
    }

    @Override
    public void deleteFood(String foodId, String userId) {
        Food foodToDelete = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        foodRepository.deleteById(foodToDelete.getId());
    }

    @Override
    public List<MealDTO> getFoodMeals(String foodId, String userId) {
        return mealRepository.findByFoodsIdAndUserId(foodId, userId)
                .stream()
                .map(meal -> new MealDTO(
                        meal.getId(),
                        meal.getTitle(),
                        meal.getDescription(),
                        meal.getType()
                )).toList();
    }

    @Override
    public Page<FoodDTO> getFoods(Pageable pageable, String userId) {
        return foodRepository.findByUserId(pageable, userId)
                .map(food -> {
                    NutritionalInformationDTO nutritionalInformationDTO = nutritionalInformationMapper.fromNIToNIDTO(food.getNutritionalInformation());

                    PortionDTO portionDTO = portionMapper.fromPortionToPortionDTO(food.getPortion());

                    return new FoodDTO(
                            food.getId(),
                            food.getTitle(),
                            food.getBrand(),
                            food.getDescription(),
                            portionDTO,
                            nutritionalInformationDTO
                    );
                });
    }
}
