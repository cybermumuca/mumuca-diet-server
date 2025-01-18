package com.mumuca.diet.meal.service;

import com.mumuca.diet.meal.dto.CreateMealLogPreferenceDTO;
import com.mumuca.diet.meal.dto.MealLogPreferenceDTO;
import com.mumuca.diet.meal.dto.UpdateMealLogPreferenceDTO;

import java.util.List;

public interface MealLogPreferenceService {
    List<MealLogPreferenceDTO> createMealLogPreference(List<CreateMealLogPreferenceDTO> createMealLogPreferenceDTOList, String userId);
    MealLogPreferenceDTO updateMealLogPreference(String mealLogPreferenceId, UpdateMealLogPreferenceDTO updateMealLogPreferenceDTO, String userId);
    void deleteMealLogPreference(String mealLogPreferenceId, String userId);
    List<MealLogPreferenceDTO> getUserMealLogPreferences(String userId);
}
