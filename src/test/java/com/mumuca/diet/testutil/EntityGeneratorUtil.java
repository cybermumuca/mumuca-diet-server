package com.mumuca.diet.testutil;

import com.github.javafaker.Faker;
import com.mumuca.diet.auth.model.User;
import com.mumuca.diet.food.model.Food;
import com.mumuca.diet.food.model.NutritionalInformation;
import com.mumuca.diet.food.model.Portion;
import com.mumuca.diet.food.model.Unit;
import com.mumuca.diet.meal.model.Meal;
import com.mumuca.diet.meal.model.MealType;
import com.mumuca.diet.model.*;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static java.time.ZoneId.systemDefault;

public class EntityGeneratorUtil {
    public static final Faker faker = new Faker();

    public static User createUser() {
        return User.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }

    public static Food createFood() {
        return Food.builder()
                .title(faker.food().dish())
                .description(faker.lorem().characters(15))
                .brand(faker.company().name())
                .build();
    }

    public static NutritionalInformation createNutritionalInformation() {
        return NutritionalInformation.builder()
                .calories(BigDecimal.valueOf(faker.number().numberBetween(50, 450)))  // Calorias entre 50 e 450
                .calcium(BigDecimal.valueOf(faker.number().numberBetween(50, 450)))  // Cálcio entre 50 e 450mg
                .carbohydrates(BigDecimal.valueOf(faker.number().numberBetween(10, 100)))  // Carboidratos entre 10g e 100g
                .protein(BigDecimal.valueOf(faker.number().numberBetween(1, 30)))  // Proteínas entre 1g e 30g
                .fat(BigDecimal.valueOf(faker.number().numberBetween(0, 40)))  // Gordura entre 0g e 40g
                .monounsaturatedFat(BigDecimal.valueOf(faker.number().numberBetween(0, 10)))  // Gordura monoinsaturada entre 0g e 10g
                .saturatedFat(BigDecimal.valueOf(faker.number().numberBetween(0, 10)))  // Gordura saturada entre 0g e 10g
                .polyunsaturatedFat(BigDecimal.valueOf(faker.number().numberBetween(0, 10)))  // Gordura poli-insaturada entre 0g e 10g
                .transFat(BigDecimal.valueOf(faker.number().numberBetween(0, 5)))  // Gordura trans entre 0g e 5g
                .cholesterol(BigDecimal.valueOf(faker.number().numberBetween(0, 50)))  // Colesterol entre 0mg e 50mg
                .sodium(BigDecimal.valueOf(faker.number().numberBetween(0, 500)))  // Sódio entre 0mg e 500mg
                .potassium(BigDecimal.valueOf(faker.number().numberBetween(0, 700)))  // Potássio entre 0mg e 700mg
                .fiber(BigDecimal.valueOf(faker.number().numberBetween(0, 10)))  // Fibra entre 0g e 10g
                .sugar(BigDecimal.valueOf(faker.number().numberBetween(0, 40)))  // Açúcares entre 0g e 40g
                .iron(BigDecimal.valueOf(faker.number().numberBetween(0, 10)))  // Ferro entre 0mg e 10mg
                .vitaminA(BigDecimal.valueOf(faker.number().numberBetween(0, 1000)))  // Vitamina A entre 0UI e 1000UI
                .vitaminC(BigDecimal.valueOf(faker.number().numberBetween(0, 60)))  // Vitamina C entre 0mg e 60mg
                .build();
    }

    public static Portion createPortion() {
        return Portion.builder()
                .amount(faker.number().numberBetween(0, 100))
                .unit(Unit.GRAM)
                .description(faker.lorem().characters(15))
                .build();
    }

    public static Meal createMeal() {
        return Meal.builder()
                .title(faker.lorem().word())
                .type(MealType.LUNCH)
                .description(faker.lorem().characters(15))
                .build();
    }

    public static Goal createGoal() {
        Goal goal = new Goal();
        goal.setGoalType(faker.options().option(GoalType.class));
        goal.setTargetCalories(faker.number().numberBetween(1500, 4000));
        goal.setTargetWeight(BigDecimal.valueOf(faker.number().numberBetween(10, 100)));
        goal.setCarbsTarget(BigDecimal.valueOf(faker.number().numberBetween(10, 100)));
        goal.setProteinTarget(BigDecimal.valueOf(faker.number().numberBetween(1, 30)));
        goal.setFatTarget(BigDecimal.valueOf(faker.number().numberBetween(0, 50)));
        goal.setWaterIntakeTarget(BigDecimal.valueOf(faker.number().numberBetween(1, 4)));
        goal.setDeadline(faker.date().future(120, TimeUnit.DAYS).toInstant().atZone(systemDefault()).toLocalDate());
        return goal;
    }
}
