package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "foods")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column
    private String brand;

    @Column
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "nutritional_information_id", referencedColumnName = "id", unique = true)
    private NutritionalInformation nutritionalInformation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
