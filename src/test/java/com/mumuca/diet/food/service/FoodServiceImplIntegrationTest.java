package com.mumuca.diet.food.service;

import com.mumuca.diet.meal.dto.MealDTO;
import com.mumuca.diet.food.dto.*;
import com.mumuca.diet.food.model.Food;
import com.mumuca.diet.food.model.NutritionalInformation;
import com.mumuca.diet.food.model.Portion;
import com.mumuca.diet.food.repository.FoodRepository;
import com.mumuca.diet.food.repository.NutritionalInformationRepository;
import com.mumuca.diet.food.repository.PortionRepository;
import com.mumuca.diet.food.service.impl.FoodServiceImpl;
import com.mumuca.diet.meal.model.Meal;
import com.mumuca.diet.food.model.Unit;
import com.mumuca.diet.auth.model.User;
import com.mumuca.diet.meal.repository.MealRepository;
import com.mumuca.diet.auth.repository.UserRepository;
import com.mumuca.diet.testutil.EntityGeneratorUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import static com.mumuca.diet.testutil.EntityGeneratorUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("FoodServiceImpl Integration Tests")
// TODO: test the unhappy paths
class FoodServiceImplIntegrationTest {

    @Autowired
    private FoodServiceImpl sut;

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
    @DisplayName("createFood tests")
    class CreateFoodTests {
        @Test
        @Transactional
        @DisplayName("should be able to create food")
        void shouldBeAbleToCreateFood() {
            // Arrange
            CreatePortionDTO portion = new CreatePortionDTO(
                    100,
                    Unit.GRAM,
                    "Porção padrão de 100g"
            );

            CreateNutritionalInformationDTO nutritionalInformation = new CreateNutritionalInformationDTO(
                    new BigDecimal("200.5"), // Calories
                    new BigDecimal("30.0"), // Carbohydrates
                    new BigDecimal("15.0"), // Protein
                    new BigDecimal("10.0"), // Fat
                    new BigDecimal("5.0"), // Monounsaturated fat
                    new BigDecimal("3.0"), // Saturated fat
                    new BigDecimal("2.0"), // Polyunsaturated fat
                    new BigDecimal("1.0"), // Trans fat
                    new BigDecimal("50.0"), // Cholesterol
                    new BigDecimal("100.0"), // Sodium
                    new BigDecimal("200.0"), // Potassium
                    new BigDecimal("8.0"), // Fiber
                    new BigDecimal("12.0"), // Sugar
                    new BigDecimal("50.0"), // Calcium
                    new BigDecimal("5.0"), // Iron
                    new BigDecimal("900.0"), // Vitamin A
                    new BigDecimal("30.0") // Vitamin C
            );

            CreateFoodDTO createFoodDTO = new CreateFoodDTO(
                    "Arroz Integral",
                    "Marca Exemplo",
                    "Arroz integral orgânico",
                    portion,
                    nutritionalInformation
            );

            User user = EntityGeneratorUtil.createUser();
            userRepository.save(user);

            // Act
            FoodDTO result = sut.createFood(createFoodDTO, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isNotNull();
            assertThat(result.title()).isEqualTo("Arroz Integral");
            assertThat(result.brand()).isEqualTo("Marca Exemplo");
            assertThat(result.description()).isEqualTo("Arroz integral orgânico");

            assertThat(result.portion()).isNotNull();
            assertThat(result.portion().id()).isNotNull();
            assertThat(result.portion().amount()).isEqualTo(100);
            assertThat(result.portion().unit()).isEqualTo(Unit.GRAM);
            assertThat(result.portion().description()).isEqualTo("Porção padrão de 100g");

            assertThat(result.nutritionalInformation()).isNotNull();
            assertThat(result.nutritionalInformation().id()).isNotNull();
            assertThat(result.nutritionalInformation().calories()).isEqualTo(new BigDecimal("200.5"));
            assertThat(result.nutritionalInformation().carbohydrates()).isEqualTo(new BigDecimal("30.0"));
            assertThat(result.nutritionalInformation().protein()).isEqualTo(new BigDecimal("15.0"));
            assertThat(result.nutritionalInformation().fat()).isEqualTo(new BigDecimal("10.0"));
            assertThat(result.nutritionalInformation().monounsaturatedFat()).isEqualTo(new BigDecimal("5.0"));
            assertThat(result.nutritionalInformation().saturatedFat()).isEqualTo(new BigDecimal("3.0"));
            assertThat(result.nutritionalInformation().polyunsaturatedFat()).isEqualTo(new BigDecimal("2.0"));
            assertThat(result.nutritionalInformation().transFat()).isEqualTo(new BigDecimal("1.0"));
            assertThat(result.nutritionalInformation().cholesterol()).isEqualTo(new BigDecimal("50.0"));
            assertThat(result.nutritionalInformation().sodium()).isEqualTo(new BigDecimal("100.0"));
            assertThat(result.nutritionalInformation().potassium()).isEqualTo(new BigDecimal("200.0"));
            assertThat(result.nutritionalInformation().fiber()).isEqualTo(new BigDecimal("8.0"));
            assertThat(result.nutritionalInformation().sugar()).isEqualTo(new BigDecimal("12.0"));
            assertThat(result.nutritionalInformation().calcium()).isEqualTo(new BigDecimal("50.0"));
            assertThat(result.nutritionalInformation().iron()).isEqualTo(new BigDecimal("5.0"));
            assertThat(result.nutritionalInformation().vitaminA()).isEqualTo(new BigDecimal("900.0"));
            assertThat(result.nutritionalInformation().vitaminC()).isEqualTo(new BigDecimal("30.0"));

            var foodInDatabase = foodRepository.findById(result.id()).orElseThrow();

            assertThat(foodInDatabase).isNotNull();
            assertThat(foodInDatabase.getId()).isEqualTo(result.id());
            assertThat(foodInDatabase.getTitle()).isEqualTo("Arroz Integral");
            assertThat(foodInDatabase.getBrand()).isEqualTo("Marca Exemplo");
            assertThat(foodInDatabase.getDescription()).isEqualTo("Arroz integral orgânico");

            assertThat(foodInDatabase.getPortion()).isNotNull();
            assertThat(foodInDatabase.getPortion().getId()).isEqualTo(result.portion().id());
            assertThat(foodInDatabase.getPortion().getAmount()).isEqualTo(result.portion().amount());
            assertThat(foodInDatabase.getPortion().getUnit()).isEqualTo(result.portion().unit());
            assertThat(foodInDatabase.getPortion().getDescription()).isEqualTo(result.portion().description());

            assertThat(foodInDatabase.getNutritionalInformation()).isNotNull();
            assertThat(foodInDatabase.getNutritionalInformation().getId()).isEqualTo(result.nutritionalInformation().id());
            assertThat(foodInDatabase.getNutritionalInformation().getCalories()).isEqualTo(result.nutritionalInformation().calories());
            assertThat(foodInDatabase.getNutritionalInformation().getCarbohydrates()).isEqualTo(result.nutritionalInformation().carbohydrates());
            assertThat(foodInDatabase.getNutritionalInformation().getProtein()).isEqualTo(result.nutritionalInformation().protein());
            assertThat(foodInDatabase.getNutritionalInformation().getFat()).isEqualTo(result.nutritionalInformation().fat());
            assertThat(foodInDatabase.getNutritionalInformation().getMonounsaturatedFat()).isEqualTo(result.nutritionalInformation().monounsaturatedFat());
            assertThat(foodInDatabase.getNutritionalInformation().getSaturatedFat()).isEqualTo(result.nutritionalInformation().saturatedFat());
            assertThat(foodInDatabase.getNutritionalInformation().getPolyunsaturatedFat()).isEqualTo(result.nutritionalInformation().polyunsaturatedFat());
            assertThat(foodInDatabase.getNutritionalInformation().getTransFat()).isEqualTo(result.nutritionalInformation().transFat());
            assertThat(foodInDatabase.getNutritionalInformation().getCholesterol()).isEqualTo(result.nutritionalInformation().cholesterol());
            assertThat(foodInDatabase.getNutritionalInformation().getSodium()).isEqualTo(result.nutritionalInformation().sodium());
            assertThat(foodInDatabase.getNutritionalInformation().getPotassium()).isEqualTo(result.nutritionalInformation().potassium());
            assertThat(foodInDatabase.getNutritionalInformation().getFiber()).isEqualTo(result.nutritionalInformation().fiber());
            assertThat(foodInDatabase.getNutritionalInformation().getSugar()).isEqualTo(result.nutritionalInformation().sugar());
            assertThat(foodInDatabase.getNutritionalInformation().getCalcium()).isEqualTo(result.nutritionalInformation().calcium());
            assertThat(foodInDatabase.getNutritionalInformation().getIron()).isEqualTo(result.nutritionalInformation().iron());
            assertThat(foodInDatabase.getNutritionalInformation().getVitaminA()).isEqualTo(result.nutritionalInformation().vitaminA());
            assertThat(foodInDatabase.getNutritionalInformation().getVitaminC()).isEqualTo(result.nutritionalInformation().vitaminC());
        }
    }

