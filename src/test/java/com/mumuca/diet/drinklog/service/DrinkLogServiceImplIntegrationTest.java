package com.mumuca.diet.drinklog.service;

import com.mumuca.diet.dto.drinklog.CreateDrinkLogDTO;
import com.mumuca.diet.dto.drinklog.DrinkLogDTO;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.DrinkLogRepository;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.service.impl.DrinkLogServiceImpl;
import com.mumuca.diet.testutil.EntityGeneratorUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findDrinkLogsByDate() {

    }

    @Test
    @Transactional
    void createDrinkLog() {
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
    void deleteDrinkLog() {
    }

    @Test
    void updateDrinkLog() {
    }
}