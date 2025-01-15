package com.mumuca.diet.meal.mapper;

import com.mumuca.diet.dto.meallog.MealLogDTO;
import com.mumuca.diet.model.MealLog;
import com.mumuca.diet.model.MealType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = MealLogMapperTest.Config.class)
public class MealLogMapperTest {

    @ComponentScan(basePackageClasses = MealLogMapper.class)
    static class Config {}

    @Autowired
    private MealLogMapper sut;

    @Test
    @DisplayName("should be able to map MealLog to MealLogDTO")
    void fromMealLogToMealLogDTO() {
        // Arrange
        MealLog mealLog = MealLog.builder()
                .id("test-id")
                .type(MealType.BREAKFAST)
                .date(LocalDate.of(2025, 1, 1))
                .time(LocalTime.of(15, 0))
                .caloriesGoal(1000)
                .build();

        // Act
        MealLogDTO result = sut.fromMealLogToMealLogDTO(mealLog);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("test-id");
        assertThat(result.type()).isEqualTo(MealType.BREAKFAST);
        assertThat(result.date()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(result.time()).isEqualTo(LocalTime.of(15, 0));
        assertThat(result.caloriesGoal()).isEqualTo(1000);
    }
}
