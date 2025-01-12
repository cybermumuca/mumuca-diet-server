package com.mumuca.diet.model;

import com.mumuca.diet.audit.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

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
public class MealLog extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private MealType type;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "calories_goal")
    private Integer caloriesGoal;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meal_logs_meals",
            joinColumns = @JoinColumn(name = "meal_log_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    private Set<Meal> meals = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meal_logs_foods",
            joinColumns = @JoinColumn(name = "meal_log_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private Set<Food> foods = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
