package com.mumuca.diet.meal.service;

import com.mumuca.diet.meal.dto.*;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.food.dto.FoodDTO;
import com.mumuca.diet.food.model.Food;
import com.mumuca.diet.food.model.NutritionalInformation;
import com.mumuca.diet.food.model.Portion;
import com.mumuca.diet.food.repository.FoodRepository;
import com.mumuca.diet.food.repository.NutritionalInformationRepository;
import com.mumuca.diet.food.repository.PortionRepository;
import com.mumuca.diet.meal.model.Meal;
import com.mumuca.diet.meal.model.MealLog;
import com.mumuca.diet.meal.model.MealType;
import com.mumuca.diet.model.User;
import com.mumuca.diet.meal.repository.MealLogRepository;
import com.mumuca.diet.meal.repository.MealRepository;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.meal.service.impl.MealLogServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.mumuca.diet.testutil.EntityGeneratorUtil.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("MealLogServiceImpl Integration Tests")
public class MealLogServiceImplIntegrationTest {

    @Autowired
    private MealLogServiceImpl sut;

    @Autowired
    private MealLogRepository mealLogRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private NutritionalInformationRepository nutritionalInformationRepository;

    @Autowired
    private PortionRepository portionRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("createMealLog tests")
    class CreateMealLogTests {
        @Test
        @Transactional
        @DisplayName("should be able to create meal log")
        void shouldBeAbleToCreateMealLog() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            CreateMealLogDTO createMealLogDTO = new CreateMealLogDTO(
                    MealType.SNACK,
                    LocalDate.of(2036, 1, 1),
                    LocalTime.of(15, 0),
                    200
            );

            // Act
            MealLogDTO result = sut.createMealLog(createMealLogDTO, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isNotNull();
            assertThat(result.type()).isEqualTo(MealType.SNACK);
            assertThat(result.date()).isEqualTo(LocalDate.of(2036, 1, 1));
            assertThat(result.time()).isEqualTo(LocalTime.of(15, 0));
            assertThat(result.caloriesGoal()).isEqualTo(200);

            var mealLogInDatabase = mealLogRepository.findById(result.id()).orElseThrow();

            assertThat(mealLogInDatabase).isNotNull();
            assertThat(mealLogInDatabase.getType()).isEqualTo(MealType.SNACK);
            assertThat(mealLogInDatabase.getDate()).isEqualTo(LocalDate.of(2036, 1, 1));
            assertThat(mealLogInDatabase.getTime()).isEqualTo(LocalTime.of(15, 0));
            assertThat(mealLogInDatabase.getCaloriesGoal()).isEqualTo(200);
            assertThat(mealLogInDatabase.getUser().getId()).isEqualTo(user.getId());
        }
    }

    @Nested
    @DisplayName("deleteMealLog tests")
    class DeleteMealLogTests {
        @Test
        @Transactional
        @DisplayName("should be able to delete meal log")
        void shouldBeAbleToDeleteMealLog() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            sut.deleteMealLog(mealLog.getId(), user.getId());

