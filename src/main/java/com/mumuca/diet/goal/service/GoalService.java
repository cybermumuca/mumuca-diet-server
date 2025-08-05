package com.mumuca.diet.goal.service;

import com.mumuca.diet.goal.dto.GoalDTO;
import com.mumuca.diet.goal.dto.UpdateMacronutrientGoalDTO;
import com.mumuca.diet.goal.dto.UpdateWaterIntakeGoalDTO;
import jakarta.validation.Valid;

public interface GoalService {
    GoalDTO getUserGoal(String userId);
    GoalDTO updateUserMacronutrientGoal(UpdateMacronutrientGoalDTO updateMacronutrientGoalDTO, String userId);
    GoalDTO updateUserWaterIntakeGoal(UpdateWaterIntakeGoalDTO updateWaterIntakeGoalDTO, String userId);
}
