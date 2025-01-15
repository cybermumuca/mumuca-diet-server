package com.mumuca.diet.meal.repository;

import com.mumuca.diet.food.model.Food;
import com.mumuca.diet.food.model.NutritionalInformation;
import com.mumuca.diet.food.model.Portion;
import com.mumuca.diet.food.repository.FoodRepository;
import com.mumuca.diet.food.repository.NutritionalInformationRepository;
import com.mumuca.diet.food.repository.PortionRepository;
import com.mumuca.diet.model.Meal;
import com.mumuca.diet.model.MealLog;
import com.mumuca.diet.model.MealType;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.MealLogRepository;
import com.mumuca.diet.repository.MealRepository;
import com.mumuca.diet.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static com.mumuca.diet.testutil.EntityGeneratorUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("MealLogRepository Integration Tests")
public class MealLogRepositoryIntegrationTests {

    @Autowired
    private MealLogRepository sut;

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
    @DisplayName("sumCaloriesFromFoodsByMealLogIdAndUserId tests")
    class SumCaloriesFromFoodsByMealLogIdAndUserIdTests {
        @Test
        @DisplayName("should be able to sum foods calories")
        void shouldBeAbleToSumFoodsCalories() {
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
                    .date(LocalDate.of(2025, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .foods(Set.of(food1, food2))
                    .build();

            sut.save(mealLog);

            // Act
            Integer result = sut.sumCaloriesFromFoodsByMealLogIdAndUserId(mealLog.getId(), user.getId());

            // Assert
            var nutritionalInformationSum = nutritionalInformation1
                    .getCalories()
                    .add(nutritionalInformation2.getCalories());

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(nutritionalInformationSum.intValue());
        }
    }

    @Nested
    @DisplayName("sumCaloriesFromMealsByMealLogIdAndUserId tests")
    class SumCaloriesFromMealsByMealLogIdAndUserIdTests {
        @Test
        @DisplayName("should be able to sum meals calories")
        void shouldBeAbleToSumMealsCalories() {
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

            MealLog mealLog = MealLog.builder()
                    .type(MealType.LUNCH)
                    .date(LocalDate.of(2025, 1, 1))
                    .time(LocalTime.of(15, 0))
                    .caloriesGoal(900)
                    .user(user)
                    .meals(Set.of(meal1, meal2))
                    .build();

            sut.save(mealLog);

            // Act
            Integer result = sut.sumCaloriesFromMealsByMealLogIdAndUserId(mealLog.getId(), user.getId());

            // Assert
            var nutritionalInformationSum = nutritionalInformation1
                    .getCalories()
                    .add(nutritionalInformation2.getCalories());

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(nutritionalInformationSum.intValue());
        }
    }
}
