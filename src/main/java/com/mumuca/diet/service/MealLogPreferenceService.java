package com.mumuca.diet.service;

import com.mumuca.diet.dto.meallogpreferences.CreateMealLogPreferenceDTO;
import com.mumuca.diet.dto.meallogpreferences.MealLogPreferenceDTO;
import com.mumuca.diet.dto.meallogpreferences.UpdateMealLogPreferenceDTO;

import java.util.List;

public interface MealLogPreferenceService {
    List<MealLogPreferenceDTO> createMealLogPreference(List<CreateMealLogPreferenceDTO> createMealLogPreferenceDTOList, String userId);
    MealLogPreferenceDTO updateMealLogPreference(String mealLogPreferenceId, UpdateMealLogPreferenceDTO updateMealLogPreferenceDTO, String userId);
    void deleteMealLogPreference(String mealLogPreferenceId, String userId);
    List<MealLogPreferenceDTO> getUserMealLogPreferences(String userId);
}
