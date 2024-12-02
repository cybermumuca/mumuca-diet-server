package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.food.*;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.model.Food;
import com.mumuca.diet.model.NutritionalInformation;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.FoodRepository;
import com.mumuca.diet.repository.NutritionalInformationRepository;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.service.FoodService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FoodDTO createFood(CreateFoodDTO createFoodDTO, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Food food = new Food();

        food.setTitle(createFoodDTO.title());
        food.setDescription(createFoodDTO.description());
        food.setBrand(createFoodDTO.brand());
        food.setUser(user);

        if (createFoodDTO.nutritionalInformation() != null) {
            CreateNutritionalInformationDTO nutritionalInfoDTO = createFoodDTO.nutritionalInformation();

            NutritionalInformation nutritionalInformation = new NutritionalInformation();
            nutritionalInformation.setCalories(nutritionalInfoDTO.calories());
            nutritionalInformation.setCarbohydrates(nutritionalInfoDTO.carbohydrates());
            nutritionalInformation.setProtein(nutritionalInfoDTO.protein());
            nutritionalInformation.setFat(nutritionalInfoDTO.fat());
            nutritionalInformation.setMonounsaturatedFat(nutritionalInfoDTO.monounsaturatedFat());
            nutritionalInformation.setSaturatedFat(nutritionalInfoDTO.saturatedFat());
            nutritionalInformation.setPolyunsaturatedFat(nutritionalInfoDTO.polyunsaturatedFat());
            nutritionalInformation.setTransFat(nutritionalInfoDTO.transFat());
            nutritionalInformation.setCholesterol(nutritionalInfoDTO.cholesterol());
            nutritionalInformation.setSodium(nutritionalInfoDTO.sodium());
            nutritionalInformation.setPotassium(nutritionalInfoDTO.potassium());
            nutritionalInformation.setFiber(nutritionalInfoDTO.fiber());
            nutritionalInformation.setSugar(nutritionalInfoDTO.sugar());
            nutritionalInformation.setCalcium(nutritionalInfoDTO.calcium());
            nutritionalInformation.setIron(nutritionalInfoDTO.iron());
            nutritionalInformation.setVitaminA(nutritionalInfoDTO.vitaminA());
            nutritionalInformation.setVitaminC(nutritionalInfoDTO.vitaminC());

            food.setNutritionalInformation(nutritionalInformation);
        }

        foodRepository.save(food);

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
    }

    @Override
    public FoodDTO getFood(String foodId, String userId) {
        Food food = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

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
    }

    @Override
    public NutritionalInformationDTO getFoodNutritionalInformation(String foodId, String userId) {
        Food food = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        NutritionalInformation ni = food.getNutritionalInformation();

        if (ni == null) {
            throw new ResourceNotFoundException("Nutritional information not found for the specified food.");
        }

        return new NutritionalInformationDTO(
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

    @Override
    @Transactional
    public FoodDTO updateFood(String foodId, UpdateFoodDTO updateFoodDTO, String userId) {
        Food food = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        if (updateFoodDTO.title() != null && !updateFoodDTO.title().isBlank()) {
            food.setTitle(updateFoodDTO.title());
        }

        if (updateFoodDTO.description() != null) {
            food.setDescription(updateFoodDTO.description());
        }

        if (updateFoodDTO.brand() != null) {
            food.setBrand(updateFoodDTO.brand());
        }

        if (updateFoodDTO.nutritionalInformation() != null) {
            if (food.getNutritionalInformation() == null) {
                NutritionalInformation nutritionalInformation = new NutritionalInformation();
                updateNutritionalInformationFields(nutritionalInformation, updateFoodDTO.nutritionalInformation());
                nutritionalInformation.setFood(food);
                food.setNutritionalInformation(nutritionalInformation);
            } else {
                updateNutritionalInformationFields(food.getNutritionalInformation(), updateFoodDTO.nutritionalInformation());
            }
        }

        foodRepository.save(food);

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
    }

    private void updateNutritionalInformationFields(NutritionalInformation nutritionalInformation, UpdateNutritionalInformationDTO updateNutritionalInfoDTO) {
        if (updateNutritionalInfoDTO.calories() != null) {
            nutritionalInformation.setCalories(updateNutritionalInfoDTO.calories());
        }
        if (updateNutritionalInfoDTO.carbohydrates() != null) {
            nutritionalInformation.setCarbohydrates(updateNutritionalInfoDTO.carbohydrates());
        }
        if (updateNutritionalInfoDTO.protein() != null) {
            nutritionalInformation.setProtein(updateNutritionalInfoDTO.protein());
        }
        if (updateNutritionalInfoDTO.fat() != null) {
            nutritionalInformation.setFat(updateNutritionalInfoDTO.fat());
        }
        if (updateNutritionalInfoDTO.monounsaturatedFat() != null) {
            nutritionalInformation.setMonounsaturatedFat(updateNutritionalInfoDTO.monounsaturatedFat());
        }
        if (updateNutritionalInfoDTO.saturatedFat() != null) {
            nutritionalInformation.setSaturatedFat(updateNutritionalInfoDTO.saturatedFat());
        }
        if (updateNutritionalInfoDTO.polyunsaturatedFat() != null) {
            nutritionalInformation.setPolyunsaturatedFat(updateNutritionalInfoDTO.polyunsaturatedFat());
        }
        if (updateNutritionalInfoDTO.transFat() != null) {
            nutritionalInformation.setTransFat(updateNutritionalInfoDTO.transFat());
        }
        if (updateNutritionalInfoDTO.cholesterol() != null) {
            nutritionalInformation.setCholesterol(updateNutritionalInfoDTO.cholesterol());
        }
        if (updateNutritionalInfoDTO.sodium() != null) {
            nutritionalInformation.setSodium(updateNutritionalInfoDTO.sodium());
        }
        if (updateNutritionalInfoDTO.potassium() != null) {
            nutritionalInformation.setPotassium(updateNutritionalInfoDTO.potassium());
        }
        if (updateNutritionalInfoDTO.fiber() != null) {
            nutritionalInformation.setFiber(updateNutritionalInfoDTO.fiber());
        }
        if (updateNutritionalInfoDTO.sugar() != null) {
            nutritionalInformation.setSugar(updateNutritionalInfoDTO.sugar());
        }
        if (updateNutritionalInfoDTO.calcium() != null) {
            nutritionalInformation.setCalcium(updateNutritionalInfoDTO.calcium());
        }
        if (updateNutritionalInfoDTO.iron() != null) {
            nutritionalInformation.setIron(updateNutritionalInfoDTO.iron());
        }
        if (updateNutritionalInfoDTO.vitaminA() != null) {
            nutritionalInformation.setVitaminA(updateNutritionalInfoDTO.vitaminA());
        }
        if (updateNutritionalInfoDTO.vitaminC() != null) {
            nutritionalInformation.setVitaminC(updateNutritionalInfoDTO.vitaminC());
        }
    }


    @Override
    public void deleteFood(String foodId, String userId) {
        Food foodToDelete = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        foodRepository.deleteById(foodToDelete.getId());
    }
}
