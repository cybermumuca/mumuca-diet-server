package com.mumuca.diet.meal.model;

import com.mumuca.diet.audit.AbstractAuditEntity;
import com.mumuca.diet.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

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
public class MealLogPreference extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private MealType type;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "calories_goal")
    private Integer caloriesGoal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
