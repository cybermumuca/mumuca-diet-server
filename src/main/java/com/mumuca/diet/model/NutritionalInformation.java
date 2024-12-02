package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "nutritional_information")
@Getter
@Setter
@NoArgsConstructor
public class NutritionalInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "calories")
    private BigDecimal calories;

    @Column(name = "carbohydrates")
    private BigDecimal carbohydrates;

    @Column(name = "protein")
    private BigDecimal protein;

    @Column(name = "fat")
    private BigDecimal fat;

    @Column(name = "monounsaturated_fat")
    private BigDecimal monounsaturatedFat;

    @Column(name = "saturated_fat")
    private BigDecimal saturatedFat;

    @Column(name = "polyunsaturated_fat")
    private BigDecimal polyunsaturatedFat;

    @Column(name = "trans_fat")
    private BigDecimal transFat;

    @Column(name = "cholesterol")
    private BigDecimal cholesterol;

    @Column(name = "sodium")
    private BigDecimal sodium;

    @Column(name = "potassium")
    private BigDecimal potassium;

    @Column(name = "fiber")
    private BigDecimal fiber;

    @Column(name = "sugar")
    private BigDecimal sugar;

    @Column(name = "calcium")
    private BigDecimal calcium;

    @Column(name = "iron")
    private BigDecimal iron;

    @Column(name = "vitamin_a")
    private BigDecimal vitaminA;

    @Column(name = "vitamin_c")
    private BigDecimal vitaminC;

    @OneToOne(mappedBy = "nutritionalInformation", cascade = CascadeType.ALL)
    private Food food;
}
