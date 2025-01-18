package com.mumuca.diet.drink.model;

import com.mumuca.diet.audit.AbstractAuditEntity;
import com.mumuca.diet.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "drink_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrinkLog extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "liquid_intake")
    private BigDecimal liquidIntake;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
