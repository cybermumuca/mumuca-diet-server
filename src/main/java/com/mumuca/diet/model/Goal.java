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
    @Column(name = "id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type")
    private GoalType goalType;

    @Column(name = "target_calories")
    private int targetCalories; // kcal

    @Column(name = "target_weight")
    private BigDecimal targetWeight; // kg

    @Column(name = "protein_target")
    private BigDecimal proteinTarget; // g

    @Column(name = "carbs_target")
    private BigDecimal carbsTarget; // g

    @Column(name = "fat_target")
    private BigDecimal fatTarget; // g

    @Column(name = "water_intake_target")
    private BigDecimal waterIntakeTarget; // L

    @Column(name = "deadline")
    private LocalDate deadline;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
