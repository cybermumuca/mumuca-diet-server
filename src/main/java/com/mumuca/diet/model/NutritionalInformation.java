package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "nutritional_informations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionalInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "calories", nullable = false)
    private BigDecimal calories = BigDecimal.ZERO;

    @Column(name = "carbohydrates", nullable = false)
    private BigDecimal carbohydrates = BigDecimal.ZERO;

    @Column(name = "protein", nullable = false)
    private BigDecimal protein = BigDecimal.ZERO;

    @Column(name = "fat", nullable = false)
    private BigDecimal fat = BigDecimal.ZERO;

    @Column(name = "monounsaturated_fat", nullable = false)
    private BigDecimal monounsaturatedFat = BigDecimal.ZERO;

    @Column(name = "saturated_fat", nullable = false)
    private BigDecimal saturatedFat = BigDecimal.ZERO;

    @Column(name = "polyunsaturated_fat", nullable = false)
    private BigDecimal polyunsaturatedFat = BigDecimal.ZERO;

    @Column(name = "trans_fat", nullable = false)
    private BigDecimal transFat = BigDecimal.ZERO;

    @Column(name = "cholesterol", nullable = false)
    private BigDecimal cholesterol = BigDecimal.ZERO;

    @Column(name = "sodium", nullable = false)
    private BigDecimal sodium = BigDecimal.ZERO;

    @Column(name = "potassium", nullable = false)
    private BigDecimal potassium = BigDecimal.ZERO;

    @Column(name = "fiber", nullable = false)
    private BigDecimal fiber = BigDecimal.ZERO;

    @Column(name = "sugar", nullable = false)
    private BigDecimal sugar = BigDecimal.ZERO;

    @Column(name = "calcium", nullable = false)
    private BigDecimal calcium = BigDecimal.ZERO;

    @Column(name = "iron", nullable = false)
    private BigDecimal iron = BigDecimal.ZERO;

    @Column(name = "vitamin_a", nullable = false)
    private BigDecimal vitaminA = BigDecimal.ZERO;

    @Column(name = "vitamin_c", nullable = false)
    private BigDecimal vitaminC = BigDecimal.ZERO;

    @OneToOne(mappedBy = "nutritionalInformation", cascade = CascadeType.ALL)
    private Food food;

    @PrePersist
    public void prePersist() {
        if (this.calories == null) this.calories = BigDecimal.ZERO;
        if (this.carbohydrates == null) this.carbohydrates = BigDecimal.ZERO;
        if (this.protein == null) this.protein = BigDecimal.ZERO;
        if (this.fat == null) this.fat = BigDecimal.ZERO;
        if (this.monounsaturatedFat == null) this.monounsaturatedFat = BigDecimal.ZERO;
        if (this.saturatedFat == null) this.saturatedFat = BigDecimal.ZERO;
        if (this.polyunsaturatedFat == null) this.polyunsaturatedFat = BigDecimal.ZERO;
        if (this.transFat == null) this.transFat = BigDecimal.ZERO;
        if (this.cholesterol == null) this.cholesterol = BigDecimal.ZERO;
        if (this.sodium == null) this.sodium = BigDecimal.ZERO;
        if (this.potassium == null) this.potassium = BigDecimal.ZERO;
        if (this.fiber == null) this.fiber = BigDecimal.ZERO;
        if (this.sugar == null) this.sugar = BigDecimal.ZERO;
        if (this.calcium == null) this.calcium = BigDecimal.ZERO;
        if (this.iron == null) this.iron = BigDecimal.ZERO;
        if (this.vitaminA == null) this.vitaminA = BigDecimal.ZERO;
        if (this.vitaminC == null) this.vitaminC = BigDecimal.ZERO;
    }
}
