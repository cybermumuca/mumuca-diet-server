package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.CompleteRegistrationDTO;
import com.mumuca.diet.dto.RegistrationCompletedDTO;
import com.mumuca.diet.exception.UserAlreadyRegisteredException;
import com.mumuca.diet.model.*;
import com.mumuca.diet.repository.BodyRepository;
import com.mumuca.diet.repository.GoalRepository;
import com.mumuca.diet.repository.ProfileRepository;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

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

        profileRepository.save(profile);

        Goal goal = new Goal();

        goal.setUser(user);
        goal.setGoalType(completeRegistrationDTO.goal());
        goal.setTargetWeight(completeRegistrationDTO.targetWeight());

        int targetCalories = this.calculateTargetCalories(
                completeRegistrationDTO.goal(),
                body,
                profile,
                completeRegistrationDTO.activityLevel()
        );

        goal.setTargetCalories(targetCalories);

        this.setMacronutrientAndWaterTargets(goal, body);

        LocalDate deadLine = this.calculateDeadline(
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

    private int calculateTargetCalories(
            GoalType goalType,
            Body body,
            Profile profile,
            ActivityLevel activityLevel
    ) {
        BigDecimal bmr;

        int age = profile.getAge();

        if (profile.getGender() == Gender.MALE) {
            bmr = BigDecimal.valueOf(88.362)
                    .add(BigDecimal.valueOf(13.397).multiply(body.getWeight()))
                    .add(BigDecimal.valueOf(4.799).multiply(body.getHeight().multiply(BigDecimal.valueOf(100))))
                    .subtract(BigDecimal.valueOf(5.677).multiply(BigDecimal.valueOf(age)));
        } else {
            bmr = BigDecimal.valueOf(447.6)
                    .add(BigDecimal.valueOf(9.2).multiply(body.getWeight()))
                    .add(BigDecimal.valueOf(3.1).multiply(body.getHeight().multiply(BigDecimal.valueOf(100))))
                    .subtract(BigDecimal.valueOf(4.3).multiply(BigDecimal.valueOf(age)));
        }

        BigDecimal activityFactor = switch (activityLevel) {
            case SEDENTARY -> BigDecimal.valueOf(1.2);
            case LIGHTLY_ACTIVE -> BigDecimal.valueOf(1.375);
            case MODERATELY_ACTIVE -> BigDecimal.valueOf(1.55);
            case VERY_ACTIVE -> BigDecimal.valueOf(1.725);
            case EXTRA_ACTIVE -> BigDecimal.valueOf(1.9);
        };

        BigDecimal totalCalories = bmr.multiply(activityFactor);

        BigDecimal adjustedCalories = switch (goalType) {
            case LOSE_WEIGHT -> totalCalories.multiply(BigDecimal.valueOf(0.8));
            case GAIN_WEIGHT -> totalCalories.multiply(BigDecimal.valueOf(1.2));
            case MAINTAIN_WEIGHT -> totalCalories;
        };

        return adjustedCalories.setScale(0, RoundingMode.HALF_UP).intValue();
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

    private LocalDate calculateDeadline(GoalType goalType, BigDecimal currentWeight, BigDecimal targetWeight) {
        long weeksToAchieveGoal;

        switch (goalType) {
            case LOSE_WEIGHT -> {
                // Perda de peso: 0.75 kg/semana
                BigDecimal weightToLose = currentWeight.subtract(targetWeight);
                weeksToAchieveGoal = weightToLose.divide(BigDecimal.valueOf(0.75), RoundingMode.CEILING).longValue();
            }
            case GAIN_WEIGHT -> {
                // Ganho de peso: 0.375 kg/semana
                BigDecimal weightToGain = targetWeight.subtract(currentWeight);
                weeksToAchieveGoal = weightToGain.divide(BigDecimal.valueOf(0.375), RoundingMode.CEILING).longValue();
            }
            default -> {
                // Manutenção de peso: Prazo padrão (ex: 6 meses)
                weeksToAchieveGoal = 26;
            }
        }

        // Calcula a data final com base nas semanas
        return LocalDate.now().plusWeeks(weeksToAchieveGoal);
    }

}