    @Nested
    @DisplayName("getFood tests")
    class GetFoodTests {
        @Test
        @Transactional
        @DisplayName("should be able to get food")
        void shouldBeAbleToGetFood() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food = createFood();
            food.setUser(user);
            foodRepository.save(food);

            NutritionalInformation nutritionalInformation = createNutritionalInformation();
            nutritionalInformation.setFood(food);
            food.setNutritionalInformation(nutritionalInformation);
            nutritionalInformationRepository.save(nutritionalInformation);

            Portion portion = createPortion();
            portion.setFood(food);
            food.setPortion(portion);
            portionRepository.save(portion);

            // Act
            FoodDTO result = sut.getFood(food.getId(), user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(food.getId());
            assertThat(result.title()).isEqualTo(food.getTitle());
            assertThat(result.brand()).isEqualTo(food.getBrand());
            assertThat(result.description()).isEqualTo(food.getDescription());

            assertThat(result.portion()).isNotNull();
            assertThat(result.portion().id()).isNotNull();
            assertThat(result.portion().amount()).isEqualTo(food.getPortion().getAmount());
            assertThat(result.portion().unit()).isEqualTo(food.getPortion().getUnit());
            assertThat(result.portion().description()).isEqualTo(food.getPortion().getDescription());

            assertThat(result.nutritionalInformation()).isNotNull();
            assertThat(result.nutritionalInformation().id()).isEqualTo(food.getNutritionalInformation().getId());

            assertThat(result.nutritionalInformation().calories()).isEqualTo(food.getNutritionalInformation().getCalories());
            assertThat(result.nutritionalInformation().carbohydrates()).isEqualTo(food.getNutritionalInformation().getCarbohydrates());
            assertThat(result.nutritionalInformation().protein()).isEqualTo(food.getNutritionalInformation().getProtein());
            assertThat(result.nutritionalInformation().fat()).isEqualTo(food.getNutritionalInformation().getFat());
            assertThat(result.nutritionalInformation().monounsaturatedFat()).isEqualTo(food.getNutritionalInformation().getMonounsaturatedFat());
            assertThat(result.nutritionalInformation().saturatedFat()).isEqualTo(food.getNutritionalInformation().getSaturatedFat());
            assertThat(result.nutritionalInformation().polyunsaturatedFat()).isEqualTo(food.getNutritionalInformation().getPolyunsaturatedFat());
            assertThat(result.nutritionalInformation().transFat()).isEqualTo(food.getNutritionalInformation().getTransFat());
            assertThat(result.nutritionalInformation().cholesterol()).isEqualTo(food.getNutritionalInformation().getCholesterol());
            assertThat(result.nutritionalInformation().sodium()).isEqualTo(food.getNutritionalInformation().getSodium());
            assertThat(result.nutritionalInformation().potassium()).isEqualTo(food.getNutritionalInformation().getPotassium());
            assertThat(result.nutritionalInformation().fiber()).isEqualTo(food.getNutritionalInformation().getFiber());
            assertThat(result.nutritionalInformation().sugar()).isEqualTo(food.getNutritionalInformation().getSugar());
            assertThat(result.nutritionalInformation().calcium()).isEqualTo(food.getNutritionalInformation().getCalcium());
            assertThat(result.nutritionalInformation().iron()).isEqualTo(food.getNutritionalInformation().getIron());
            assertThat(result.nutritionalInformation().vitaminA()).isEqualTo(food.getNutritionalInformation().getVitaminA());
            assertThat(result.nutritionalInformation().vitaminC()).isEqualTo(food.getNutritionalInformation().getVitaminC());
        }
    }

