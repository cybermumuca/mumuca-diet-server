package com.mumuca.diet.meal.service;

import com.mumuca.diet.dto.meallogpreferences.CreateMealLogPreferenceDTO;
import com.mumuca.diet.dto.meallogpreferences.MealLogPreferenceDTO;
import com.mumuca.diet.dto.meallogpreferences.UpdateMealLogPreferenceDTO;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.exception.UniqueMealLogPreferenceException;
import com.mumuca.diet.exception.UserNotRegisteredYetException;
import com.mumuca.diet.model.*;
import com.mumuca.diet.repository.GoalRepository;
import com.mumuca.diet.repository.MealLogPreferenceRepository;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.service.impl.MealLogPreferenceServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static com.mumuca.diet.testutil.EntityGeneratorUtil.createGoal;
import static com.mumuca.diet.testutil.EntityGeneratorUtil.createUser;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("MealLogPreferenceServiceImpl Integration Tests")
public class MealLogPreferenceServiceImplIntegrationTest {

    @Autowired
    private MealLogPreferenceServiceImpl sut;

    @Autowired
    private MealLogPreferenceRepository mealLogPreferenceRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("createMealLogPreference tests")
    class CreateMealLogPreferenceTests {

        @Test
        @Transactional
        @DisplayName("should create meal log preferences successfully")
        void shouldCreateMealLogPreferencesSuccessfully() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Goal goal = createGoal();
            goal.setUser(user);

            goalRepository.save(goal);

            List<CreateMealLogPreferenceDTO> createDTOs = List.of(
                    new CreateMealLogPreferenceDTO(MealType.BREAKFAST, LocalTime.of(8, 0)),
                    new CreateMealLogPreferenceDTO(MealType.LUNCH, LocalTime.of(12, 0))
            );

            // Act
            List<MealLogPreferenceDTO> result = sut.createMealLogPreference(createDTOs, user.getId());

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(MealLogPreferenceDTO::type, MealLogPreferenceDTO::time)
                    .containsExactlyInAnyOrder(
                            tuple(MealType.BREAKFAST, LocalTime.of(8, 0)),
                            tuple(MealType.LUNCH, LocalTime.of(12, 0))
                    );

            var mealLogResult1 = result.getFirst();

            var mealLogPreferenceInDatabase1 = mealLogPreferenceRepository
                    .findById(mealLogResult1.id())
                    .orElseThrow();

            assertThat(mealLogPreferenceInDatabase1.getType()).isEqualTo(mealLogResult1.type());
            assertThat(mealLogPreferenceInDatabase1.getTime()).isEqualTo(mealLogResult1.time());
            assertThat(mealLogPreferenceInDatabase1.getCaloriesGoal()).isEqualTo(mealLogResult1.caloriesGoal());

            var mealLogResult2 = result.getLast();

            var mealLogPreferenceInDatabase2 = mealLogPreferenceRepository.findById(mealLogResult2.id()).orElseThrow();

            assertThat(mealLogPreferenceInDatabase2.getType()).isEqualTo(mealLogResult2.type());
            assertThat(mealLogPreferenceInDatabase2.getTime()).isEqualTo(mealLogResult2.time());
            assertThat(mealLogPreferenceInDatabase2.getCaloriesGoal()).isEqualTo(mealLogResult2.caloriesGoal());
        }

        @Test
        @Transactional
        @DisplayName("should return empty list if input list is null or empty")
        void shouldReturnEmptyListIfInputListIsNullOrEmpty() {
            // Act & Assert
            assertThat(sut.createMealLogPreference(null, randomUUID().toString())).isEmpty();
            assertThat(sut.createMealLogPreference(List.of(), randomUUID().toString())).isEmpty();
        }

        @Test
        @Transactional
        @DisplayName("should throw UniqueMealLogPreferenceException if meal type already exists")
        void shouldThrowUniqueMealLogPreferenceExceptionIfMealTypeAlreadyExists() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            MealLogPreference mealLogPreference = MealLogPreference.builder()
                    .time(LocalTime.of(12, 0))
                    .caloriesGoal(900)
                    .type(MealType.LUNCH)
                    .user(user)
                    .build();

