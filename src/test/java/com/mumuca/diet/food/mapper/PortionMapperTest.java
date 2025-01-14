package com.mumuca.diet.food.mapper;

import com.mumuca.diet.food.dto.CreatePortionDTO;
import com.mumuca.diet.food.dto.PortionDTO;
import com.mumuca.diet.food.model.Portion;
import com.mumuca.diet.model.Unit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = PortionMapperTest.Config.class)
class PortionMapperTest {

    @ComponentScan(basePackageClasses = PortionMapper.class)
    static class Config {}

    @Autowired
    private PortionMapper sut;

    @Test
    @DisplayName("should map Portion to PortionDTO")
    void fromPortionToPortionDTO() {
        // Arrange
        Portion portion = new Portion();
        portion.setId("123");
        portion.setAmount(10);
        portion.setUnit(Unit.GRAM);
        portion.setDescription("test");

        // Act
        PortionDTO result = sut.fromPortionToPortionDTO(portion);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("123");
        assertThat(result.amount()).isEqualTo(10);
        assertThat(result.unit()).isEqualTo(Unit.GRAM);
        assertThat(result.description()).isEqualTo("test");
    }

    @Test
    @DisplayName("should map PortionDTO to Portion")
    void fromPortionDTOToPortion() {
        // Arrange
        PortionDTO portionDTO = new PortionDTO("123", 10, Unit.GRAM, "test");

        // Act
        Portion result = sut.fromPortionDTOToPortion(portionDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("123");
        assertThat(result.getAmount()).isEqualTo(10);
        assertThat(result.getUnit()).isEqualTo(Unit.GRAM);
        assertThat(result.getDescription()).isEqualTo("test");
    }

    @Test
    @DisplayName("should map CreatePortionDTO to Portion")
    void fromCreatePortionDTOToPortion() {
        // Arrange
        CreatePortionDTO createPortionDTO = new CreatePortionDTO(10, Unit.GRAM, "test");

        // Act
        Portion result = sut.fromCreatePortionDTOToPortion(createPortionDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(10);
        assertThat(result.getUnit()).isEqualTo(Unit.GRAM);
        assertThat(result.getDescription()).isEqualTo("test");
    }
}