    @Nested
    @DisplayName("getFoodNutritionalInformation tests")
    class GetFoodNutritionalInformationTests {
        @Test
        @Transactional
        @DisplayName("should be able to get food nutritional information")
        void shouldBeAbleToGetFoodNutritionalInformation() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food = createFood();
            food.setUser(user);
            foodRepository.save(food);

            NutritionalInformation nutritionalInformation = createNutritionalInformation();
            nutritionalInformation.setFood(food);
            food.setNutritionalInformation(nutritionalInformation);
            nutritionalInformationRepository.save(nutritionalInformation);

            // Act
            NutritionalInformationDTO result = sut.getFoodNutritionalInformation(food.getId(), user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.calories()).isEqualTo(food.getNutritionalInformation().getCalories());
            assertThat(result.carbohydrates()).isEqualTo(food.getNutritionalInformation().getCarbohydrates());
            assertThat(result.protein()).isEqualTo(food.getNutritionalInformation().getProtein());
            assertThat(result.fat()).isEqualTo(food.getNutritionalInformation().getFat());
            assertThat(result.monounsaturatedFat()).isEqualTo(food.getNutritionalInformation().getMonounsaturatedFat());
            assertThat(result.saturatedFat()).isEqualTo(food.getNutritionalInformation().getSaturatedFat());
            assertThat(result.polyunsaturatedFat()).isEqualTo(food.getNutritionalInformation().getPolyunsaturatedFat());
            assertThat(result.transFat()).isEqualTo(food.getNutritionalInformation().getTransFat());
            assertThat(result.cholesterol()).isEqualTo(food.getNutritionalInformation().getCholesterol());
            assertThat(result.sodium()).isEqualTo(food.getNutritionalInformation().getSodium());
            assertThat(result.potassium()).isEqualTo(food.getNutritionalInformation().getPotassium());
            assertThat(result.fiber()).isEqualTo(food.getNutritionalInformation().getFiber());
            assertThat(result.sugar()).isEqualTo(food.getNutritionalInformation().getSugar());
            assertThat(result.calcium()).isEqualTo(food.getNutritionalInformation().getCalcium());
            assertThat(result.iron()).isEqualTo(food.getNutritionalInformation().getIron());
            assertThat(result.vitaminA()).isEqualTo(food.getNutritionalInformation().getVitaminA());
            assertThat(result.vitaminC()).isEqualTo(food.getNutritionalInformation().getVitaminC());
        }
    }

