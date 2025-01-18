package com.mumuca.diet.model;

import com.mumuca.diet.audit.AbstractAuditEntity;
import com.mumuca.diet.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private Gender gender;

    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Column(name = "photoUrl")
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private ActivityLevel activityLevel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public int getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}