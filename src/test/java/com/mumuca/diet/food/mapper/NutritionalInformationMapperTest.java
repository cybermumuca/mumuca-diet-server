package com.mumuca.diet.food.mapper;

import com.mumuca.diet.dto.food.CreateNutritionalInformationDTO;
import com.mumuca.diet.dto.food.NutritionalInformationDTO;
import com.mumuca.diet.model.NutritionalInformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = NutritionalInformationMapperTest.Config.class)
class NutritionalInformationMapperTest {

    @ComponentScan(basePackageClasses = NutritionalInformationMapper.class)
    static class Config {}

    @Autowired
    private NutritionalInformationMapper sut;

    @Test
    @DisplayName("should map CreateNutritionalInformationDTO to NutritionalInformation")
    void shouldMapCreateNutritionalInformationDTOToNutritionalInformation() {
        // Arrange
        CreateNutritionalInformationDTO createNutritionalInformationDTO = new CreateNutritionalInformationDTO(
                new BigDecimal("200.5"), // calories
                new BigDecimal("30.0"),  // carbohydrates
                new BigDecimal("15.0"),  // protein
                new BigDecimal("10.0"),  // fat
                new BigDecimal("5.0"),   // monounsaturatedFat
                new BigDecimal("3.0"),   // saturatedFat
                new BigDecimal("2.0"),   // polyunsaturatedFat
                new BigDecimal("1.0"),   // transFat
                new BigDecimal("50.0"),  // cholesterol
                new BigDecimal("100.0"), // sodium
                new BigDecimal("200.0"), // potassium
                new BigDecimal("8.0"),   // fiber
                new BigDecimal("12.0"),  // sugar
                new BigDecimal("50.0"),  // calcium
                new BigDecimal("5.0"),   // iron
                new BigDecimal("900.0"), // vitaminA
                new BigDecimal("30.0")   // vitaminC
        );

        // Act
        NutritionalInformation nutritionalInformation = sut.fromCreateNIDTOToNI(createNutritionalInformationDTO);

        // Assert
        assertThat(nutritionalInformation).isNotNull();
        assertThat(nutritionalInformation.getId()).isNull();
        assertThat(nutritionalInformation.getCalories()).isEqualTo(new BigDecimal("200.5"));
        assertThat(nutritionalInformation.getCarbohydrates()).isEqualTo(new BigDecimal("30.0"));
        assertThat(nutritionalInformation.getProtein()).isEqualTo(new BigDecimal("15.0"));
        assertThat(nutritionalInformation.getFat()).isEqualTo(new BigDecimal("10.0"));
        assertThat(nutritionalInformation.getMonounsaturatedFat()).isEqualTo(new BigDecimal("5.0"));
        assertThat(nutritionalInformation.getSaturatedFat()).isEqualTo(new BigDecimal("3.0"));
        assertThat(nutritionalInformation.getTransFat()).isEqualTo(new BigDecimal("1.0"));
        assertThat(nutritionalInformation.getCholesterol()).isEqualTo(new BigDecimal("50.0"));
        assertThat(nutritionalInformation.getSodium()).isEqualTo(new BigDecimal("100.0"));
        assertThat(nutritionalInformation.getPotassium()).isEqualTo(new BigDecimal("200.0"));
        assertThat(nutritionalInformation.getFiber()).isEqualTo(new BigDecimal("8.0"));
        assertThat(nutritionalInformation.getSugar()).isEqualTo(new BigDecimal("12.0"));
        assertThat(nutritionalInformation.getCalcium()).isEqualTo(new BigDecimal("50.0"));
        assertThat(nutritionalInformation.getIron()).isEqualTo(new BigDecimal("5.0"));
        assertThat(nutritionalInformation.getVitaminA()).isEqualTo(new BigDecimal("900.0"));
        assertThat(nutritionalInformation.getVitaminC()).isEqualTo(new BigDecimal("30.0"));
        assertThat(nutritionalInformation.getFood()).isNull();
    }

    @Test
    @DisplayName("should map NutritionalInformation to NutritionalInformationDTO")
    void shouldMapNutritionalInformationToNutritionalInformationDTO() {
        // Arrange
        NutritionalInformation nutritionalInformation = NutritionalInformation.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .calories(new BigDecimal("200.5"))
                .carbohydrates(new BigDecimal("30.0"))
                .protein(new BigDecimal("15.0"))
                .fat(new BigDecimal("10.0"))
                .monounsaturatedFat(new BigDecimal("5.0"))
                .saturatedFat(new BigDecimal("3.0"))
                .polyunsaturatedFat(new BigDecimal("2.0"))
                .transFat(new BigDecimal("1.0"))
                .cholesterol(new BigDecimal("50.0"))
                .sodium(new BigDecimal("100.0"))
                .potassium(new BigDecimal("200.0"))
                .fiber(new BigDecimal("8.0"))
                .sugar(new BigDecimal("12.0"))
                .calcium(new BigDecimal("50.0"))
                .iron(new BigDecimal("5.0"))
                .vitaminA(new BigDecimal("900.0"))
                .vitaminC(new BigDecimal("30.0"))
                .food(null)
                .build();

        // Act
        NutritionalInformationDTO result = sut.fromNIToNIDTO(nutritionalInformation);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();
        assertThat(result.id()).isEqualTo("123e4567-e89b-12d3-a456-426614174000");
        assertThat(result.calories()).isEqualTo(new BigDecimal("200.5"));
        assertThat(result.carbohydrates()).isEqualTo(new BigDecimal("30.0"));
        assertThat(result.protein()).isEqualTo(new BigDecimal("15.0"));
        assertThat(result.fat()).isEqualTo(new BigDecimal("10.0"));
        assertThat(result.monounsaturatedFat()).isEqualTo(new BigDecimal("5.0"));
        assertThat(result.saturatedFat()).isEqualTo(new BigDecimal("3.0"));
        assertThat(result.transFat()).isEqualTo(new BigDecimal("1.0"));
        assertThat(result.cholesterol()).isEqualTo(new BigDecimal("50.0"));
        assertThat(result.sodium()).isEqualTo(new BigDecimal("100.0"));
        assertThat(result.potassium()).isEqualTo(new BigDecimal("200.0"));
        assertThat(result.fiber()).isEqualTo(new BigDecimal("8.0"));
        assertThat(result.sugar()).isEqualTo(new BigDecimal("12.0"));
        assertThat(result.calcium()).isEqualTo(new BigDecimal("50.0"));
        assertThat(result.iron()).isEqualTo(new BigDecimal("5.0"));
        assertThat(result.vitaminA()).isEqualTo(new BigDecimal("900.0"));
        assertThat(result.vitaminC()).isEqualTo(new BigDecimal("30.0"));
    }
}