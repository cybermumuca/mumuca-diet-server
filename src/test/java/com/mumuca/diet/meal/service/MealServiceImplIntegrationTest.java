package com.mumuca.diet.meal.service;


import com.mumuca.diet.dto.meal.*;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.food.model.Food;
import com.mumuca.diet.food.model.NutritionalInformation;
import com.mumuca.diet.food.model.Portion;
import com.mumuca.diet.food.repository.FoodRepository;
import com.mumuca.diet.food.repository.NutritionalInformationRepository;
import com.mumuca.diet.food.repository.PortionRepository;
import com.mumuca.diet.model.Meal;
import com.mumuca.diet.model.MealType;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.MealRepository;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.service.impl.MealServiceImpl;
import com.mumuca.diet.testutil.EntityGeneratorUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.mumuca.diet.testutil.EntityGeneratorUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("MealServiceImpl Integration Tests")
public class MealServiceImplIntegrationTest {

    @Autowired
    private MealServiceImpl sut;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private NutritionalInformationRepository nutritionalInformationRepository;

    @Autowired
    private PortionRepository portionRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("createMeal tests")
    class CreateMealTests {
        @Test
        @Transactional
        @DisplayName("should be able to create meal")
        void shouldBeAbleToCreateMeal() {
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

            CreateMealDTO createMealDTO = new CreateMealDTO("Test Title", "Test Description", MealType.DINNER, Collections.singletonList(food.getId()));

            // Act
            MealDTO result = sut.createMeal(createMealDTO, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isNotNull();
            assertThat(result.title()).isEqualTo("Test Title");
            assertThat(result.description()).isEqualTo("Test Description");
            assertThat(result.type()).isEqualTo(MealType.DINNER);

            Meal mealInDatabase = entityManager
                    .createQuery("SELECT m FROM Meal m JOIN FETCH m.foods WHERE m.id = :id", Meal.class)
                    .setParameter("id", result.id())
                    .getSingleResult();

            assertThat(mealInDatabase).isNotNull();
            assertThat(mealInDatabase.getTitle()).isEqualTo("Test Title");
            assertThat(mealInDatabase.getDescription()).isEqualTo("Test Description");
            assertThat(mealInDatabase.getType()).isEqualTo(MealType.DINNER);

            assertThat(mealInDatabase.getFoods().size()).isEqualTo(1);

            Food mealFood = foodRepository.findById(mealInDatabase.getFoods().stream().toList().getFirst().getId()).orElseThrow();

            assertThat(mealFood.getId()).isEqualTo(food.getId());
            assertThat(mealFood.getTitle()).isEqualTo(food.getTitle());
            assertThat(mealFood.getDescription()).isEqualTo(food.getDescription());
            assertThat(mealFood.getBrand()).isEqualTo(food.getBrand());
        }

        @Test
        @Transactional
        @DisplayName("should be able to ignore if some food IDs are not found")
        void shouldBeAbleToIgnoreIfSomeFoodIdsNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            List<String> invalidFoodIds = List.of("invalid-food-id");

            CreateMealDTO createMealDTO = new CreateMealDTO(
                    "Meal Title",
                    "Meal Description",
                    MealType.BREAKFAST,
                    invalidFoodIds
            );

            // Act
            MealDTO result = sut.createMeal(createMealDTO, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isNotNull();
            assertThat(result.title()).isEqualTo("Meal Title");
            assertThat(result.description()).isEqualTo("Meal Description");
            assertThat(result.type()).isEqualTo(MealType.BREAKFAST);

            Meal mealInDatabase = mealRepository.findById(result.id()).get();

            assertThat(mealInDatabase).isNotNull();
            assertThat(mealInDatabase.getTitle()).isEqualTo("Meal Title");
            assertThat(mealInDatabase.getDescription()).isEqualTo("Meal Description");
            assertThat(mealInDatabase.getType()).isEqualTo(MealType.BREAKFAST);

            Hibernate.initialize(mealInDatabase.getFoods());

            assertThat(mealInDatabase.getFoods().size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("getMeal tests")
    class GetMealTests {
        @Test
        @Transactional
        @DisplayName("should be able to get meal with foods")
        void shouldBeAbleToGetMealWithFoods() {
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

            Meal meal = createMeal();
            meal.setUser(user);
            meal.setFoods(Set.of(food));
            mealRepository.save(meal);

            // Act
            MealWithFoodsDTO result = sut.getMeal(meal.getId(), user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(meal.getId());
            assertThat(result.title()).isEqualTo(meal.getTitle());
            assertThat(result.description()).isEqualTo(meal.getDescription());
            assertThat(result.type()).isEqualTo(meal.getType());

            assertThat(result.foods().size()).isEqualTo(1);

            var mealFood = result.foods().getFirst();
            assertThat(mealFood.id()).isEqualTo(food.getId());
            assertThat(mealFood.brand()).isEqualTo(food.getBrand());
            assertThat(mealFood.title()).isEqualTo(food.getTitle());
            assertThat(mealFood.description()).isEqualTo(food.getDescription());

            var mealFoodPortion = mealFood.portion();
            assertThat(mealFoodPortion).isNotNull();
            assertThat(mealFoodPortion.id()).isEqualTo(portion.getId());
            assertThat(mealFoodPortion.amount()).isEqualTo(portion.getAmount());
            assertThat(mealFoodPortion.description()).isEqualTo(portion.getDescription());
            assertThat(mealFoodPortion.unit()).isEqualTo(portion.getUnit());

            var mealFoodNutritionalInformation = mealFood.nutritionalInformation();
            assertThat(mealFoodNutritionalInformation).isNotNull();
            assertThat(mealFoodNutritionalInformation.id()).isEqualTo(nutritionalInformation.getId());
            assertThat(mealFoodNutritionalInformation.calories()).isEqualTo(nutritionalInformation.getCalories());
            assertThat(mealFoodNutritionalInformation.carbohydrates()).isEqualTo(nutritionalInformation.getCarbohydrates());
            assertThat(mealFoodNutritionalInformation.protein()).isEqualTo(nutritionalInformation.getProtein());
            assertThat(mealFoodNutritionalInformation.fat()).isEqualTo(nutritionalInformation.getFat());
            assertThat(mealFoodNutritionalInformation.monounsaturatedFat()).isEqualTo(nutritionalInformation.getMonounsaturatedFat());
            assertThat(mealFoodNutritionalInformation.saturatedFat()).isEqualTo(nutritionalInformation.getSaturatedFat());
            assertThat(mealFoodNutritionalInformation.polyunsaturatedFat()).isEqualTo(nutritionalInformation.getPolyunsaturatedFat());
            assertThat(mealFoodNutritionalInformation.transFat()).isEqualTo(nutritionalInformation.getTransFat());
            assertThat(mealFoodNutritionalInformation.cholesterol()).isEqualTo(nutritionalInformation.getCholesterol());
            assertThat(mealFoodNutritionalInformation.sodium()).isEqualTo(nutritionalInformation.getSodium());
            assertThat(mealFoodNutritionalInformation.potassium()).isEqualTo(nutritionalInformation.getPotassium());
            assertThat(mealFoodNutritionalInformation.fiber()).isEqualTo(nutritionalInformation.getFiber());
            assertThat(mealFoodNutritionalInformation.sugar()).isEqualTo(nutritionalInformation.getSugar());
            assertThat(mealFoodNutritionalInformation.calcium()).isEqualTo(nutritionalInformation.getCalcium());
            assertThat(mealFoodNutritionalInformation.iron()).isEqualTo(nutritionalInformation.getIron());
            assertThat(mealFoodNutritionalInformation.vitaminA()).isEqualTo(nutritionalInformation.getVitaminA());
            assertThat(mealFoodNutritionalInformation.vitaminC()).isEqualTo(nutritionalInformation.getVitaminC());
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if the meal does not exist")
        void shouldThrowResourceNotFoundExceptionIfTheMealDoesNotExist() {
            // Act & Assert
            assertThatThrownBy(() -> sut.getMeal(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal not found");
        }
    }

    @Nested
    @DisplayName("getMealNutritionalInformation tests")
    class GetMealNutritionalInformationTests {
        @Test
        @Transactional
        @DisplayName("should be able to get meal nutritional information")
        void shouldBeAbleToGetMealNutritionalInformation() {
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

            Meal meal = createMeal();
            meal.setUser(user);
            meal.setFoods(Set.of(food));
            mealRepository.save(meal);

            // Act
            Optional<MealNutritionalInformationDTO> result = sut.getMealNutritionalInformation(meal.getId(), user.getId());

            // Assert
            assertThat(result).isPresent();

            MealNutritionalInformationDTO dto = result.get();
            assertThat(dto.calories()).isEqualByComparingTo(nutritionalInformation.getCalories());
            assertThat(dto.carbohydrates()).isEqualByComparingTo(nutritionalInformation.getCarbohydrates());
            assertThat(dto.protein()).isEqualByComparingTo(nutritionalInformation.getProtein());
            assertThat(dto.fat()).isEqualByComparingTo(nutritionalInformation.getFat());
            assertThat(dto.monounsaturatedFat()).isEqualByComparingTo(nutritionalInformation.getMonounsaturatedFat());
            assertThat(dto.saturatedFat()).isEqualByComparingTo(nutritionalInformation.getSaturatedFat());
            assertThat(dto.polyunsaturatedFat()).isEqualByComparingTo(nutritionalInformation.getPolyunsaturatedFat());
            assertThat(dto.transFat()).isEqualByComparingTo(nutritionalInformation.getTransFat());
            assertThat(dto.cholesterol()).isEqualByComparingTo(nutritionalInformation.getCholesterol());
            assertThat(dto.sodium()).isEqualByComparingTo(nutritionalInformation.getSodium());
            assertThat(dto.potassium()).isEqualByComparingTo(nutritionalInformation.getPotassium());
            assertThat(dto.fiber()).isEqualByComparingTo(nutritionalInformation.getFiber());
            assertThat(dto.sugar()).isEqualByComparingTo(nutritionalInformation.getSugar());
            assertThat(dto.calcium()).isEqualByComparingTo(nutritionalInformation.getCalcium());
            assertThat(dto.iron()).isEqualByComparingTo(nutritionalInformation.getIron());
            assertThat(dto.vitaminA()).isEqualByComparingTo(nutritionalInformation.getVitaminA());
            assertThat(dto.vitaminC()).isEqualByComparingTo(nutritionalInformation.getVitaminC());
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if the meal does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealDoesNotExist() {
            // Act & Assert
            assertThatThrownBy(() -> sut.getMealNutritionalInformation(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal not found.");
        }

        @Test
        @Transactional
        @DisplayName("should return empty if meal has no foods")
        void shouldReturnEmptyIfMealHasNoFoods() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal = createMeal();
            meal.setUser(user);
            mealRepository.save(meal);

            // Act
            Optional<MealNutritionalInformationDTO> result = sut.getMealNutritionalInformation(meal.getId(), user.getId());

            // Assert
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateMeal tests")
    class UpdateMealTests {

        @Test
        @Transactional
        @DisplayName("should be able to update meal")
        void shouldBeAbleToUpdateMeal() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal = createMeal();
            meal.setType(MealType.BREAKFAST);
            meal.setUser(user);
            mealRepository.save(meal);

            UpdateMealDTO updateMealDTO = new UpdateMealDTO(
                    "Updated Meal Title",
                    "Updated Meal Description",
                    MealType.DINNER
            );

            // Act
            MealDTO result = sut.updateMeal(meal.getId(), updateMealDTO, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo("Updated Meal Title");
            assertThat(result.description()).isEqualTo("Updated Meal Description");
            assertThat(result.type()).isEqualTo(MealType.DINNER);

            Meal updatedMealInDatabase = mealRepository
                    .findById(meal.getId())
                    .orElseThrow();
            assertThat(updatedMealInDatabase.getTitle()).isEqualTo(result.title());
            assertThat(updatedMealInDatabase.getDescription()).isEqualTo(result.description());
            assertThat(updatedMealInDatabase.getType()).isEqualTo(MealType.DINNER);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if the meal does not exist")
        void shouldThrowResourceNotFoundExceptionIfTheMealDoesNotExist() {
            // Arrange
            UpdateMealDTO updateMealDTO = new UpdateMealDTO(
                    "Updated Title",
                    "Updated Description",
                    MealType.DINNER
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.updateMeal(UUID.randomUUID().toString(), updateMealDTO, UUID.randomUUID().toString()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal not found.");
        }

        @Test
        @Transactional
        @DisplayName("should not update meal if title, description and type are the same")
        void shouldNotUpdateMealIfNoChanges() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal = createMeal();
            meal.setUser(user);
            mealRepository.save(meal);

            UpdateMealDTO updateMealDTO = new UpdateMealDTO(
                    meal.getTitle(),
                    meal.getDescription(),
                    meal.getType()
            );

            // Act
            MealDTO result = sut.updateMeal(meal.getId(), updateMealDTO, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo(meal.getTitle());
            assertThat(result.description()).isEqualTo(meal.getDescription());
            assertThat(result.type()).isEqualTo(meal.getType());

            Meal mealInDatabase = mealRepository.findById(meal.getId()).orElseThrow();

            assertThat(mealInDatabase.getTitle()).isEqualTo(meal.getTitle());
            assertThat(mealInDatabase.getDescription()).isEqualTo(meal.getDescription());
            assertThat(mealInDatabase.getType()).isEqualTo(meal.getType());
        }
    }

    @Nested
    @DisplayName("deleteMeal tests")
    class DeleteMealTests {
        @Test
        @Transactional
        @DisplayName("should be able to delete meal if it exists and belongs to the user")
        void shouldBeAbleToDeleteMealIfItExistsAndBelongsToUser() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal = createMeal();
            meal.setUser(user);
            mealRepository.save(meal);

            // Act
            sut.deleteMeal(meal.getId(), user.getId());

            // Assert
            Optional<Meal> deletedMeal = mealRepository.findById(meal.getId());
            assertThat(deletedMeal).isEmpty();
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if the meal does not exist")
        void shouldThrowResourceNotFoundExceptionIfMealDoesNotExist() {
            // Act & Assert
            assertThatThrownBy(() -> sut.deleteMeal(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal not found.");
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if the meal does not belong to the user")
        void shouldThrowResourceNotFoundExceptionIfMealDoesNotBelongToUser() {
            // Arrange
            User owner = createUser();
            userRepository.save(owner);

            User anotherUser = createUser();
            userRepository.save(anotherUser);

            Meal meal = createMeal();
            meal.setUser(owner);
            mealRepository.save(meal);

            // Act & Assert
            assertThatThrownBy(() -> sut.deleteMeal(meal.getId(), anotherUser.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal not found.");
        }
    }

    @Nested
    @DisplayName("removeFoodsFromMeal tests")
    class RemoveFoodsFromMealTests {

        @Test
        @Transactional
        @DisplayName("should be able to remove specified foods from the meal")
        void shouldBeAbleToRemoveSpecifiedFoodsFromMeal() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal = createMeal();
            meal.setUser(user);

            Food food1 = createFood();
            food1.setUser(user);
            Food food2 = createFood();
            food2.setUser(user);

            foodRepository.saveAll(List.of(food1, food2));
            meal.setFoods(new LinkedHashSet<>(List.of(food1, food2)));
            mealRepository.save(meal);

            // Act
            sut.removeFoodsFromMeal(meal.getId(), List.of(food1.getId()), user.getId());

            // Assert
            Meal updatedMealInDatabase = mealRepository
                    .findByIdAndUserIdWithFoods(meal.getId(), user.getId())
                    .orElseThrow();

            assertThat(updatedMealInDatabase.getFoods()).doesNotContain(food1);
            assertThat(updatedMealInDatabase.getFoods()).contains(food2);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if the meal does not exist")
        void shouldThrowResourceNotFoundExceptionIfTheMealDoesNotExist() {
            // Act & Assert
            assertThatThrownBy(() -> sut.removeFoodsFromMeal(UUID.randomUUID().toString(), List.of("food-id"), "user-id"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal not found.");
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if some foods are not found")
        void shouldThrowResourceNotFoundExceptionIfSomeFoodsNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal = createMeal();
            meal.setUser(user);
            mealRepository.save(meal);

            // Act & Assert
            assertThatThrownBy(() -> sut.removeFoodsFromMeal(meal.getId(), List.of("invalid-food-id"), user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Some foods were not found or do not belong to the user.");
        }
    }

    @Nested
    @DisplayName("addFoodsToMeal tests")
    class AddFoodsToMealTests {

        @Test
        @Transactional
        @DisplayName("should be able to add unique foods to the meal")
        void shouldBeAbleToAddUniqueFoodsToMeal() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal = createMeal();
            meal.setFoods(new LinkedHashSet<>());
            meal.setUser(user);
            mealRepository.save(meal);

            Food food1 = createFood();
            food1.setUser(user);
            Food food2 = createFood();
            food2.setUser(user);

            foodRepository.saveAll(List.of(food1, food2));

            // Act
            sut.addFoodsToMeal(meal.getId(), List.of(food1.getId(), food2.getId()), user.getId());

            // Assert
            Meal updatedMealInDatabase = mealRepository
                    .findByIdAndUserIdWithFoods(meal.getId(), user.getId())
                    .orElseThrow();
            assertThat(updatedMealInDatabase.getFoods()).contains(food1, food2);
        }

        @Test
        @Transactional
        @DisplayName("should not add duplicate foods to the meal")
        void shouldNotAddDuplicateFoodsToMeal() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal = createMeal();
            meal.setUser(user);

            Food food = createFood();
            food.setUser(user);
            foodRepository.save(food);

            meal.setFoods(Set.of(food));
            mealRepository.save(meal);

            // Act
            sut.addFoodsToMeal(meal.getId(), List.of(food.getId()), user.getId());

            // Assert
            Meal updatedMealInDatabase = mealRepository.findByIdAndUserIdWithFoods(meal.getId(), user.getId()).get();
            assertThat(updatedMealInDatabase.getFoods())
                    .hasSize(1)
                    .contains(food);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if the meal does not exist")
        void shouldThrowResourceNotFoundExceptionIfTheMealDoesNotExist() {
            // Act & Assert
            assertThatThrownBy(() -> sut.addFoodsToMeal(UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()), UUID.randomUUID().toString()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Meal not found.");
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if some foods are not found")
        void shouldThrowResourceNotFoundExceptionIfSomeFoodsAreNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal = createMeal();
            meal.setUser(user);
            mealRepository.save(meal);

            // Act & Assert
            assertThatThrownBy(() -> sut.addFoodsToMeal(meal.getId(), List.of("invalid-food-id"), user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Some food were not found.");
        }
    }

    @Nested
    @DisplayName("getMeals tests")
    class GetMealsTests {

        @Test
        @Transactional
        @DisplayName("should be able to return paginated meals")
        void shouldBeAbleToReturnPaginatedMeals() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Meal meal1 = createMeal();
            Meal meal2 = createMeal();

            meal1.setUser(user);
            meal2.setUser(user);
            mealRepository.saveAll(List.of(meal1, meal2));

            Pageable pageable = PageRequest.of(0, 10);

            // Act
            Page<MealDTO> meals = sut.getMeals(pageable, user.getId());

            // Assert
            assertThat(meals.getContent())
                    .hasSize(2)
                    .extracting(MealDTO::id)
                    .containsExactlyInAnyOrder(meal1.getId(), meal2.getId());
            assertThat(meals.getNumber()).isEqualTo(0);
            assertThat(meals.getSize()).isEqualTo(10);
            assertThat(meals.getTotalElements()).isEqualTo(2);
            assertThat(meals.getTotalPages()).isEqualTo(1);
        }

        @Test
        @Transactional
        @DisplayName("should be able to return an empty page if the user has no meals")
        void shouldBeAbleToReturnEmptyPageIfTheUserHasNoMeals() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Pageable pageable = PageRequest.of(0, 10);

            // Act
            Page<MealDTO> meals = sut.getMeals(pageable, user.getId());

            // Assert
            assertThat(meals.getContent()).isEmpty();
            assertThat(meals.getNumber()).isEqualTo(0);
            assertThat(meals.getSize()).isEqualTo(10);
            assertThat(meals.getTotalElements()).isEqualTo(0);
            assertThat(meals.getTotalPages()).isEqualTo(0);
        }
    }

}
