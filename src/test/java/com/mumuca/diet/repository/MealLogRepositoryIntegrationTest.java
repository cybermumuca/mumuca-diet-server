package com.mumuca.diet.repository;

import com.mumuca.diet.dto.meal.MealNutritionalInformationDTO;
import com.mumuca.diet.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.mumuca.diet.testutil.EntityGeneratorUtil.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MealLogRepositoryIntegrationTest {

    @Autowired
    private MealLogRepository sut;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MealLogRepository mealLogRepository;

    @BeforeEach
    void setUp() {}

    @Test
    @Transactional
    @Rollback
    @DisplayName("should correctly sum the nutritional information of foods when all fields are not null")
    void shouldCorrectlySumTheNutritionalInformationOfFoodsWhenAllFieldsAreNotNull() {
        User user = createUser();
        userRepository.save(user);

        NutritionalInformation nutritionalInfo1 = createNutritionalInformation();
        NutritionalInformation nutritionalInfo2 = createNutritionalInformation();

        Food food1 = createFood();
        food1.setNutritionalInformation(nutritionalInfo1);
        food1.setUser(user);

        Food food2 = createFood();
        food2.setNutritionalInformation(nutritionalInfo2);
        food2.setUser(user);

        Food food3 = createFood();
        food3.setNutritionalInformation(createNutritionalInformation());
        food3.setUser(user);

        foodRepository.saveAll(List.of(food1, food2, food3));

        Meal meal = createMeal();
        meal.setFoods(Set.of(food3));
        meal.setUser(user);

        mealRepository.save(meal);

        MealLog mealLog = MealLog.builder()
                .type(MealType.LUNCH)
                .caloriesGoal(faker.number().numberBetween(50, 450))
                .time(LocalTime.now())
                .date(LocalDate.now())
                .foods(Set.of(food1, food2))
                .meals(Set.of(meal))
                .user(user)
                .build();

        mealLogRepository.save(mealLog);

        Optional<MealNutritionalInformationDTO> result = sut
                .sumFoodsNutritionalInformationByMealLogIdAndUserId(mealLog.getId(), user.getId());

        assertTrue(result.isPresent(), "The result of the sum should not be empty.");

        MealNutritionalInformationDTO sum = result.get();

        BigDecimal expectedCalories = nutritionalInfo1.getCalories().add(nutritionalInfo2.getCalories());
        BigDecimal expectedCarbohydrates = nutritionalInfo1.getCarbohydrates().add(nutritionalInfo2.getCarbohydrates());
        BigDecimal expectedProtein = nutritionalInfo1.getProtein().add(nutritionalInfo2.getProtein());
        BigDecimal expectedFat = nutritionalInfo1.getFat().add(nutritionalInfo2.getFat());
        BigDecimal expectedMonounsaturatedFat = nutritionalInfo1.getMonounsaturatedFat().add(nutritionalInfo2.getMonounsaturatedFat());
        BigDecimal expectedSaturatedFat = nutritionalInfo1.getSaturatedFat().add(nutritionalInfo2.getSaturatedFat());
        BigDecimal expectedPolyunsaturatedFat = nutritionalInfo1.getPolyunsaturatedFat().add(nutritionalInfo2.getPolyunsaturatedFat());
        BigDecimal expectedTransFat = nutritionalInfo1.getTransFat().add(nutritionalInfo2.getTransFat());
        BigDecimal expectedCholesterol = nutritionalInfo1.getCholesterol().add(nutritionalInfo2.getCholesterol());
        BigDecimal expectedSodium = nutritionalInfo1.getSodium().add(nutritionalInfo2.getSodium());
        BigDecimal expectedPotassium = nutritionalInfo1.getPotassium().add(nutritionalInfo2.getPotassium());
        BigDecimal expectedFiber = nutritionalInfo1.getFiber().add(nutritionalInfo2.getFiber());
        BigDecimal expectedSugar = nutritionalInfo1.getSugar().add(nutritionalInfo2.getSugar());
        BigDecimal expectedCalcium = nutritionalInfo1.getCalcium().add(nutritionalInfo2.getCalcium());
        BigDecimal expectedIron = nutritionalInfo1.getIron().add(nutritionalInfo2.getIron());
        BigDecimal expectedVitaminA = nutritionalInfo1.getVitaminA().add(nutritionalInfo2.getVitaminA());
        BigDecimal expectedVitaminC = nutritionalInfo1.getVitaminC().add(nutritionalInfo2.getVitaminC());

        assertEquals(0, expectedCalories.compareTo(sum.calories()), "The calories summed do not match");
        assertEquals(0, expectedCarbohydrates.compareTo(sum.carbohydrates()), "The carbohydrates summed do not match");
        assertEquals(0, expectedProtein.compareTo(sum.protein()), "The proteins summed do not match");
        assertEquals(0, expectedFat.compareTo(sum.fat()), "The fat summed do not match");
        assertEquals(0, expectedMonounsaturatedFat.compareTo(sum.monounsaturatedFat()), "The monounsaturated fat summed do not match");
        assertEquals(0, expectedSaturatedFat.compareTo(sum.saturatedFat()), "The saturated fat summed do not match");
        assertEquals(0, expectedPolyunsaturatedFat.compareTo(sum.polyunsaturatedFat()), "The polyunsaturated fat summed do not match");
        assertEquals(0, expectedTransFat.compareTo(sum.transFat()), "The trans fat summed do not match");
        assertEquals(0, expectedCholesterol.compareTo(sum.cholesterol()), "The cholesterol summed do not match");
        assertEquals(0, expectedSodium.compareTo(sum.sodium()), "The sodium summed do not match");
        assertEquals(0, expectedPotassium.compareTo(sum.potassium()), "The potassium summed do not match");
        assertEquals(0, expectedFiber.compareTo(sum.fiber()), "The fiber summed do not match");
        assertEquals(0, expectedSugar.compareTo(sum.sugar()), "The sugar summed do not match");
        assertEquals(0, expectedCalcium.compareTo(sum.calcium()), "The calcium summed do not match");
        assertEquals(0, expectedIron.compareTo(sum.iron()), "The iron summed do not match");
        assertEquals(0, expectedVitaminA.compareTo(sum.vitaminA()), "The vitamin A summed do not match");
        assertEquals(0, expectedVitaminC.compareTo(sum.vitaminC()), "The vitamin C summed do not match");
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("should correctly sum the nutritional information of foods when some fields are null")
    void shouldCorrectlySumTheNutritionalInformationOfFoodsWhenSomeFieldsAreNull() {
        User user = createUser();
        userRepository.save(user);

        NutritionalInformation nutritionalInfo1 = createNutritionalInformation();

        nutritionalInfo1.setCarbohydrates(null);
        nutritionalInfo1.setProtein(null);

        NutritionalInformation nutritionalInfo2 = createNutritionalInformation();

        nutritionalInfo2.setFat(null);

        Food food1 = createFood();
        food1.setNutritionalInformation(nutritionalInfo1);
        food1.setUser(user);

        Food food2 = createFood();
        food2.setNutritionalInformation(nutritionalInfo2);
        food2.setUser(user);

        Food food3 = createFood();
        food3.setNutritionalInformation(createNutritionalInformation());
        food3.setUser(user);

        foodRepository.saveAll(List.of(food1, food2, food3));

        Meal meal = createMeal();
        meal.setFoods(Set.of(food3));
        meal.setUser(user);

        mealRepository.save(meal);

        MealLog mealLog = MealLog.builder()
                .type(MealType.LUNCH)
                .caloriesGoal(faker.number().numberBetween(50, 450))
                .time(LocalTime.now())
                .date(LocalDate.now())
                .foods(Set.of(food1, food2))
                .meals(Set.of(meal))
                .user(user)
                .build();

        mealLogRepository.save(mealLog);

        Optional<MealNutritionalInformationDTO> result = sut
                .sumFoodsNutritionalInformationByMealLogIdAndUserId(mealLog.getId(), user.getId());

        assertTrue(result.isPresent(), "The result of the sum should not be empty.");

        MealNutritionalInformationDTO sum = result.get();

        BigDecimal expectedCalories = nutritionalInfo1.getCalories().add(nutritionalInfo2.getCalories());
        BigDecimal expectedCarbohydrates = nutritionalInfo1.getCarbohydrates().add(nutritionalInfo2.getCarbohydrates());
        BigDecimal expectedProtein = nutritionalInfo1.getProtein().add(nutritionalInfo2.getProtein());
        BigDecimal expectedFat = nutritionalInfo1.getFat().add(nutritionalInfo2.getFat());
        BigDecimal expectedMonounsaturatedFat = nutritionalInfo1.getMonounsaturatedFat().add(nutritionalInfo2.getMonounsaturatedFat());
        BigDecimal expectedSaturatedFat = nutritionalInfo1.getSaturatedFat().add(nutritionalInfo2.getSaturatedFat());
        BigDecimal expectedPolyunsaturatedFat = nutritionalInfo1.getPolyunsaturatedFat().add(nutritionalInfo2.getPolyunsaturatedFat());
        BigDecimal expectedTransFat = nutritionalInfo1.getTransFat().add(nutritionalInfo2.getTransFat());
        BigDecimal expectedCholesterol = nutritionalInfo1.getCholesterol().add(nutritionalInfo2.getCholesterol());
        BigDecimal expectedSodium = nutritionalInfo1.getSodium().add(nutritionalInfo2.getSodium());
        BigDecimal expectedPotassium = nutritionalInfo1.getPotassium().add(nutritionalInfo2.getPotassium());
        BigDecimal expectedFiber = nutritionalInfo1.getFiber().add(nutritionalInfo2.getFiber());
        BigDecimal expectedSugar = nutritionalInfo1.getSugar().add(nutritionalInfo2.getSugar());
        BigDecimal expectedCalcium = nutritionalInfo1.getCalcium().add(nutritionalInfo2.getCalcium());
        BigDecimal expectedIron = nutritionalInfo1.getIron().add(nutritionalInfo2.getIron());
        BigDecimal expectedVitaminA = nutritionalInfo1.getVitaminA().add(nutritionalInfo2.getVitaminA());
        BigDecimal expectedVitaminC = nutritionalInfo1.getVitaminC().add(nutritionalInfo2.getVitaminC());

        assertEquals(0, expectedCalories.compareTo(sum.calories()), "The calories summed do not match");
        assertEquals(0, expectedCarbohydrates.compareTo(sum.carbohydrates()), "The carbohydrates summed do not match");
        assertEquals(0, expectedProtein.compareTo(sum.protein()), "The proteins summed do not match");
        assertEquals(0, expectedFat.compareTo(sum.fat()), "The fat summed do not match");
        assertEquals(0, expectedMonounsaturatedFat.compareTo(sum.monounsaturatedFat()), "The monounsaturated fat summed do not match");
        assertEquals(0, expectedSaturatedFat.compareTo(sum.saturatedFat()), "The saturated fat summed do not match");
        assertEquals(0, expectedPolyunsaturatedFat.compareTo(sum.polyunsaturatedFat()), "The polyunsaturated fat summed do not match");
        assertEquals(0, expectedTransFat.compareTo(sum.transFat()), "The trans fat summed do not match");
        assertEquals(0, expectedCholesterol.compareTo(sum.cholesterol()), "The cholesterol summed do not match");
        assertEquals(0, expectedSodium.compareTo(sum.sodium()), "The sodium summed do not match");
        assertEquals(0, expectedPotassium.compareTo(sum.potassium()), "The potassium summed do not match");
        assertEquals(0, expectedFiber.compareTo(sum.fiber()), "The fiber summed do not match");
        assertEquals(0, expectedSugar.compareTo(sum.sugar()), "The sugar summed do not match");
        assertEquals(0, expectedCalcium.compareTo(sum.calcium()), "The calcium summed do not match");
        assertEquals(0, expectedIron.compareTo(sum.iron()), "The iron summed do not match");
        assertEquals(0, expectedVitaminA.compareTo(sum.vitaminA()), "The vitamin A summed do not match");
        assertEquals(0, expectedVitaminC.compareTo(sum.vitaminC()), "The vitamin C summed do not match");
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("should correctly sum the nutritional information of meals when all fields are not null")
    void shouldCorrectlySumTheNutritionalInformationOfMealsWhenAllFieldsAreNotNull() {
        User user = createUser();
        userRepository.save(user);

        NutritionalInformation nutritionalInfo1 = createNutritionalInformation();
        NutritionalInformation nutritionalInfo2 = createNutritionalInformation();

        Food food1 = createFood();
        food1.setNutritionalInformation(createNutritionalInformation());
        food1.setUser(user);

        Food food2 = createFood();
        food2.setNutritionalInformation(createNutritionalInformation());
        food2.setUser(user);

        Food food3 = createFood();
        food3.setNutritionalInformation(nutritionalInfo1);
        food3.setUser(user);

        Food food4 = createFood();
        food4.setNutritionalInformation(nutritionalInfo2);
        food4.setUser(user);

        foodRepository.saveAll(List.of(food1, food2, food3, food4));

        Meal meal = createMeal();
        meal.setFoods(Set.of(food3, food4));
        meal.setUser(user);

        mealRepository.save(meal);

        MealLog mealLog = MealLog.builder()
                .type(MealType.LUNCH)
                .caloriesGoal(faker.number().numberBetween(50, 450))
                .time(LocalTime.now())
                .date(LocalDate.now())
                .foods(Set.of(food1, food2))
                .meals(Set.of(meal))
                .user(user)
                .build();

        mealLogRepository.save(mealLog);

        Optional<MealNutritionalInformationDTO> result = sut
                .sumMealsNutritionalInformationByMealLogIdAndUserId(mealLog.getId(), user.getId());

        assertTrue(result.isPresent(), "The result of the sum should not be empty.");

        MealNutritionalInformationDTO sum = result.get();

        BigDecimal expectedCalories = nutritionalInfo1.getCalories().add(nutritionalInfo2.getCalories());
        BigDecimal expectedCarbohydrates = nutritionalInfo1.getCarbohydrates().add(nutritionalInfo2.getCarbohydrates());
        BigDecimal expectedProtein = nutritionalInfo1.getProtein().add(nutritionalInfo2.getProtein());
        BigDecimal expectedFat = nutritionalInfo1.getFat().add(nutritionalInfo2.getFat());
        BigDecimal expectedMonounsaturatedFat = nutritionalInfo1.getMonounsaturatedFat().add(nutritionalInfo2.getMonounsaturatedFat());
        BigDecimal expectedSaturatedFat = nutritionalInfo1.getSaturatedFat().add(nutritionalInfo2.getSaturatedFat());
        BigDecimal expectedPolyunsaturatedFat = nutritionalInfo1.getPolyunsaturatedFat().add(nutritionalInfo2.getPolyunsaturatedFat());
        BigDecimal expectedTransFat = nutritionalInfo1.getTransFat().add(nutritionalInfo2.getTransFat());
        BigDecimal expectedCholesterol = nutritionalInfo1.getCholesterol().add(nutritionalInfo2.getCholesterol());
        BigDecimal expectedSodium = nutritionalInfo1.getSodium().add(nutritionalInfo2.getSodium());
        BigDecimal expectedPotassium = nutritionalInfo1.getPotassium().add(nutritionalInfo2.getPotassium());
        BigDecimal expectedFiber = nutritionalInfo1.getFiber().add(nutritionalInfo2.getFiber());
        BigDecimal expectedSugar = nutritionalInfo1.getSugar().add(nutritionalInfo2.getSugar());
        BigDecimal expectedCalcium = nutritionalInfo1.getCalcium().add(nutritionalInfo2.getCalcium());
        BigDecimal expectedIron = nutritionalInfo1.getIron().add(nutritionalInfo2.getIron());
        BigDecimal expectedVitaminA = nutritionalInfo1.getVitaminA().add(nutritionalInfo2.getVitaminA());
        BigDecimal expectedVitaminC = nutritionalInfo1.getVitaminC().add(nutritionalInfo2.getVitaminC());

        assertEquals(0, expectedCalories.compareTo(sum.calories()), "The calories summed do not match");
        assertEquals(0, expectedCarbohydrates.compareTo(sum.carbohydrates()), "The carbohydrates summed do not match");
        assertEquals(0, expectedProtein.compareTo(sum.protein()), "The proteins summed do not match");
        assertEquals(0, expectedFat.compareTo(sum.fat()), "The fat summed do not match");
        assertEquals(0, expectedMonounsaturatedFat.compareTo(sum.monounsaturatedFat()), "The monounsaturated fat summed do not match");
        assertEquals(0, expectedSaturatedFat.compareTo(sum.saturatedFat()), "The saturated fat summed do not match");
        assertEquals(0, expectedPolyunsaturatedFat.compareTo(sum.polyunsaturatedFat()), "The polyunsaturated fat summed do not match");
        assertEquals(0, expectedTransFat.compareTo(sum.transFat()), "The trans fat summed do not match");
        assertEquals(0, expectedCholesterol.compareTo(sum.cholesterol()), "The cholesterol summed do not match");
        assertEquals(0, expectedSodium.compareTo(sum.sodium()), "The sodium summed do not match");
        assertEquals(0, expectedPotassium.compareTo(sum.potassium()), "The potassium summed do not match");
        assertEquals(0, expectedFiber.compareTo(sum.fiber()), "The fiber summed do not match");
        assertEquals(0, expectedSugar.compareTo(sum.sugar()), "The sugar summed do not match");
        assertEquals(0, expectedCalcium.compareTo(sum.calcium()), "The calcium summed do not match");
        assertEquals(0, expectedIron.compareTo(sum.iron()), "The iron summed do not match");
        assertEquals(0, expectedVitaminA.compareTo(sum.vitaminA()), "The vitamin A summed do not match");
        assertEquals(0, expectedVitaminC.compareTo(sum.vitaminC()), "The vitamin C summed do not match");
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("should correctly sum the nutritional information of meals when some fields are null")
    void shouldCorrectlySumTheNutritionalInformationOfMealsWhenSomeFieldsAreNull() {
        User user = createUser();
        userRepository.save(user);

        NutritionalInformation nutritionalInfo1 = createNutritionalInformation();

        nutritionalInfo1.setCarbohydrates(null);
        nutritionalInfo1.setProtein(null);

        NutritionalInformation nutritionalInfo2 = createNutritionalInformation();

        nutritionalInfo2.setFat(null);

        Food food1 = createFood();
        food1.setNutritionalInformation(createNutritionalInformation());
        food1.setUser(user);

        Food food2 = createFood();
        food2.setNutritionalInformation(createNutritionalInformation());
        food2.setUser(user);

        Food food3 = createFood();
        food3.setNutritionalInformation(nutritionalInfo1);
        food3.setUser(user);

        Food food4 = createFood();
        food4.setNutritionalInformation(nutritionalInfo2);
        food4.setUser(user);

        foodRepository.saveAll(List.of(food1, food2, food3, food4));

        Meal meal = createMeal();
        meal.setFoods(Set.of(food3, food4));
        meal.setUser(user);

        mealRepository.save(meal);

        MealLog mealLog = MealLog.builder()
                .type(MealType.LUNCH)
                .caloriesGoal(faker.number().numberBetween(50, 450))
                .time(LocalTime.now())
                .date(LocalDate.now())
                .foods(Set.of(food1, food2))
                .meals(Set.of(meal))
                .user(user)
                .build();

        mealLogRepository.save(mealLog);

        Optional<MealNutritionalInformationDTO> result = sut
                .sumMealsNutritionalInformationByMealLogIdAndUserId(mealLog.getId(), user.getId());

        assertTrue(result.isPresent(), "The result of the sum should not be empty.");

        MealNutritionalInformationDTO sum = result.get();

        BigDecimal expectedCalories = nutritionalInfo1.getCalories().add(nutritionalInfo2.getCalories());
        BigDecimal expectedCarbohydrates = nutritionalInfo1.getCarbohydrates().add(nutritionalInfo2.getCarbohydrates());
        BigDecimal expectedProtein = nutritionalInfo1.getProtein().add(nutritionalInfo2.getProtein());
        BigDecimal expectedFat = nutritionalInfo1.getFat().add(nutritionalInfo2.getFat());
        BigDecimal expectedMonounsaturatedFat = nutritionalInfo1.getMonounsaturatedFat().add(nutritionalInfo2.getMonounsaturatedFat());
        BigDecimal expectedSaturatedFat = nutritionalInfo1.getSaturatedFat().add(nutritionalInfo2.getSaturatedFat());
        BigDecimal expectedPolyunsaturatedFat = nutritionalInfo1.getPolyunsaturatedFat().add(nutritionalInfo2.getPolyunsaturatedFat());
        BigDecimal expectedTransFat = nutritionalInfo1.getTransFat().add(nutritionalInfo2.getTransFat());
        BigDecimal expectedCholesterol = nutritionalInfo1.getCholesterol().add(nutritionalInfo2.getCholesterol());
        BigDecimal expectedSodium = nutritionalInfo1.getSodium().add(nutritionalInfo2.getSodium());
        BigDecimal expectedPotassium = nutritionalInfo1.getPotassium().add(nutritionalInfo2.getPotassium());
        BigDecimal expectedFiber = nutritionalInfo1.getFiber().add(nutritionalInfo2.getFiber());
        BigDecimal expectedSugar = nutritionalInfo1.getSugar().add(nutritionalInfo2.getSugar());
        BigDecimal expectedCalcium = nutritionalInfo1.getCalcium().add(nutritionalInfo2.getCalcium());
        BigDecimal expectedIron = nutritionalInfo1.getIron().add(nutritionalInfo2.getIron());
        BigDecimal expectedVitaminA = nutritionalInfo1.getVitaminA().add(nutritionalInfo2.getVitaminA());
        BigDecimal expectedVitaminC = nutritionalInfo1.getVitaminC().add(nutritionalInfo2.getVitaminC());

        assertEquals(0, expectedCalories.compareTo(sum.calories()), "The calories summed do not match");
        assertEquals(0, expectedCarbohydrates.compareTo(sum.carbohydrates()), "The carbohydrates summed do not match");
        assertEquals(0, expectedProtein.compareTo(sum.protein()), "The proteins summed do not match");
        assertEquals(0, expectedFat.compareTo(sum.fat()), "The fat summed do not match");
        assertEquals(0, expectedMonounsaturatedFat.compareTo(sum.monounsaturatedFat()), "The monounsaturated fat summed do not match");
        assertEquals(0, expectedSaturatedFat.compareTo(sum.saturatedFat()), "The saturated fat summed do not match");
        assertEquals(0, expectedPolyunsaturatedFat.compareTo(sum.polyunsaturatedFat()), "The polyunsaturated fat summed do not match");
        assertEquals(0, expectedTransFat.compareTo(sum.transFat()), "The trans fat summed do not match");
        assertEquals(0, expectedCholesterol.compareTo(sum.cholesterol()), "The cholesterol summed do not match");
        assertEquals(0, expectedSodium.compareTo(sum.sodium()), "The sodium summed do not match");
        assertEquals(0, expectedPotassium.compareTo(sum.potassium()), "The potassium summed do not match");
        assertEquals(0, expectedFiber.compareTo(sum.fiber()), "The fiber summed do not match");
        assertEquals(0, expectedSugar.compareTo(sum.sugar()), "The sugar summed do not match");
        assertEquals(0, expectedCalcium.compareTo(sum.calcium()), "The calcium summed do not match");
        assertEquals(0, expectedIron.compareTo(sum.iron()), "The iron summed do not match");
        assertEquals(0, expectedVitaminA.compareTo(sum.vitaminA()), "The vitamin A summed do not match");
        assertEquals(0, expectedVitaminC.compareTo(sum.vitaminC()), "The vitamin C summed do not match");
    }
}