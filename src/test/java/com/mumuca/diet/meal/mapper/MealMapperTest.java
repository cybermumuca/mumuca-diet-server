package com.mumuca.diet.meal.mapper;

import com.mumuca.diet.dto.meal.MealDTO;
import com.mumuca.diet.model.Meal;
import com.mumuca.diet.model.MealType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = MealMapperTest.Config.class)
class MealMapperTest {

    @ComponentScan(basePackageClasses = MealMapper.class)
    static class Config {}

    @Autowired
    private MealMapper sut;

    @Test
    @DisplayName("should map Meal to MealDTO")
    void fromMealToMealDTO() {
        // Arrange
        Meal meal = new Meal();
        meal.setId("test-id");
        meal.setType(MealType.BREAKFAST);
        meal.setTitle("test-title");
        meal.setDescription("test-description");

        // Act
        MealDTO result = sut.fromMealToMealDTO(meal);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("test-id");
        assertThat(result.title()).isEqualTo("test-title");
        assertThat(result.type()).isEqualTo(MealType.BREAKFAST);
        assertThat(result.description()).isEqualTo("test-description");
    }
}