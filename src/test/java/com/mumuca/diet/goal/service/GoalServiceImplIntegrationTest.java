package com.mumuca.diet.goal.service;

import com.mumuca.diet.auth.model.User;
import com.mumuca.diet.auth.repository.UserRepository;
import com.mumuca.diet.dto.goal.GoalDTO;
import com.mumuca.diet.dto.goal.UpdateMacronutrientGoalDTO;
import com.mumuca.diet.dto.goal.UpdateWaterIntakeGoalDTO;
import com.mumuca.diet.exception.UserNotRegisteredYetException;
import com.mumuca.diet.model.Goal;
import com.mumuca.diet.model.GoalType;
import com.mumuca.diet.repository.GoalRepository;
import com.mumuca.diet.service.GoalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.mumuca.diet.testutil.EntityGeneratorUtil.createUser;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("GoalServiceImpl Integration Tests")
public class GoalServiceImplIntegrationTest {

    @Autowired
    private GoalService sut;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("getUserGoal tests")
    class GetUserGoalTests {

        @Test
        @Transactional
        @DisplayName("should be able to get user goal")
        void shouldBeAbleToGetUserGoal() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Goal goal = new Goal();
            goal.setUser(user);
            goal.setTargetCalories(2500);
            goal.setGoalType(GoalType.LOSE_WEIGHT);
            goal.setProteinTarget(BigDecimal.valueOf(100));
            goal.setCarbsTarget(BigDecimal.valueOf(300));
            goal.setFatTarget(BigDecimal.valueOf(70));
            goal.setTargetWeight(BigDecimal.valueOf(70));
            goal.setWaterIntakeTarget(BigDecimal.valueOf(3));
            goal.setDeadline(LocalDate.now().plusMonths(6));
            goalRepository.save(goal);

            // Act
            GoalDTO result = sut.getUserGoal(user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(goal.getId());
            assertThat(result.targetCalories()).isEqualTo(goal.getTargetCalories());
            assertThat(result.macronutrientsTarget().protein()).isEqualTo(goal.getProteinTarget().floatValue());
            assertThat(result.macronutrientsTarget().carbs()).isEqualTo(goal.getCarbsTarget().floatValue());
            assertThat(result.macronutrientsTarget().fat()).isEqualTo(goal.getFatTarget().floatValue());
            assertThat(result.targetWeight()).isEqualTo(goal.getTargetWeight());
            assertThat(result.waterIntakeTarget()).isEqualTo(goal.getWaterIntakeTarget());
            assertThat(result.deadline()).isEqualTo(goal.getDeadline());
        }

        @Test
        @Transactional
        @DisplayName("should throw UserNotRegisteredYetException when user goal does not exist")
        void shouldThrowUserNotRegisteredYetExceptionWhenUserGoalDoesNotExist() {
            // Act & Assert
            assertThatThrownBy(() -> sut.getUserGoal(randomUUID().toString()))
                    .isInstanceOf(UserNotRegisteredYetException.class)
                    .hasMessage("Registration not complete.");
        }
    }

    @Nested
    @DisplayName("updateUserMacronutrientGoal tests")
    class UpdateUserMacronutrientGoalTests {

        @Test
        @Transactional
        @DisplayName("should be able to update user macronutrient goal")
        void shouldBeAbleToUpdateUserMacronutrientGoal() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Goal goal = new Goal();
            goal.setUser(user);
            goal.setTargetCalories(2500);
            goal.setGoalType(GoalType.LOSE_WEIGHT);
            goal.setProteinTarget(BigDecimal.valueOf(100));
            goal.setCarbsTarget(BigDecimal.valueOf(300));
            goal.setFatTarget(BigDecimal.valueOf(70));
            goal.setTargetWeight(BigDecimal.valueOf(70));
            goal.setWaterIntakeTarget(BigDecimal.valueOf(3));
            goal.setDeadline(LocalDate.now().plusMonths(6));
            goalRepository.save(goal);

            UpdateMacronutrientGoalDTO updateMacronutrientGoalDTO = new UpdateMacronutrientGoalDTO(
                    BigDecimal.valueOf(120),
                    BigDecimal.valueOf(350),
                    BigDecimal.valueOf(80)
            );