    @Nested
    @DisplayName("updateFood tests")
    class UpdateFoodTests {
        @Test
        @Transactional
        @DisplayName("should be able to update food")
        void shouldBeAbleToUpdateFood() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food = createFood();
            food.setUser(user);
            foodRepository.save(food);

            NutritionalInformation nutritionalInformation = createNutritionalInformation();
            nutritionalInformation.setFood(food);
            food.setNutritionalInformation(nutritionalInformation);
            nutritionalInformationRepository.save(nutritionalInformation);

            Portion portion = createPortion();
            portion.setFood(food);
            food.setPortion(portion);
            portionRepository.save(portion);

            UpdatePortionDTO updatePortionDTO = new UpdatePortionDTO(
                    2,
                    Unit.GRAM,
                    "2 grams of food"
            );

            UpdateNutritionalInformationDTO updateNutritionalInformationDTO = new UpdateNutritionalInformationDTO(
                    new BigDecimal("150.0"),
                    new BigDecimal("20.0"),
                    new BigDecimal("5.0"),
                    new BigDecimal("10.0"),
                    new BigDecimal("3.0"),
                    new BigDecimal("2.0"),
                    new BigDecimal("1.0"),
                    new BigDecimal("0.5"),
                    new BigDecimal("30.0"),
                    new BigDecimal("200.0"),
                    new BigDecimal("300.0"),
                    new BigDecimal("4.0"),
                    new BigDecimal("10.0"),
                    new BigDecimal("50.0"),
                    new BigDecimal("2.0"),
                    new BigDecimal("100.0"),
                    new BigDecimal("60.0")
            );

