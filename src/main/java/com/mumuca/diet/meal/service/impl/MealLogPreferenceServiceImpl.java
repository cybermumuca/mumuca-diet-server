package com.mumuca.diet.meal.service.impl;

import com.mumuca.diet.meal.dto.CreateMealLogPreferenceDTO;
import com.mumuca.diet.meal.dto.MealLogPreferenceDTO;
import com.mumuca.diet.meal.dto.UpdateMealLogPreferenceDTO;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.exception.UniqueMealLogPreferenceException;
import com.mumuca.diet.exception.UserNotRegisteredYetException;
import com.mumuca.diet.meal.model.MealLogPreference;
import com.mumuca.diet.meal.model.MealType;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.GoalRepository;
import com.mumuca.diet.meal.repository.MealLogPreferenceRepository;
import com.mumuca.diet.meal.service.MealLogPreferenceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.mumuca.diet.util.UpdateUtils.updateIfDifferent;

@Service
@AllArgsConstructor
public class MealLogPreferenceServiceImpl implements MealLogPreferenceService {

    // TODO: I don't know if this proportion is ideal, I'll fix it later.
    private static final Map<MealType, Double> MEAL_TYPE_PROPORTIONS = Map.of(
            MealType.BREAKFAST, 0.2,         // 20%
            MealType.BRUNCH, 0.1,            // 10%
            MealType.LUNCH, 0.3,             // 30%
            MealType.AFTERNOON_SNACK, 0.1,   // 10%
            MealType.DINNER, 0.2,            // 20%
            MealType.SUPPER, 0.05,           // 5%
            MealType.SNACK, 0.05,            // 5%
            MealType.PRE_WORKOUT, 0.05,      // 5%
            MealType.POST_WORKOUT, 0.05,     // 5%
            MealType.MIDNIGHT_SNACK, 0.05       // 5%
    );

    private final MealLogPreferenceRepository mealLogPreferenceRepository;
    private final GoalRepository goalRepository;

    private int calculateCaloriesForMeal(MealType mealType, int targetCalories) {
        double proportion = MEAL_TYPE_PROPORTIONS.getOrDefault(mealType, 0.0);
        return (int) Math.round(targetCalories * proportion);
    }

    @Override
    @Transactional
    public List<MealLogPreferenceDTO> createMealLogPreference(List<CreateMealLogPreferenceDTO> createMealLogPreferenceDTOList, String userId) {
        if (createMealLogPreferenceDTOList == null || createMealLogPreferenceDTOList.isEmpty()) {
            return List.of();
        }

        var user = new User(userId);

        int userTargetCalories = goalRepository.findTargetCaloriesByUserId(userId)
                .orElseThrow(() -> new UserNotRegisteredYetException("Goal not found."));

        return createMealLogPreferenceDTOList
                .stream()
                .filter(createMealLogPreferenceDTO -> {
                    if (createMealLogPreferenceDTO.type() == null) {
                        return false;
                    }

                    return createMealLogPreferenceDTO.time() != null;
                })
                .map(createMealLogPreferenceDTO -> {
                    var mealLogPreferenceWithSameTypeExists = mealLogPreferenceRepository
                            .existsByTypeAndUserId(createMealLogPreferenceDTO.type(), userId);

                    if (mealLogPreferenceWithSameTypeExists) {
                        throw new UniqueMealLogPreferenceException("There is already a preference with the same type");
                    }

                    var mealLogPreferenceToSave = new MealLogPreference();

                    mealLogPreferenceToSave.setUser(user);
                    mealLogPreferenceToSave.setType(createMealLogPreferenceDTO.type());
                    mealLogPreferenceToSave.setTime(createMealLogPreferenceDTO.time());

                    int caloriesForMeal = this.calculateCaloriesForMeal(
                            createMealLogPreferenceDTO.type(),
                            userTargetCalories
                    );

                    mealLogPreferenceToSave.setCaloriesGoal(caloriesForMeal);

                    mealLogPreferenceRepository.save(mealLogPreferenceToSave);

                    return new MealLogPreferenceDTO(
                            mealLogPreferenceToSave.getId(),
                            mealLogPreferenceToSave.getType(),
                            mealLogPreferenceToSave.getTime(),
                            mealLogPreferenceToSave.getCaloriesGoal()
                    );
                })
                .toList();
    }

    @Override
    public MealLogPreferenceDTO updateMealLogPreference(String mealLogPreferenceId, UpdateMealLogPreferenceDTO updateMealLogPreferenceDTO, String userId) {
        MealLogPreference mealLogPreferenceToUpdate = mealLogPreferenceRepository
                .findMealLogPreferenceByIdAndUserId(mealLogPreferenceId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log Preference not found."));

        boolean updated = false;

        updated |= updateIfDifferent(
                mealLogPreferenceToUpdate::getTime,
                mealLogPreferenceToUpdate::setTime,
                updateMealLogPreferenceDTO.time()
        );

        updated |= updateIfDifferent(
                mealLogPreferenceToUpdate::getType,
                mealLogPreferenceToUpdate::setType,
                updateMealLogPreferenceDTO.type()
        );

        updated |= updateIfDifferent(
                mealLogPreferenceToUpdate::getCaloriesGoal,
                mealLogPreferenceToUpdate::setCaloriesGoal,
                updateMealLogPreferenceDTO.caloriesGoal()
        );

        if (updated) {
            mealLogPreferenceRepository.save(mealLogPreferenceToUpdate);
        }

        return new MealLogPreferenceDTO(
                mealLogPreferenceToUpdate.getId(),
                mealLogPreferenceToUpdate.getType(),
                mealLogPreferenceToUpdate.getTime(),
                mealLogPreferenceToUpdate.getCaloriesGoal()
        );
    }

    @Override
    public void deleteMealLogPreference(String mealLogPreferenceId, String userId) {
        MealLogPreference mealLogPreferenceToDelete = mealLogPreferenceRepository.findMealLogPreferenceByIdAndUserId(mealLogPreferenceId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Log Preference not found."));

        mealLogPreferenceRepository.deleteById(mealLogPreferenceToDelete.getId());
    }

    @Override
    public List<MealLogPreferenceDTO> getUserMealLogPreferences(String userId) {
        return mealLogPreferenceRepository.findAllMealLogPreferencesByUserId(userId)
                .stream()
                .map((mealLogPreference) -> new MealLogPreferenceDTO(
                        mealLogPreference.getId(),
                        mealLogPreference.getType(),
                        mealLogPreference.getTime(),
                        mealLogPreference.getCaloriesGoal()
                )).toList();
    }
}
