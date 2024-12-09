package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(
        name = "meal_log_preferences",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "type"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MealLogPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MealType type;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private Integer caloriesGoal;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
