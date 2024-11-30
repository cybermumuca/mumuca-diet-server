package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "weights")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private double registry;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