            mealLogPreferenceRepository.save(mealLogPreference);

            Goal goal = createGoal();
            goal.setUser(user);

            goalRepository.save(goal);

            List<CreateMealLogPreferenceDTO> createDTOs = List.of(
                    new CreateMealLogPreferenceDTO(MealType.LUNCH, LocalTime.of(12, 0))
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.createMealLogPreference(createDTOs, user.getId()))
                    .isInstanceOf(UniqueMealLogPreferenceException.class)
                    .hasMessageContaining("There is already a preference with the same type");
        }

        @Test
        @Transactional
        @DisplayName("should throw UserNotRegisteredYetException if target calories not found")
        void shouldThrowUserNotRegisteredYetExceptionIfTargetCaloriesNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            List<CreateMealLogPreferenceDTO> createDTOs = List.of(
                    new CreateMealLogPreferenceDTO(MealType.LUNCH, LocalTime.of(12, 0))
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.createMealLogPreference(createDTOs, user.getId()))
                    .isInstanceOf(UserNotRegisteredYetException.class)
                    .hasMessageContaining("Goal not found.");
        }
    }

    @Nested
    @DisplayName("updateMealLogPreference tests")
    class UpdateMealLogPreferenceTests {

        @Test
        @Transactional
        @DisplayName("should be able to update meal log preference")
        void shouldBeAbleToUpdateMealLogPreference() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            MealLogPreference mealLogPreference = MealLogPreference.builder()
                    .time(LocalTime.of(12, 0))
                    .caloriesGoal(900)
                    .type(MealType.LUNCH)
                    .user(user)
                    .build();

            mealLogPreferenceRepository.save(mealLogPreference);

            UpdateMealLogPreferenceDTO updateMealLogPreferenceDTO = new UpdateMealLogPreferenceDTO(
                    MealType.DINNER,
                    LocalTime.of(13, 0),
                    700
            );

            // Act
            MealLogPreferenceDTO result = sut.updateMealLogPreference(
                    mealLogPreference.getId(), updateMealLogPreferenceDTO, user.getId()
            );

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(mealLogPreference.getId());
            assertThat(result.time()).isEqualTo(updateMealLogPreferenceDTO.time());
            assertThat(result.type()).isEqualTo(updateMealLogPreferenceDTO.type());
            assertThat(result.caloriesGoal()).isEqualTo(updateMealLogPreferenceDTO.caloriesGoal());

            var mealLogPreferenceInDatabase = mealLogPreferenceRepository
                    .findById(mealLogPreference.getId())
                    .orElseThrow();

            assertThat(mealLogPreferenceInDatabase.getId()).isEqualTo(mealLogPreference.getId());
            assertThat(mealLogPreferenceInDatabase.getType()).isEqualTo(updateMealLogPreferenceDTO.type());
            assertThat(mealLogPreferenceInDatabase.getTime()).isEqualTo(updateMealLogPreferenceDTO.time());
            assertThat(mealLogPreferenceInDatabase.getCaloriesGoal()).isEqualTo(updateMealLogPreferenceDTO.caloriesGoal());
            assertThat(mealLogPreferenceInDatabase.getUser().getId()).isEqualTo(user.getId());

        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log preference does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealLogPreferenceDoesNotExist() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            UpdateMealLogPreferenceDTO updateMealLogPreferenceDTO = new UpdateMealLogPreferenceDTO(
                    MealType.DINNER,
                    LocalTime.of(13, 0),
                    700
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.updateMealLogPreference(randomUUID().toString(), updateMealLogPreferenceDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log Preference not found.");
        }

        @Test
        @Transactional
        @DisplayName("should not update meal log preference if values are unchanged")
        void shouldNotUpdateMealLogPreferenceIfValuesAreUnchanged() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            MealLogPreference mealLogPreference = MealLogPreference.builder()
                    .time(LocalTime.of(12, 0))
                    .caloriesGoal(900)
                    .type(MealType.LUNCH)
                    .user(user)
                    .build();

            mealLogPreferenceRepository.save(mealLogPreference);

            UpdateMealLogPreferenceDTO updateMealLogPreferenceDTO = new UpdateMealLogPreferenceDTO(
                    mealLogPreference.getType(),
                    mealLogPreference.getTime(),
                    mealLogPreference.getCaloriesGoal()
            );

            // Act
            MealLogPreferenceDTO updatedPreference = sut.updateMealLogPreference(
                    mealLogPreference.getId(),
                    updateMealLogPreferenceDTO,
                    user.getId()
            );

            // Assert
            assertThat(updatedPreference).isNotNull();
            assertThat(updatedPreference.time()).isEqualTo(mealLogPreference.getTime());
            assertThat(updatedPreference.type()).isEqualTo(mealLogPreference.getType());
            assertThat(updatedPreference.caloriesGoal()).isEqualTo(mealLogPreference.getCaloriesGoal());

            var notUpdatedMealLogPreferenceInDatabase = mealLogPreferenceRepository.findById(mealLogPreference.getId()).orElseThrow();

            assertThat(notUpdatedMealLogPreferenceInDatabase.getId()).isEqualTo(mealLogPreference.getId());
            assertThat(notUpdatedMealLogPreferenceInDatabase.getType()).isEqualTo(mealLogPreference.getType());
            assertThat(notUpdatedMealLogPreferenceInDatabase.getTime()).isEqualTo(mealLogPreference.getTime());
            assertThat(notUpdatedMealLogPreferenceInDatabase.getCaloriesGoal()).isEqualTo(mealLogPreference.getCaloriesGoal());
            assertThat(notUpdatedMealLogPreferenceInDatabase.getUser().getId()).isEqualTo(user.getId());
        }
    }

    @Nested
    @DisplayName("deleteMealLogPreference tests")
    class DeleteMealLogPreferenceTests {

        @Test
        @Transactional
        @DisplayName("should be able to delete meal log preference")
        void shouldBeAbleToDeleteMealLogPreference() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            MealLogPreference mealLogPreference = MealLogPreference.builder()
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .type(MealType.LUNCH)
                    .user(user)
                    .build();

            mealLogPreferenceRepository.save(mealLogPreference);

            // Act
            sut.deleteMealLogPreference(mealLogPreference.getId(), user.getId());

            // Assert
            assertThat(mealLogPreferenceRepository.findById(mealLogPreference.getId())).isEmpty();
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log preference does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealLogPreferenceDoesNotExist() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            // Act & Assert
            assertThatThrownBy(() -> sut.deleteMealLogPreference(randomUUID().toString(), user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log Preference not found.");
        }
    }

    @Nested
    @DisplayName("getUserMealLogPreferences tests")
    class GetUserMealLogPreferencesTests {
        @Test
        @Transactional
        @DisplayName("should be able to get user meal log preferences")
        void shouldBeAbleToGetUserMealLogPreferences() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            MealLogPreference mealLogPreference1 = MealLogPreference.builder()
                    .time(LocalTime.of(12, 0))
                    .caloriesGoal(900)
                    .type(MealType.LUNCH)
                    .user(user)
                    .build();

            MealLogPreference mealLogPreference2 = MealLogPreference.builder()
                    .time(LocalTime.of(19, 0))
                    .caloriesGoal(450)
                    .type(MealType.DINNER)
                    .user(user)
                    .build();

            mealLogPreferenceRepository.saveAll(List.of(mealLogPreference1, mealLogPreference2));

            // Act
            List<MealLogPreferenceDTO> mealLogPreferences = sut.getUserMealLogPreferences(user.getId());

            // Assert
            assertThat(mealLogPreferences)
                    .hasSize(2)
                    .extracting(MealLogPreferenceDTO::id,
                            MealLogPreferenceDTO::type,
                            MealLogPreferenceDTO::time,
                            MealLogPreferenceDTO::caloriesGoal)
                    .containsExactly(
                            tuple(mealLogPreference1.getId(), mealLogPreference1.getType(), mealLogPreference1.getTime(), mealLogPreference1.getCaloriesGoal()),
                            tuple(mealLogPreference2.getId(), mealLogPreference2.getType(), mealLogPreference2.getTime(), mealLogPreference2.getCaloriesGoal())
                    );

        }
    }
}
