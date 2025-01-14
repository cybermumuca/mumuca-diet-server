package com.mumuca.diet.drink.mapper;

import com.mumuca.diet.drink.dto.CreateDrinkLogDTO;
import com.mumuca.diet.drink.dto.DrinkLogDTO;
import com.mumuca.diet.drink.dto.UpdateDrinkLogDTO;
import com.mumuca.diet.drink.model.DrinkLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = DrinkLogMapperTest.Config.class)
class DrinkLogMapperTest {

    @ComponentScan(basePackageClasses = DrinkLogMapper.class)
    static class Config {}

    @Autowired
    private DrinkLogMapper sut;

    @Test
    @DisplayName("should map DrinkLog to DrinkLogDTO")
    void shouldMapDrinkLogToDrinkLogDTO() {
        // Arrange
        DrinkLog drinkLog = DrinkLog.builder()
                .id("123")
                .date(LocalDate.of(2025, 1, 1))
                .time(LocalTime.of(14, 30))
                .liquidIntake(new BigDecimal("500.0"))
                .build();

        // Act
        DrinkLogDTO dto = sut.fromDrinkLogToDrinkLogDTO(drinkLog);

        // Assert
        assertThat(dto.id()).isEqualTo("123");
        assertThat(dto.date()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(dto.time()).isEqualTo(LocalTime.of(14, 30));
        assertThat(dto.liquidIntake()).isEqualTo(new BigDecimal("500.0"));
    }

    @Test
    @DisplayName("should map CreateDrinkLogDTO to DrinkLog")
    void shouldMapCreateDrinkLogToDrinkLog() {
        // Arrange
        CreateDrinkLogDTO dto = new CreateDrinkLogDTO(
                LocalDate.of(2025, 1, 1),
                LocalTime.of(18, 30),
                new BigDecimal("500.0")
        );

        // Act
        DrinkLog drinkLog = sut.fromCreateDrinkLogDTOToDrinkLog(dto);

        // Assert
        assertThat(drinkLog.getDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(drinkLog.getTime()).isEqualTo(LocalTime.of(18, 30));
        assertThat(drinkLog.getLiquidIntake()).isEqualTo(new BigDecimal("500.0"));
    }

    @Test
    @DisplayName("should map DrinkLogDTO to DrinkLog")
    void shouldMapDrinkLogDTOToDrinkLog() {
        // Arrange
        DrinkLogDTO dto = new DrinkLogDTO(
                "123",
                LocalDate.of(2025, 1, 1),
                LocalTime.of(12, 30),
                new BigDecimal("500.0")
        );

        // Act
        DrinkLog drinkLog = sut.fromDrinkLogDTOToDrinkLog(dto);

        // Assert
        assertThat(drinkLog.getId()).isEqualTo("123");
        assertThat(drinkLog.getDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(drinkLog.getTime()).isEqualTo(LocalTime.of(12, 30));
        assertThat(drinkLog.getLiquidIntake()).isEqualTo(new BigDecimal("500.0"));
    }

    @Test
    @DisplayName("should update DrinkLog from UpdateDrinkLogDTO")
    void shouldUpdateDrinkLogFromDTO() {
        // Arrange
        DrinkLog drinkLog = DrinkLog.builder()
                .id("test-id")
                .time(LocalTime.of(10, 0))
                .date(LocalDate.of(2025, 1, 1))
                .liquidIntake(new BigDecimal("500"))
                .build();

        UpdateDrinkLogDTO updateDrinkLogDTO = new UpdateDrinkLogDTO(
                LocalTime.of(15, 30),
                LocalDate.of(2025, 1, 2),
                new BigDecimal("750")
        );

        // Act
        sut.updateDrinkLogFromDTO(updateDrinkLogDTO, drinkLog);

        // Assert
        assertThat(drinkLog.getTime()).isEqualTo(LocalTime.of(15, 30));
        assertThat(drinkLog.getDate()).isEqualTo(LocalDate.of(2025, 1, 2));
        assertThat(drinkLog.getLiquidIntake()).isEqualTo(new BigDecimal("750"));
    }

    @Test
    @DisplayName("should ignore null fields when updating DrinkLog from UpdateDrinkLogDTO")
    void shouldIgnoreNullFieldsInUpdateDrinkLogFromDTO() {
        // Arrange
        DrinkLog drinkLog = DrinkLog.builder()
                .id("test-id")
                .time(LocalTime.of(10, 0))
                .date(LocalDate.of(2025, 1, 1))
                .liquidIntake(new BigDecimal("500"))
                .build();

        UpdateDrinkLogDTO updateDrinkLogDTO = new UpdateDrinkLogDTO(
                LocalTime.of(15, 30),
                null,
                null
        );

        // Act
        sut.updateDrinkLogFromDTO(updateDrinkLogDTO, drinkLog);

        // Assert
        assertThat(drinkLog.getTime()).isEqualTo(LocalTime.of(15, 30));
        assertThat(drinkLog.getDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(drinkLog.getLiquidIntake()).isEqualTo(new BigDecimal("500"));
    }

    @Test
    @DisplayName("should ignore equal fields when updating DrinkLog from UpdateDrinkLogDTO")
    void shouldIgnoreEqualFieldsInUpdateDrinkLogFromDTO() {
        // Arrange
        DrinkLog drinkLog = DrinkLog.builder()
                .id("test-id")
                .time(LocalTime.of(10, 0))
                .date(LocalDate.of(2025, 1, 1))
                .liquidIntake(new BigDecimal("500"))
                .build();

        UpdateDrinkLogDTO updateDrinkLogDTO = new UpdateDrinkLogDTO(
                LocalTime.of(10, 0),
                LocalDate.of(2025, 1, 1),
                new BigDecimal("500")
        );

        // Act
        sut.updateDrinkLogFromDTO(updateDrinkLogDTO, drinkLog);

        // Assert
        assertThat(drinkLog.getTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(drinkLog.getDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(drinkLog.getLiquidIntake()).isEqualTo(new BigDecimal("500"));
    }
}