            UpdateFoodDTO updateFoodDTO = new UpdateFoodDTO(
                    "Updated Food Title",
                    "Updated Brand",
                    "Updated Description",
                    updatePortionDTO,
                    updateNutritionalInformationDTO
            );


            // Act
            sut.updateFood(food.getId(), updateFoodDTO, user.getId());

            // Assert
            var foodInDatabase = foodRepository.findById(food.getId()).orElseThrow();

            assertThat(foodInDatabase.getTitle()).isEqualTo("Updated Food Title");
            assertThat(foodInDatabase.getBrand()).isEqualTo("Updated Brand");
            assertThat(foodInDatabase.getDescription()).isEqualTo("Updated Description");

            Portion updatedPortion = foodInDatabase.getPortion();
            assertThat(updatedPortion.getAmount()).isEqualTo(2);
            assertThat(updatedPortion.getUnit()).isEqualTo(Unit.GRAM);
            assertThat(updatedPortion.getDescription()).isEqualTo("2 grams of food");

            NutritionalInformation updatedNutritionalInformation = foodInDatabase.getNutritionalInformation();
            assertThat(updatedNutritionalInformation.getCalories()).isEqualTo(new BigDecimal("150.0"));
            assertThat(updatedNutritionalInformation.getCarbohydrates()).isEqualTo(new BigDecimal("20.0"));
            assertThat(updatedNutritionalInformation.getProtein()).isEqualTo(new BigDecimal("5.0"));
            assertThat(updatedNutritionalInformation.getFat()).isEqualTo(new BigDecimal("10.0"));
            assertThat(updatedNutritionalInformation.getMonounsaturatedFat()).isEqualTo(new BigDecimal("3.0"));
            assertThat(updatedNutritionalInformation.getSaturatedFat()).isEqualTo(new BigDecimal("2.0"));
            assertThat(updatedNutritionalInformation.getPolyunsaturatedFat()).isEqualTo(new BigDecimal("1.0"));
            assertThat(updatedNutritionalInformation.getTransFat()).isEqualTo(new BigDecimal("0.5"));
            assertThat(updatedNutritionalInformation.getCholesterol()).isEqualTo(new BigDecimal("30.0"));
            assertThat(updatedNutritionalInformation.getSodium()).isEqualTo(new BigDecimal("200.0"));
            assertThat(updatedNutritionalInformation.getPotassium()).isEqualTo(new BigDecimal("300.0"));
            assertThat(updatedNutritionalInformation.getFiber()).isEqualTo(new BigDecimal("4.0"));
            assertThat(updatedNutritionalInformation.getSugar()).isEqualTo(new BigDecimal("10.0"));
            assertThat(updatedNutritionalInformation.getCalcium()).isEqualTo(new BigDecimal("50.0"));
            assertThat(updatedNutritionalInformation.getIron()).isEqualTo(new BigDecimal("2.0"));
            assertThat(updatedNutritionalInformation.getVitaminA()).isEqualTo(new BigDecimal("100.0"));
            assertThat(updatedNutritionalInformation.getVitaminC()).isEqualTo(new BigDecimal("60.0"));
        }
    }

    @Nested
    @DisplayName("deleteFood tests")
    class DeleteFoodTests {
        @Test
        @Transactional
        @DisplayName("should be able to delete food")
        void shouldBeAbleToDeleteFood() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food = createFood();
            food.setUser(user);
            foodRepository.save(food);

            NutritionalInformation nutritionalInformation = createNutritionalInformation();
            nutritionalInformation.setFood(food);
            food.setNutritionalInformation(nutritionalInformation);
            nutritionalInformationRepository.save(nutritionalInformation);

            // Act
            sut.deleteFood(food.getId(), user.getId());

            // Assert
            assertThat(foodRepository.findById(food.getId())).isEmpty();
        }
    }

    @Nested
    @DisplayName("getFoodMeals tests")
    class GetFoodMealsTests {
        @Test
        @Transactional
        @DisplayName("should be able to list meals that contain food")
        void shouldBeAbleToListMealsThatContainFood() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food = createFood();
            food.setUser(user);
            foodRepository.save(food);

            NutritionalInformation nutritionalInformation = createNutritionalInformation();
            nutritionalInformation.setFood(food);
            food.setNutritionalInformation(nutritionalInformation);
            nutritionalInformationRepository.save(nutritionalInformation);

            Portion portion = createPortion();
            portion.setFood(food);
            food.setPortion(portion);
            portionRepository.save(portion);

            Set<Food> foodSet = new HashSet<>();
            foodSet.add(food);

            Meal meal1 = createMeal();
            meal1.setFoods(foodSet);
            meal1.setUser(user);

            Meal meal2 = createMeal();
            meal2.setFoods(foodSet);
            meal2.setUser(user);

            Meal meal3 = createMeal();
            meal3.setFoods(foodSet);
            meal3.setUser(user);

            Meal meal4 = createMeal();
            meal4.setUser(user);

            mealRepository.save(meal1);
            mealRepository.save(meal2);
            mealRepository.save(meal3);
            mealRepository.save(meal4);

            // Act
            List<MealDTO> meals = sut.getFoodMeals(food.getId(), user.getId());

            // Assert
            assertThat(meals.size()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("getFoods tests")
    class GetFoodsTests {
        @Test
        @Transactional
        @DisplayName("should be able to paginate foods")
        void shouldBeAbleToPaginateFoods() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Food food = createFood();
            food.setUser(user);
            foodRepository.save(food);

            NutritionalInformation nutritionalInformation = createNutritionalInformation();
            nutritionalInformation.setFood(food);
            food.setNutritionalInformation(nutritionalInformation);
            nutritionalInformationRepository.save(nutritionalInformation);

            Portion portion = createPortion();
            portion.setFood(food);
            food.setPortion(portion);
            portionRepository.save(portion);

            // Act
            Page<FoodDTO> foods = sut.getFoods(Pageable.unpaged(), user.getId());

            // Assert
            assertThat(foods).isNotNull();
            assertThat(foods.getContent()).isNotNull();
            assertThat(foods.getContent().size()).isEqualTo(1);
            assertThat(foods.getNumberOfElements()).isEqualTo(1);
            assertThat(foods.getTotalElements()).isEqualTo(1);
            assertThat(foods.getTotalPages()).isEqualTo(1);
        }
    }
}