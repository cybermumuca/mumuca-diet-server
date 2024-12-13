package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type", nullable = false)
    private GoalType goalType;

    @Column(name = "target_calories", nullable = false)
    private int targetCalories; // kcal

    @Column(name = "target_weight", nullable = false)
    private BigDecimal targetWeight; // kg

    @Column(name = "protein_target", nullable = false)
    private BigDecimal proteinTarget; // g

    @Column(name = "carbs_target", nullable = false)
    private BigDecimal carbsTarget; // g

    @Column(name = "fat_target", nullable = false)
    private BigDecimal fatTarget; // g

    @Column(name = "water_intake_target", nullable = false, precision = 5, scale = 2)
    private BigDecimal waterIntakeTarget; // L

    @Column
    private LocalDate deadline;

    @OneToOne(mappedBy = "goal", cascade = CascadeType.ALL)
    private User user;
}
