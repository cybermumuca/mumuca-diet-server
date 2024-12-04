package com.mumuca.diet.service;

import com.mumuca.diet.dto.goal.GoalDTO;
import com.mumuca.diet.dto.goal.UpdateMacronutrientGoalDTO;
import com.mumuca.diet.dto.goal.UpdateWaterIntakeGoalDTO;
import jakarta.validation.Valid;

public interface GoalService {
    GoalDTO getUserGoal(String userId);
    GoalDTO updateUserMacronutrientGoal(UpdateMacronutrientGoalDTO updateMacronutrientGoalDTO, String userId);
    GoalDTO updateUserWaterIntakeGoal(UpdateWaterIntakeGoalDTO updateWaterIntakeGoalDTO, String userId);
}
