package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "foods")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "brand")
    private String brand;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "food", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Portion portion;

    @OneToOne(mappedBy = "food", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private NutritionalInformation nutritionalInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "foods", fetch = FetchType.LAZY)
    private Set<Meal> meals = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "foods", fetch = FetchType.LAZY)
    private Set<MealLog> mealLogs = new LinkedHashSet<>();

    public Food(String id) {
        this.id = id;
    }
}
