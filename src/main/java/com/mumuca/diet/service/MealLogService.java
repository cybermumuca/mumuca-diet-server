package com.mumuca.diet.service;

import com.mumuca.diet.dto.meallog.CreateMealLogDTO;
import com.mumuca.diet.dto.meallog.MealLogDTO;
import jakarta.validation.Valid;

public interface MealLogService {
    MealLogDTO createMealLog(CreateMealLogDTO createMealLogDTO, String userId);
    void deleteMealLog(String mealLogId, String userId);
}
