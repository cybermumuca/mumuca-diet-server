package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "bodies",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Body {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal height;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    public BigDecimal calculateBMI() {
//        if (weight == null || height == null) {
//            throw new IllegalStateException("Weight and height must be set to calculate BMI.");
//        }
//
//        return weight.divide(height.multiply(height), 2, RoundingMode.HALF_UP);
//    }
}
