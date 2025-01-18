package com.mumuca.diet.drink.service;

import com.mumuca.diet.drink.dto.CreateDrinkLogDTO;
import com.mumuca.diet.drink.dto.DrinkLogDTO;
import com.mumuca.diet.drink.dto.UpdateDrinkLogDTO;
import com.mumuca.diet.drink.model.DrinkLog;
import com.mumuca.diet.auth.model.User;
import com.mumuca.diet.drink.repository.DrinkLogRepository;
import com.mumuca.diet.auth.repository.UserRepository;
import com.mumuca.diet.drink.service.impl.DrinkLogServiceImpl;
import com.mumuca.diet.testutil.EntityGeneratorUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DrinkLogServiceImplIntegrationTest {

    @Autowired
    private DrinkLogServiceImpl sut;

    @Autowired
    private DrinkLogRepository drinkLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("should find drink logs by date successfully")
    void shouldFindDrinkLogsByDate() {
        // Assert
        User user = EntityGeneratorUtil.createUser();
        userRepository.save(user);

        DrinkLog drinkLog = new DrinkLog();
        drinkLog.setDate(LocalDate.of(2025, 1, 1));
        drinkLog.setTime(LocalTime.of(10, 0));
        drinkLog.setLiquidIntake(new BigDecimal("500"));
        drinkLog.setUser(user);

        drinkLogRepository.save(drinkLog);

        // Act
        List<DrinkLogDTO> result = sut.findDrinkLogsByDate(LocalDate.of(2025, 1, 1), user.getId());

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(drinkLog.getId());
        assertThat(result.getFirst().date()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(result.getFirst().time()).isEqualTo(LocalTime.of(10, 0));
        assertThat(result.getFirst().liquidIntake()).isEqualTo(new BigDecimal("500"));
    }

    @Test
    @Transactional
    @DisplayName("should create drink log successfully")
    void shouldCreateDrinkLog() {
        // Arrange
        CreateDrinkLogDTO createDrinkLogDTO = new CreateDrinkLogDTO(
                LocalDate.of(2025, 1, 2),
                LocalTime.of(12, 0),
                new BigDecimal("300")
        );
        User user = EntityGeneratorUtil.createUser();
        userRepository.save(user);

        // Act
        DrinkLogDTO result = sut.createDrinkLog(createDrinkLogDTO, user.getId());

        // Assert
        assertThat(result.date()).isEqualTo(LocalDate.of(2025, 1, 2));
        assertThat(result.time()).isEqualTo(LocalTime.of(12, 0));
        assertThat(result.liquidIntake()).isEqualTo(new BigDecimal("300"));

        var drinkLogInDatabase = drinkLogRepository.findById(result.id()).get();

        assertThat(drinkLogInDatabase.getDate()).isEqualTo(LocalDate.of(2025, 1, 2));
        assertThat(drinkLogInDatabase.getTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(drinkLogInDatabase.getLiquidIntake()).isEqualTo(new BigDecimal("300"));
        assertThat(drinkLogInDatabase.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @Transactional
    @DisplayName("should delete drink log successfully")
    void shouldDeleteDrinkLog() {
        // Arrange
        User user = EntityGeneratorUtil.createUser();
        userRepository.save(user);

        DrinkLog drinkLog = new DrinkLog();
        drinkLog.setDate(LocalDate.of(2025, 1, 1));
        drinkLog.setTime(LocalTime.of(10, 0));
        drinkLog.setLiquidIntake(new BigDecimal("500"));
        drinkLog.setUser(user);

        drinkLogRepository.save(drinkLog);

        // Act
        sut.deleteDrinkLog(drinkLog.getId(), user.getId());

        // Assert
        assertThat(drinkLogRepository.findById(drinkLog.getId())).isNotPresent();
    }

    @Test
    @Transactional
    @DisplayName("should update drink log successfully")
    void shouldUpdateDrinkLog() {
        // Arrange
        User user = EntityGeneratorUtil.createUser();
        userRepository.save(user);

        DrinkLog drinkLog = new DrinkLog();
        drinkLog.setDate(LocalDate.of(2025, 1, 1));
        drinkLog.setTime(LocalTime.of(10, 0));
        drinkLog.setLiquidIntake(new BigDecimal("500"));
        drinkLog.setUser(user);

        drinkLogRepository.save(drinkLog);

        UpdateDrinkLogDTO updateDrinkLogDTO = new UpdateDrinkLogDTO(
                LocalTime.of(15, 30),
                LocalDate.of(2025, 1, 3),
                new BigDecimal("1000")
        );

        // Act
        sut.updateDrinkLog(drinkLog.getId(), updateDrinkLogDTO, user.getId());

        // Assert
        DrinkLog updatedDrinkLog = drinkLogRepository.findById(drinkLog.getId()).orElseThrow();
        assertThat(updatedDrinkLog.getTime()).isEqualTo(LocalTime.of(15, 30));
        assertThat(updatedDrinkLog.getDate()).isEqualTo(LocalDate.of(2025, 1, 3));
        assertThat(updatedDrinkLog.getLiquidIntake()).isEqualTo(new BigDecimal("1000"));
    }
}