            // Assert
            Optional<MealLog> deletedMealLog = mealLogRepository.findById(mealLog.getId());
            assertThat(deletedMealLog).isEmpty();
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotExist() {
            // Act & Assert
            assertThatThrownBy(() -> sut.deleteMealLog(randomUUID().toString(), randomUUID().toString()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log Not Found.");
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if the meal log does not belong to the user")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotBelongToUser() {
            // Arrange
            User owner = createUser();
            userRepository.save(owner);

            User anotherUser = createUser();
            userRepository.save(anotherUser);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(owner)
                    .build();

            mealLogRepository.save(mealLog);

            // Act & Assert
            assertThatThrownBy(() -> sut.deleteMealLog(mealLog.getId(), anotherUser.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log Not Found.");
        }
    }

    @Nested
    @DisplayName("getMealLog tests")
    class GetMealLogTests {
        @Test
        @Transactional
        @DisplayName("should be able to get meal log")
        void shouldBeAbleToGetMealLog() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            MealLogDTO result = sut.getMealLog(mealLog.getId(), user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.type()).isEqualTo(MealType.LUNCH);
            assertThat(result.date()).isEqualTo(LocalDate.of(2036, 1, 1));
            assertThat(result.time()).isEqualTo(LocalTime.of(15, 0));
            assertThat(result.caloriesGoal()).isEqualTo(900);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotExist() {
            // Act & Assert
            assertThatThrownBy(() -> sut.getMealLog(randomUUID().toString(), randomUUID().toString()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not belong to the user")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotBelongToUser() {
            // Arrange
            User owner = createUser();
            userRepository.save(owner);

            User anotherUser = createUser();
            userRepository.save(anotherUser);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(owner)
                    .build();

            mealLogRepository.save(mealLog);

            // Act & Assert
            assertThatThrownBy(() -> sut.getMealLog(mealLog.getId(), anotherUser.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }
    }

    @Nested
    @DisplayName("findMealLogsByDate tests")
    class FindMealLogsByDateTests {
        @Test
        @Transactional
        @DisplayName("should be able to return meal logs with zero calories")
        void shouldBeAbleToReturnMealLogsWithZeroCalories() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            LocalDate date = LocalDate.of(2025, 1, 1);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(date)
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            List<MealLogWithCaloriesConsumedDTO> result = sut.findMealLogsByDate(date, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(1);

            var mealLogResult = result.getFirst();

            assertThat(mealLogResult).isNotNull();
            assertThat(mealLogResult.type()).isEqualTo(MealType.LUNCH);
            assertThat(mealLogResult.date()).isEqualTo(date);
            assertThat(mealLogResult.time()).isEqualTo(LocalTime.of(15, 0));
            assertThat(mealLogResult.caloriesGoal()).isEqualTo(900);
            assertThat(mealLogResult.caloriesConsumed()).isEqualTo(0);
        }

        @Test
        @Transactional
        @DisplayName("should be able to return meal logs with calories consumed")
        void shouldBeAbleToReturnMealLogsWithCaloriesConsumed() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);

            Food food2 = createFood();
            food2.setUser(user);

            Food food3 = createFood();
            food3.setUser(user);
            foodRepository.saveAll(List.of(food1, food2, food3));


            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            NutritionalInformation nutritionalInformation2 = createNutritionalInformation();
            nutritionalInformation2.setFood(food2);
            food2.setNutritionalInformation(nutritionalInformation2);

            NutritionalInformation nutritionalInformation3 = createNutritionalInformation();
            nutritionalInformation3.setFood(food3);
            food3.setNutritionalInformation(nutritionalInformation3);
            nutritionalInformationRepository.saveAll(List.of(nutritionalInformation1, nutritionalInformation2, nutritionalInformation3));


            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            Portion portion2 = createPortion();
            portion2.setFood(food2);
            food2.setPortion(portion2);

            Portion portion3 = createPortion();
            portion3.setFood(food3);
            food3.setPortion(portion3);

            portionRepository.saveAll(List.of(portion1, portion2, portion3));

            Meal meal = createMeal();
            meal.setUser(user);
            meal.setFoods(Set.of(food3, food1));

            mealRepository.save(meal);


            LocalDate date = LocalDate.of(2025, 1, 1);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(date)
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .foods(Set.of(food1, food2))
                    .meals(Set.of(meal))
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            List<MealLogWithCaloriesConsumedDTO> results = sut.findMealLogsByDate(date, user.getId());

            assertThat(results).isNotNull();
            assertThat(results.size()).isEqualTo(1);

            var mealLogResult = results.getFirst();

            assertThat(mealLogResult).isNotNull();
            assertThat(mealLogResult.type()).isEqualTo(MealType.LUNCH);
            assertThat(mealLogResult.date()).isEqualTo(date);
            assertThat(mealLogResult.time()).isEqualTo(LocalTime.of(15, 0));
            assertThat(mealLogResult.caloriesGoal()).isEqualTo(900);

            var nutritionalInformationSum = nutritionalInformation1
                    .getCalories()
                    .add(nutritionalInformation2.getCalories())
                    .add(nutritionalInformation3.getCalories())
                    .add(nutritionalInformation1.getCalories());

            assertThat(mealLogResult.caloriesConsumed()).isEqualTo(nutritionalInformationSum.intValue());
        }

        @Test
        @Transactional
        @DisplayName("should be able to return meal logs with zero calories")
        void shouldBeAbleToReturnEmptyListWhenNoMealLogsFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            LocalDate date = LocalDate.of(2025, 1, 1);

            // Act
            List<MealLogWithCaloriesConsumedDTO> result = sut.findMealLogsByDate(date, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getMealLogFoods tests")
    class GetMealLogFoodsTests {
        @Test
        @Transactional
        @DisplayName("should be able to return meal log foods")
        void shouldBeAbleToReturnMealLogFoods() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);

            Food food2 = createFood();
            food2.setUser(user);

            foodRepository.saveAll(List.of(food1, food2));

            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            NutritionalInformation nutritionalInformation2 = createNutritionalInformation();
            nutritionalInformation2.setFood(food2);
            food2.setNutritionalInformation(nutritionalInformation2);

            nutritionalInformationRepository.saveAll(List.of(nutritionalInformation1, nutritionalInformation2));

            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            Portion portion2 = createPortion();
            portion2.setFood(food2);
            food2.setPortion(portion2);

            portionRepository.saveAll(List.of(portion1, portion2));

            LocalDate date = LocalDate.of(2025, 1, 1);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(date)
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .foods(Set.of(food1, food2))
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            List<FoodDTO> result = sut.getMealLogFoods(mealLog.getId(), user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            assertThat(result.size()).isEqualTo(2);
            assertThat(result)
                    .extracting(FoodDTO::id,
                            foodDTO -> foodDTO.portion().id(),
                            foodDTO -> foodDTO.nutritionalInformation().id())
                    .containsExactlyInAnyOrder(
                            tuple(food1.getId(), portion1.getId(), nutritionalInformation1.getId()),
                            tuple(food2.getId(), portion2.getId(), nutritionalInformation2.getId())
                    );
        }

        @Test
        @Transactional
        @DisplayName("should be able to return empty list when meal log has no foods")
        void shouldBeAbleToReturnEmptyListWhenNoMealLogFoodsFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            LocalDate date = LocalDate.of(2025, 1, 1);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(date)
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            List<FoodDTO> result = sut.getMealLogFoods(mealLog.getId(), user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getMealLogMeals tests")
    class GetMealLogMealsTests {
        @Test
        @Transactional
        @DisplayName("should be able to return meal log meals")
        void shouldBeAbleToReturnMealLogMeals() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);

            Food food2 = createFood();
            food2.setUser(user);

            foodRepository.saveAll(List.of(food1, food2));

            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            NutritionalInformation nutritionalInformation2 = createNutritionalInformation();
            nutritionalInformation2.setFood(food2);
            food2.setNutritionalInformation(nutritionalInformation2);

            nutritionalInformationRepository.saveAll(List.of(nutritionalInformation1, nutritionalInformation2));

            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            Portion portion2 = createPortion();
            portion2.setFood(food2);
            food2.setPortion(portion2);

            portionRepository.saveAll(List.of(portion1, portion2));

            Meal meal1 = createMeal();
            meal1.setUser(user);
            meal1.setFoods(Set.of(food1));

            Meal meal2 = createMeal();
            meal2.setUser(user);
            meal2.setFoods(Set.of(food2));

            mealRepository.saveAll(List.of(meal1, meal2));

            LocalDate date = LocalDate.of(2025, 1, 1);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(date)
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .meals(Set.of(meal1, meal2))
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            List<MealWithFoodsDTO> result = sut.getMealLogMeals(mealLog.getId(), user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            assertThat(result.size()).isEqualTo(2);
        }

        @Test
        @Transactional
        @DisplayName("should be able to return empty list when meal log has no meals")
        void shouldBeAbleToReturnEmptyListWhenNoMealLogMealsFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            LocalDate date = LocalDate.of(2025, 1, 1);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(date)
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            List<MealWithFoodsDTO> result = sut.getMealLogMeals(mealLog.getId(), user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateMealLog tests")
    class UpdateMealLogTests {
        @Test
        @Transactional
        @DisplayName("should be able to update meal log")
        void shouldBeAbleToUpdateMealLog() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            UpdateMealLogDTO updateMealLogDTO = new UpdateMealLogDTO(
                    MealType.AFTERNOON_SNACK,
                    LocalTime.of(22, 0),
                    100
            );

            // Act
            sut.updateMealLog(mealLog.getId(), updateMealLogDTO, user.getId());

            // Assert
            var mealLogInDatabase = mealLogRepository.findById(mealLog.getId()).orElseThrow();

            assertThat(mealLogInDatabase).isNotNull();
            assertThat(mealLogInDatabase.getType()).isEqualTo(MealType.AFTERNOON_SNACK);
            assertThat(mealLogInDatabase.getDate()).isEqualTo(LocalDate.of(2036, 1, 1));
            assertThat(mealLogInDatabase.getTime()).isEqualTo(LocalTime.of(22, 0));
            assertThat(mealLogInDatabase.getCaloriesGoal()).isEqualTo(100);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if the meal log does not exist")
        void shouldThrowResourceNotFoundExceptionIfTheMealLogDoesNotExist() {
            // Arrange
            UpdateMealLogDTO updateMealLogDTO = new UpdateMealLogDTO(
                    MealType.AFTERNOON_SNACK,
                    LocalTime.of(22, 0),
                    100
            );

            // Act & Assert
            assertThatThrownBy(
                    () -> sut.updateMealLog(randomUUID().toString(), updateMealLogDTO, randomUUID().toString()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not belong to the user")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotBelongToUser() {
            // Arrange
            User owner = createUser();
            userRepository.save(owner);

            User anotherUser = createUser();
            userRepository.save(anotherUser);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(owner)
                    .build();

            mealLogRepository.save(mealLog);

            UpdateMealLogDTO updateMealLogDTO = new UpdateMealLogDTO(
                    MealType.AFTERNOON_SNACK,
                    LocalTime.of(22, 0),
                    100
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.updateMealLog(mealLog.getId(), updateMealLogDTO, anotherUser.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }
    }

    @Nested
    @DisplayName("getMealLogNutritionalInformation tests")
    class GetMealLogNutritionalInformationTests {

        @Test
        @DisplayName("should be able to return empty nutritional information for meal log")
        void shouldBeAbleToReturnEmptyNutritionalInformationForMealLog() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            Optional<MealNutritionalInformationDTO> result = sut.getMealLogNutritionalInformation(mealLog.getId(), user.getId());

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should be able to return nutritional information for meal log")
        void shouldBeAbleToReturnNutritionalInformationForMealLog() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);

            Food food2 = createFood();
            food2.setUser(user);

            Food food3 = createFood();
            food3.setUser(user);

            foodRepository.saveAll(List.of(food1, food2, food3));

            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            NutritionalInformation nutritionalInformation2 = createNutritionalInformation();
            nutritionalInformation2.setFood(food2);
            food2.setNutritionalInformation(nutritionalInformation2);

            NutritionalInformation nutritionalInformation3 = createNutritionalInformation();
            nutritionalInformation3.setFood(food3);
            food3.setNutritionalInformation(nutritionalInformation3);

            nutritionalInformationRepository.saveAll(List.of(nutritionalInformation1, nutritionalInformation2, nutritionalInformation3));

            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            Portion portion2 = createPortion();
            portion2.setFood(food2);
            food2.setPortion(portion2);

            Portion portion3 = createPortion();
            portion3.setFood(food3);
            food3.setPortion(portion3);

            portionRepository.saveAll(List.of(portion1, portion2, portion3));

            Meal meal1 = createMeal();
            meal1.setUser(user);
            meal1.setFoods(Set.of(food1));

            Meal meal2 = createMeal();
            meal2.setUser(user);
            meal2.setFoods(Set.of(food2));

            mealRepository.saveAll(List.of(meal1, meal2));

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .meals(Set.of(meal1, meal2))
                    .foods(Set.of(food3))
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            Optional<MealNutritionalInformationDTO> optionalResult = sut.getMealLogNutritionalInformation(mealLog.getId(), user.getId());

            // Assert
            assertThat(optionalResult).isPresent();

            var result = optionalResult.get();

            var caloriesSum = nutritionalInformation1.getCalories()
                    .add(nutritionalInformation2.getCalories())
                    .add(nutritionalInformation3.getCalories());
            assertThat(result.calories()).isEqualTo(caloriesSum);

            var carbohydratesSum = nutritionalInformation1.getCarbohydrates()
                    .add(nutritionalInformation2.getCarbohydrates())
                    .add(nutritionalInformation3.getCarbohydrates());

            assertThat(result.carbohydrates()).isEqualTo(carbohydratesSum);

            var proteinSum = nutritionalInformation1.getProtein()
                    .add(nutritionalInformation2.getProtein())
                    .add(nutritionalInformation3.getProtein());

            assertThat(result.protein()).isEqualTo(proteinSum);

            var fatSum = nutritionalInformation1.getFat()
                    .add(nutritionalInformation2.getFat())
                    .add(nutritionalInformation3.getFat());

            assertThat(result.fat()).isEqualTo(fatSum);

            var monounsaturatedFatSum = nutritionalInformation1.getMonounsaturatedFat()
                    .add(nutritionalInformation2.getMonounsaturatedFat())
                    .add(nutritionalInformation3.getMonounsaturatedFat());

            assertThat(result.monounsaturatedFat()).isEqualTo(monounsaturatedFatSum);

            var saturatedFatSum = nutritionalInformation1.getSaturatedFat()
                    .add(nutritionalInformation2.getSaturatedFat())
                    .add(nutritionalInformation3.getSaturatedFat());

            assertThat(result.saturatedFat()).isEqualTo(saturatedFatSum);

            var polyunsaturatedFatSum = nutritionalInformation1.getPolyunsaturatedFat()
                    .add(nutritionalInformation2.getPolyunsaturatedFat())
                    .add(nutritionalInformation3.getPolyunsaturatedFat());

            assertThat(result.polyunsaturatedFat()).isEqualTo(polyunsaturatedFatSum);

            var transFatSum = nutritionalInformation1.getTransFat()
                    .add(nutritionalInformation2.getTransFat())
                    .add(nutritionalInformation3.getTransFat());

            assertThat(result.transFat()).isEqualByComparingTo(transFatSum);

            var cholesterolSum = nutritionalInformation1.getCholesterol()
                    .add(nutritionalInformation2.getCholesterol())
                    .add(nutritionalInformation3.getCholesterol());

            assertThat(result.cholesterol()).isEqualByComparingTo(cholesterolSum);

            var sodiumSum = nutritionalInformation1.getSodium()
                    .add(nutritionalInformation2.getSodium())
                    .add(nutritionalInformation3.getSodium());

            assertThat(result.sodium()).isEqualByComparingTo(sodiumSum);

            var potassiumSum = nutritionalInformation1.getPotassium()
                    .add(nutritionalInformation2.getPotassium())
                    .add(nutritionalInformation3.getPotassium());

            assertThat(result.potassium()).isEqualByComparingTo(potassiumSum);

            var fiberSum = nutritionalInformation1.getFiber()
                    .add(nutritionalInformation2.getFiber())
                    .add(nutritionalInformation3.getFiber());

            assertThat(result.fiber()).isEqualByComparingTo(fiberSum);

            var sugarSum = nutritionalInformation1.getSugar()
                    .add(nutritionalInformation2.getSugar())
                    .add(nutritionalInformation3.getSugar());

            assertThat(result.sugar()).isEqualByComparingTo(sugarSum);

            var calciumSum = nutritionalInformation1.getCalcium()
                    .add(nutritionalInformation2.getCalcium())
                    .add(nutritionalInformation3.getCalcium());

            assertThat(result.calcium()).isEqualByComparingTo(calciumSum);

            var ironSum = nutritionalInformation1.getIron()
                    .add(nutritionalInformation2.getIron())
                    .add(nutritionalInformation3.getIron());

            assertThat(result.iron()).isEqualByComparingTo(ironSum);

            var vitaminASum = nutritionalInformation1.getVitaminA()
                    .add(nutritionalInformation2.getVitaminA())
                    .add(nutritionalInformation3.getVitaminA());

            assertThat(result.vitaminA()).isEqualByComparingTo(vitaminASum);

            var vitaminCSum = nutritionalInformation1.getVitaminC()
                    .add(nutritionalInformation2.getVitaminC())
                    .add(nutritionalInformation3.getVitaminC());

            assertThat(result.vitaminC()).isEqualByComparingTo(vitaminCSum);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotExist() {
            // Act & Assert
            assertThatThrownBy(() -> sut.getMealLogNutritionalInformation(randomUUID().toString(), randomUUID().toString()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not belong to the user")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotBelongToUser() {
            // Arrange
            User owner = createUser();
            userRepository.save(owner);

            User anotherUser = createUser();
            userRepository.save(anotherUser);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(owner)
                    .build();

            mealLogRepository.save(mealLog);

            // Act & Assert
            assertThatThrownBy(() -> sut.getMealLogNutritionalInformation(mealLog.getId(), anotherUser.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }
    }

    @Nested
    @DisplayName("addFoodsToMealLog Tests")
    class AddFoodsToMealLogTests {
        @Test
        @Transactional
        @DisplayName("should be able to add foods to meal log")
        void shouldBeAbleToAddFoodsToMealLog() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);

            Food food2 = createFood();
            food2.setUser(user);

            foodRepository.saveAll(List.of(food1, food2));

            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            NutritionalInformation nutritionalInformation2 = createNutritionalInformation();
            nutritionalInformation2.setFood(food2);
            food2.setNutritionalInformation(nutritionalInformation2);

            nutritionalInformationRepository.saveAll(List.of(nutritionalInformation1, nutritionalInformation2));

            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            Portion portion2 = createPortion();
            portion2.setFood(food2);
            food2.setPortion(portion2);

            portionRepository.saveAll(List.of(portion1, portion2));

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .foods(new HashSet<>())
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            AddFoodsToMealLogDTO addFoodsToMealLogDTO = new AddFoodsToMealLogDTO(
                    Set.of(food1.getId(), food2.getId())
            );

            // Act
            sut.addFoodsToMealLog(mealLog.getId(), addFoodsToMealLogDTO, user.getId());

            // Assert
            MealLog mealLogInDatabase = mealLogRepository
                    .findMealLogByIdAndUserId(mealLog.getId(), user.getId())
                    .orElseThrow();

            assertThat(mealLogInDatabase.getFoods()).contains(food1, food2);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if some foods were not found")
        void shouldThrowResourceNotFoundExceptionIfSomeFoodsWereNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);
            Food food2 = createFood();
            food2.setUser(user);

            foodRepository.saveAll(List.of(food1, food2));

            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            NutritionalInformation nutritionalInformation2 = createNutritionalInformation();
            nutritionalInformation2.setFood(food2);
            food2.setNutritionalInformation(nutritionalInformation2);

            nutritionalInformationRepository.saveAll(List.of(nutritionalInformation1, nutritionalInformation2));

            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            Portion portion2 = createPortion();
            portion2.setFood(food2);
            food2.setPortion(portion2);

            portionRepository.saveAll(List.of(portion1, portion2));

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .foods(new HashSet<>())
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            AddFoodsToMealLogDTO addFoodsToMealLogDTO = new AddFoodsToMealLogDTO(
                    Set.of(food1.getId(), food2.getId(), randomUUID().toString())
            );

            // Act
            assertThatThrownBy(() -> sut.addFoodsToMealLog(mealLog.getId(), addFoodsToMealLogDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Some foods were not found.");


            // Assert
            MealLog mealLogInDatabase = mealLogRepository
                    .findMealLogByIdAndUserId(mealLog.getId(), user.getId())
                    .orElseThrow();

            //Hibernate.initialize(mealLogInDatabase.getFoods());

            assertThat(mealLogInDatabase.getFoods()).hasSize(0);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotExist() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);

            Food food2 = createFood();
            food2.setUser(user);

            foodRepository.saveAll(List.of(food1, food2));

            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            NutritionalInformation nutritionalInformation2 = createNutritionalInformation();
            nutritionalInformation2.setFood(food2);
            food2.setNutritionalInformation(nutritionalInformation2);

            nutritionalInformationRepository.saveAll(List.of(nutritionalInformation1, nutritionalInformation2));

            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            Portion portion2 = createPortion();
            portion2.setFood(food2);
            food2.setPortion(portion2);

            portionRepository.saveAll(List.of(portion1, portion2));

            AddFoodsToMealLogDTO addFoodsToMealLogDTO = new AddFoodsToMealLogDTO(
                    Set.of(food1.getId(), food2.getId())
            );

            // Act
            assertThatThrownBy(() -> sut.addFoodsToMealLog(randomUUID().toString(), addFoodsToMealLogDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }
    }

    @Nested
    @DisplayName("addMealsToMealLog Tests")
    class AddMealsToMealLogTests {
        @Test
        @Transactional
        @DisplayName("should be able to add meals to meal log")
        void shouldBeAbleToAddFoodsToMealLog() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal1 = createMeal();
            meal1.setUser(user);

            Meal meal2 = createMeal();
            meal2.setUser(user);

            mealRepository.saveAll(List.of(meal1, meal2));

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .meals(new HashSet<>())
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            AddMealsToMealLogDTO addMealsToMealLogDTO = new AddMealsToMealLogDTO(
                    Set.of(meal1.getId(), meal2.getId())
            );

            // Act
            sut.addMealsToMealLog(mealLog.getId(), addMealsToMealLogDTO, user.getId());

            // Assert
            MealLog mealLogInDatabase = mealLogRepository
                    .findMealLogByIdAndUserId(mealLog.getId(), user.getId())
                    .orElseThrow();

            assertThat(mealLogInDatabase.getMeals()).contains(meal1, meal2);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if some meals were not found")
        void shouldThrowResourceNotFoundExceptionIfSomeFoodsWereNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal1 = createMeal();
            meal1.setUser(user);

            Meal meal2 = createMeal();
            meal2.setUser(user);

            mealRepository.saveAll(List.of(meal1, meal2));

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .meals(new HashSet<>())
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            AddMealsToMealLogDTO addMealsToMealLogDTO = new AddMealsToMealLogDTO(
                    Set.of(meal1.getId(), meal2.getId(), randomUUID().toString())
            );

            // Act
            assertThatThrownBy(() -> sut.addMealsToMealLog(mealLog.getId(), addMealsToMealLogDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Some meals were not found.");

            // Assert
            MealLog mealLogInDatabase = mealLogRepository
                    .findMealLogByIdAndUserId(mealLog.getId(), user.getId())
                    .orElseThrow();

            assertThat(mealLogInDatabase.getMeals()).hasSize(0);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotExist() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal1 = createMeal();
            meal1.setUser(user);

            Meal meal2 = createMeal();
            meal2.setUser(user);

            mealRepository.saveAll(List.of(meal1, meal2));

            Set<Meal> meals = new HashSet<>();
            meals.add(meal1);
            meals.add(meal2);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .meals(meals)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            AddMealsToMealLogDTO addMealsToMealLogDTO = new AddMealsToMealLogDTO(
                    Set.of(meal1.getId(), meal2.getId())
            );

            // Act
            assertThatThrownBy(() -> sut.addMealsToMealLog(randomUUID().toString(), addMealsToMealLogDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }
    }

    @Nested
    @DisplayName("removeMealLogFoods tests")
    class RemoveMealLogFoodsTests {
        @Test
        @Transactional
        @DisplayName("should be able to remove meal log foods")
        void shouldBeAbleToRemoveMealLogFoods() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);

            Food food2 = createFood();
            food2.setUser(user);

            foodRepository.saveAll(List.of(food1, food2));

            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            NutritionalInformation nutritionalInformation2 = createNutritionalInformation();
            nutritionalInformation2.setFood(food2);
            food2.setNutritionalInformation(nutritionalInformation2);

            nutritionalInformationRepository.saveAll(List.of(nutritionalInformation1, nutritionalInformation2));

            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            Portion portion2 = createPortion();
            portion2.setFood(food2);
            food2.setPortion(portion2);

            portionRepository.saveAll(List.of(portion1, portion2));

            Set<Food> foods = new HashSet<>();
            foods.add(food1);
            foods.add(food2);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .foods(foods)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            DeleteMealLogFoodsDTO deleteMealLogFoodsDTO = new DeleteMealLogFoodsDTO(
                Set.of(food1.getId(), food2.getId())
            );

            // Act
            sut.removeMealLogFoods(mealLog.getId(), deleteMealLogFoodsDTO, user.getId());

            // Assert
            MealLog mealLogInDatabase = mealLogRepository
                    .findMealLogByIdAndUserId(mealLog.getId(), user.getId())
                    .orElseThrow();

            assertThat(mealLogInDatabase.getFoods()).hasSize(0);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotExist() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);

            Food food2 = createFood();
            food2.setUser(user);

            foodRepository.saveAll(List.of(food1, food2));

            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            NutritionalInformation nutritionalInformation2 = createNutritionalInformation();
            nutritionalInformation2.setFood(food2);
            food2.setNutritionalInformation(nutritionalInformation2);

            nutritionalInformationRepository.saveAll(List.of(nutritionalInformation1, nutritionalInformation2));

            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            Portion portion2 = createPortion();
            portion2.setFood(food2);
            food2.setPortion(portion2);

            portionRepository.saveAll(List.of(portion1, portion2));

            DeleteMealLogFoodsDTO deleteMealLogFoodsDTO = new DeleteMealLogFoodsDTO(
                    Set.of(food1.getId(), food2.getId())
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.removeMealLogFoods(randomUUID().toString(), deleteMealLogFoodsDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if some foods were not found")
        void shouldThrowResourceNotFoundExceptionIfSomeFoodsWereNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food1 = createFood();
            food1.setUser(user);

            foodRepository.save(food1);

            NutritionalInformation nutritionalInformation1 = createNutritionalInformation();
            nutritionalInformation1.setFood(food1);
            food1.setNutritionalInformation(nutritionalInformation1);

            nutritionalInformationRepository.save(nutritionalInformation1);

            Portion portion1 = createPortion();
            portion1.setFood(food1);
            food1.setPortion(portion1);

            portionRepository.save(portion1);

            Set<Food> foods = new HashSet<>();
            foods.add(food1);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .foods(foods)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            DeleteMealLogFoodsDTO deleteMealLogFoodsDTO = new DeleteMealLogFoodsDTO(
                    Set.of(food1.getId(), randomUUID().toString())
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.removeMealLogFoods(mealLog.getId(), deleteMealLogFoodsDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Some foods were not found.");

            MealLog mealLogInDatabase = mealLogRepository
                    .findMealLogByIdAndUserId(mealLog.getId(), user.getId())
                    .orElseThrow();

            assertThat(mealLogInDatabase.getFoods()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("removeMealLogMeals tests")
    class RemoveMealLogMealsTests {
        @Test
        @Transactional
        @DisplayName("should be able to remove meal log meals")
        void shouldBeAbleToRemoveMealLogMeals() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal1 = createMeal();
            Meal meal2 = createMeal();
            meal1.setUser(user);
            meal2.setUser(user);

            mealRepository.saveAll(List.of(meal1, meal2));

            Set<Meal> meals = new HashSet<>();
            meals.add(meal1);
            meals.add(meal2);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .meals(meals)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            DeleteMealLogMealsDTO deleteMealLogMealsDTO = new DeleteMealLogMealsDTO(
                    Set.of(meal1.getId(), meal2.getId())
            );

            // Act
            sut.removeMealLogMeals(mealLog.getId(), deleteMealLogMealsDTO, user.getId());

            // Assert
            MealLog mealLogInDatabase = mealLogRepository
                    .findMealLogByIdAndUserId(mealLog.getId(), user.getId())
                    .orElseThrow();

            assertThat(mealLogInDatabase.getMeals()).hasSize(0);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if meal log does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealLogDoesNotExist() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal1 = createMeal();
            Meal meal2 = createMeal();
            meal1.setUser(user);
            meal2.setUser(user);

            mealRepository.saveAll(List.of(meal1, meal2));

            DeleteMealLogMealsDTO deleteMealLogMealsDTO = new DeleteMealLogMealsDTO(
                    Set.of(meal1.getId(), meal2.getId())
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.removeMealLogMeals(randomUUID().toString(), deleteMealLogMealsDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal Log not found.");
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if some foods were not found")
        void shouldThrowResourceNotFoundExceptionIfSomeFoodsWereNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal1 = createMeal();
            meal1.setUser(user);

            mealRepository.save(meal1);

            Set<Meal> meals = new HashSet<>();
            meals.add(meal1);

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2036, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .meals(meals)
                    .user(user)
                    .build();

            mealLogRepository.save(mealLog);

            // Act
            DeleteMealLogMealsDTO deleteMealLogMealsDTO = new DeleteMealLogMealsDTO(
                    Set.of(meal1.getId(), randomUUID().toString())
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.removeMealLogMeals(mealLog.getId(), deleteMealLogMealsDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Some meals were not found.");

            MealLog mealLogInDatabase = mealLogRepository
                    .findMealLogByIdAndUserId(mealLog.getId(), user.getId())
                    .orElseThrow();

            assertThat(mealLogInDatabase.getMeals()).hasSize(1);
        }
    }
}
