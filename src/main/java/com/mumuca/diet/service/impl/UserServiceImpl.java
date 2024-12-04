package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.BmiDTO;
import com.mumuca.diet.dto.CompleteRegistrationDTO;
import com.mumuca.diet.dto.DiagnosisDTO;
import com.mumuca.diet.dto.RegistrationCompletedDTO;
import com.mumuca.diet.exception.UserAlreadyRegisteredException;
import com.mumuca.diet.exception.UserNotRegisteredYetException;
import com.mumuca.diet.model.*;
import com.mumuca.diet.repository.BodyRepository;
import com.mumuca.diet.repository.GoalRepository;
import com.mumuca.diet.repository.ProfileRepository;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.service.UserService;
import com.mumuca.diet.util.NutritionalCalculator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static com.mumuca.diet.util.NutritionalCalculator.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BodyRepository bodyRepository;
    private final ProfileRepository profileRepository;
    private final GoalRepository goalRepository;

    @Override
    @Transactional
    public RegistrationCompletedDTO completeRegistration(
            CompleteRegistrationDTO completeRegistrationDTO,
            String userId
    ) {
        goalRepository.findByUserId(userId)
                .ifPresent(goal -> {
                    throw new UserAlreadyRegisteredException("User already registered.");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Body body = new Body();

        body.setHeight(completeRegistrationDTO.height());
        body.setWeight(completeRegistrationDTO.weight());
        body.setDate(LocalDate.now());
        body.setUser(user);

        bodyRepository.save(body);

        Profile profile = new Profile();

        profile.setBirthDate(completeRegistrationDTO.birthDate());
        profile.setGender(completeRegistrationDTO.gender());
        profile.setActivityLevel(completeRegistrationDTO.activityLevel());
        profile.setUser(user);

        user.setProfile(profile);

        userRepository.save(user);

        Goal goal = new Goal();

        goal.setUser(user);
        goal.setGoalType(completeRegistrationDTO.goal());
        goal.setTargetWeight(completeRegistrationDTO.targetWeight());

        int targetCalories = this.calculateTargetCalories(
                body.getWeight(),
                body.getHeight(),
                profile.getAge(),
                profile.getGender(),
                profile.getActivityLevel(),
                goal.getGoalType()
        );

        goal.setTargetCalories(targetCalories);

        this.setMacronutrientAndWaterTargets(goal, body);

        LocalDate deadLine = NutritionalCalculator.calculateDeadline(
                goal.getGoalType(),
                body.getWeight(),
                completeRegistrationDTO.targetWeight()
        );

        goal.setDeadline(deadLine);

        goalRepository.save(goal);

        return new RegistrationCompletedDTO(
                body.getWeight().floatValue(),
                body.getHeight().floatValue(),
                profile.getAge(),
                profile.getGender(),
                goal.getGoalType(),
                profile.getActivityLevel(),
                goal.getTargetWeight().floatValue(),
                goal.getTargetCalories(),
                goal.getProteinTarget().floatValue(),
                goal.getCarbsTarget().floatValue(),
                goal.getFatTarget().floatValue(),
                goal.getWaterIntakeTarget().floatValue(),
                goal.getDeadline()
        );
    }

    @Override
    public DiagnosisDTO generateDiagnosis(String userId) {
        Body body = bodyRepository.findFirstByUserIdOrderByDateDesc(userId)
                        .orElseThrow(() -> new UserNotRegisteredYetException("Body not found."));

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotRegisteredYetException("Profile not found."));

        BigDecimal bmiValue = calculateBMI(body.getWeight(), body.getHeight());
        String bmiClassification = classifyBMI(bmiValue);
        var bmi = new BmiDTO(bmiValue.floatValue(), bmiClassification);

        var idealWeight = calculateIdealWeight(body.getHeight());
        String fatRate = "%.1f%%".formatted(calculateBodyFat(body.getWeight(), body.getHeight(), profile.getAge(), profile.getGender()));

        return new DiagnosisDTO(
                bmi,
                idealWeight.getFirst().floatValue(),
                idealWeight.getLast().floatValue(),
                fatRate
        );
    }

    private int calculateTargetCalories(
            BigDecimal weight,
            BigDecimal height,
            int age,
            Gender gender,
            ActivityLevel activityLevel,
            GoalType goalType
    ) {
        BigDecimal bmr = NutritionalCalculator.calculateBMR(
                weight,
                height,
                age,
                gender
        );

        BigDecimal caloriesAdjustedForActivity = NutritionalCalculator.adjustCaloriesForActivity(bmr, activityLevel);
        return NutritionalCalculator.adjustCaloriesForGoal(caloriesAdjustedForActivity, goalType).intValue();
    }

    // TODO: Improve this
    private void setMacronutrientAndWaterTargets(Goal goal, Body body) {
        // Conversão de calorias para gramas de macronutrientes
        BigDecimal calories = BigDecimal.valueOf(goal.getTargetCalories());

        BigDecimal proteinCalories = calories.multiply(BigDecimal.valueOf(0.25)); // 25% calorias para proteínas
        BigDecimal carbsCalories = calories.multiply(BigDecimal.valueOf(0.45));  // 45% calorias para carboidratos
        BigDecimal fatCalories = calories.multiply(BigDecimal.valueOf(0.30));    // 30% calorias para gorduras

        // 1 grama de proteína = 4 calorias
        goal.setProteinTarget(proteinCalories.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP));
        // 1 grama de carboidratos = 4 calorias
        goal.setCarbsTarget(carbsCalories.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP));
        // 1 grama de gordura = 9 calorias
        goal.setFatTarget(fatCalories.divide(BigDecimal.valueOf(9), 2, RoundingMode.HALF_UP));

        // Cálculo da ingestão de água (litros)
        BigDecimal waterInLiters = body.getWeight()
                .multiply(BigDecimal.valueOf(35))
                .divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);

        goal.setWaterIntakeTarget(waterInLiters);
    }
}
