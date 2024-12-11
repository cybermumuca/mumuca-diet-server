package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.MacronutrientDTO;
import com.mumuca.diet.dto.goal.GoalDTO;
import com.mumuca.diet.dto.goal.UpdateMacronutrientGoalDTO;
import com.mumuca.diet.dto.goal.UpdateWaterIntakeGoalDTO;
import com.mumuca.diet.exception.UserNotRegisteredYetException;
import com.mumuca.diet.repository.GoalRepository;
import com.mumuca.diet.service.GoalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mumuca.diet.util.UpdateUtils.updateIfDifferent;

@Service
@AllArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    @Override
    public GoalDTO getUserGoal(String userId) {
        return goalRepository.findByUserId(userId)
                .map(goal -> new GoalDTO(
                        goal.getId(),
                        goal.getGoalType(),
                        goal.getTargetCalories(),
                        MacronutrientDTO.builder()
                                .protein(goal.getProteinTarget().floatValue())
                                .carbs(goal.getCarbsTarget().floatValue())
                                .fat(goal.getFatTarget().floatValue())
                                .build(),
                        goal.getTargetWeight(),
                        goal.getWaterIntakeTarget(),
                        goal.getDeadline())
                )
                .orElseThrow(() -> new UserNotRegisteredYetException("Registration not complete."));
    }

    @Override
    public GoalDTO updateUserMacronutrientGoal(
            UpdateMacronutrientGoalDTO updateMacronutrientGoalDTO,
            String userId
    ) {
        return goalRepository.findByUserId(userId)
                .map(goalToUpdate -> {

                    boolean updated = false;

                    updated |= updateIfDifferent(
                            goalToUpdate::getCarbsTarget,
                            goalToUpdate::setCarbsTarget,
                            updateMacronutrientGoalDTO.carbsTarget()
                    );

                    updated |= updateIfDifferent(
                            goalToUpdate::getProteinTarget,
                            goalToUpdate::setProteinTarget,
                            updateMacronutrientGoalDTO.proteinTarget()
                    );

                    updated |= updateIfDifferent(
                            goalToUpdate::getFatTarget,
                            goalToUpdate::setFatTarget,
                            updateMacronutrientGoalDTO.fatTarget()
                    );


                    if (updated) {
                        goalRepository.save(goalToUpdate);
                    }

                    return new GoalDTO(
                            goalToUpdate.getId(),
                            goalToUpdate.getGoalType(),
                            goalToUpdate.getTargetCalories(),
                            MacronutrientDTO.builder()
                                    .protein(goalToUpdate.getProteinTarget().floatValue())
                                    .carbs(goalToUpdate.getCarbsTarget().floatValue())
                                    .fat(goalToUpdate.getFatTarget().floatValue())
                                    .build(),
                            goalToUpdate.getTargetWeight(),
                            goalToUpdate.getWaterIntakeTarget(),
                            goalToUpdate.getDeadline()
                    );
                })
                .orElseThrow(() -> new UserNotRegisteredYetException("Registration not complete."));
    }

    @Override
    public GoalDTO updateUserWaterIntakeGoal(UpdateWaterIntakeGoalDTO updateWaterIntakeGoalDTO, String userId) {
        return goalRepository.findByUserId(userId)
                .map(goal -> {
                    goal.setWaterIntakeTarget(updateWaterIntakeGoalDTO.waterIntakeGoal());

                    goalRepository.save(goal);

                    return new GoalDTO(
                            goal.getId(),
                            goal.getGoalType(),
                            goal.getTargetCalories(),
                            MacronutrientDTO.builder()
                                    .protein(goal.getProteinTarget().floatValue())
                                    .carbs(goal.getCarbsTarget().floatValue())
                                    .fat(goal.getFatTarget().floatValue())
                                    .build(),
                            goal.getTargetWeight(),
                            goal.getWaterIntakeTarget(),
                            goal.getDeadline()
                    );
                })
                .orElseThrow(() -> new UserNotRegisteredYetException("Registration not complete."));
    }
}
