package com.mumuca.diet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

//    public BigDecimal calculateBMR() {
//        if (weight == null || height == null || age <= 0 || gender == null) {
//            throw new IllegalStateException("Weight, height, age, and gender must be set to calculate BMR.");
//        }
//
//        BigDecimal bmr;
//
//        if (gender == Gender.MALE) {
//            bmr = BigDecimal.valueOf(88.362)
//                    .add(BigDecimal.valueOf(13.397).multiply(weight))
//                    .add(BigDecimal.valueOf(4.799).multiply(height.multiply(BigDecimal.valueOf(100))))
//                    .subtract(BigDecimal.valueOf(5.677).multiply(BigDecimal.valueOf(age)));
//        } else {
//            bmr = BigDecimal.valueOf(447.6)
//                    .add(BigDecimal.valueOf(9.2).multiply(weight))
//                    .add(BigDecimal.valueOf(3.1).multiply(height.multiply(BigDecimal.valueOf(100))))
//                    .subtract(BigDecimal.valueOf(4.3).multiply(BigDecimal.valueOf(age)));
//        }
//
//        return bmr.setScale(2, RoundingMode.HALF_UP);
//    }
}
