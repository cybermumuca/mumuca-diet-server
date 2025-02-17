package com.mumuca.diet.auth.model;

import com.mumuca.diet.drink.model.DrinkLog;
import com.mumuca.diet.food.model.Food;
import com.mumuca.diet.meal.model.Meal;
import com.mumuca.diet.meal.model.MealLog;
import com.mumuca.diet.meal.model.MealLogPreference;
import com.mumuca.diet.model.Body;
import com.mumuca.diet.model.Goal;
import com.mumuca.diet.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Column(insertable = false, updatable = false)
    private Set<Role> roles;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Food> foods = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Meal> meals = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Body> bodyHistory = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DrinkLog> drinkLogs = new ArrayList<>();

    // TODO: JPA will always bring this entity in every query for Users
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile;

    // TODO: JPA will always bring this entity in every query for Users
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Goal goal;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MealLog> mealLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MealLogPreference> mealLogPreferences = new ArrayList<>();

    public User(String id) {
        this.id = id;
    }
}