            // Act
            GoalDTO result = sut.updateUserMacronutrientGoal(updateMacronutrientGoalDTO, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(goal.getId());
            assertThat(result.goalType()).isEqualTo(goal.getGoalType());
            assertThat(result.targetCalories()).isEqualTo(goal.getTargetCalories());
            assertThat(result.targetWeight()).isEqualTo(goal.getTargetWeight());
            assertThat(result.waterIntakeTarget()).isEqualTo(goal.getWaterIntakeTarget());
            assertThat(result.deadline()).isEqualTo(goal.getDeadline());
            assertThat(result.macronutrientsTarget().carbs()).isEqualTo(updateMacronutrientGoalDTO.carbsTarget().floatValue());
            assertThat(result.macronutrientsTarget().protein()).isEqualTo(updateMacronutrientGoalDTO.proteinTarget().floatValue());
            assertThat(result.macronutrientsTarget().fat()).isEqualTo(updateMacronutrientGoalDTO.fatTarget().floatValue());
        }

        @Test
        @Transactional
        @DisplayName("should throw UserNotRegisteredYetException when updating goal for non-existent user")
        void shouldThrowUserNotRegisteredYetExceptionWhenUpdatingGoalForNonExistentUser() {
            // Arrange
            UpdateMacronutrientGoalDTO updateDTO = new UpdateMacronutrientGoalDTO(BigDecimal.valueOf(350), BigDecimal.valueOf(120), BigDecimal.valueOf(80));

            // Act & Assert
            assertThatThrownBy(() -> sut.updateUserMacronutrientGoal(updateDTO, randomUUID().toString()))
                    .isInstanceOf(UserNotRegisteredYetException.class)
                    .hasMessage("Registration not complete.");
        }
    }

    @Nested
    @DisplayName("updateUserWaterIntakeGoal tests")
    class UpdateUserWaterIntakeGoalTests {

        @Test
        @Transactional
        @DisplayName("should be able to update user water intake goal")
        void shouldBeAbleToUpdateUserWaterIntakeGoal() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Goal goal = new Goal();
            goal.setUser(user);
            goal.setGoalType(GoalType.LOSE_WEIGHT);
            goal.setTargetCalories(2500);
            goal.setProteinTarget(BigDecimal.valueOf(100));
            goal.setCarbsTarget(BigDecimal.valueOf(300));
            goal.setFatTarget(BigDecimal.valueOf(70));
            goal.setTargetWeight(BigDecimal.valueOf(70));
            goal.setWaterIntakeTarget(BigDecimal.valueOf(3));
            goal.setDeadline(LocalDate.now().plusMonths(6));
            goalRepository.save(goal);

            UpdateWaterIntakeGoalDTO updateWaterIntakeGoalDTO = new UpdateWaterIntakeGoalDTO(BigDecimal.valueOf(4));

            // Act
            GoalDTO result = sut.updateUserWaterIntakeGoal(updateWaterIntakeGoalDTO, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(goal.getId());
            assertThat(result.goalType()).isEqualTo(goal.getGoalType());
            assertThat(result.targetCalories()).isEqualTo(goal.getTargetCalories());
            assertThat(result.targetWeight()).isEqualTo(goal.getTargetWeight());
            assertThat(result.waterIntakeTarget()).isEqualTo(updateWaterIntakeGoalDTO.waterIntakeGoal());
            assertThat(result.deadline()).isEqualTo(goal.getDeadline());
            assertThat(result.macronutrientsTarget().carbs()).isEqualTo(goal.getCarbsTarget().floatValue());
            assertThat(result.macronutrientsTarget().protein()).isEqualTo(goal.getProteinTarget().floatValue());
            assertThat(result.macronutrientsTarget().fat()).isEqualTo(goal.getFatTarget().floatValue());
        }

        @Test
        @Transactional
        @DisplayName("should throw UserNotRegisteredYetException when updating water intake goal for non-existent user")
        void shouldThrowUserNotRegisteredYetExceptionWhenUpdatingWaterIntakeGoalForNonExistentUser() {
            // Arrange
            UpdateWaterIntakeGoalDTO updateDTO = new UpdateWaterIntakeGoalDTO(BigDecimal.valueOf(4));

            // Act & Assert
            assertThatThrownBy(() -> sut.updateUserWaterIntakeGoal(updateDTO, randomUUID().toString()))
                    .isInstanceOf(UserNotRegisteredYetException.class)
                    .hasMessage("Registration not complete.");
        }
    }
}
