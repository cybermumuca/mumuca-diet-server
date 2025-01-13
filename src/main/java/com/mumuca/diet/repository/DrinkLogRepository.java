package com.mumuca.diet.repository;

import com.mumuca.diet.model.DrinkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkLogRepository extends JpaRepository<DrinkLog, String> {
    List<DrinkLog> findByDateAndUserId(LocalDate date, String userId);
    Optional<DrinkLog> findDrinkLogByIdAndUserId(String id, String userId);
}
