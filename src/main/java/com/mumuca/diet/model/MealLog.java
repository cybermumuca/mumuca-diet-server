package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "meal_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MealLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MealType type;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private Integer caloriesGoal;

    @ManyToMany
    @JoinTable(
            name = "meal_log_meals",
            joinColumns = @JoinColumn(name = "meal_log_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    private Set<Meal> meals = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "meal_log_foods",
            joinColumns = @JoinColumn(name = "meal_log_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private Set<Food> foods = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
