package com.mumuca.diet.model;

import com.mumuca.diet.audit.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "meals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Meal extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "meal_type_enum")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private MealType type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meals_foods",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private Set<Food> foods = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "meals")
    private Set<MealLog> mealLogs = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
