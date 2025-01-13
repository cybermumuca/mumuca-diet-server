package com.mumuca.diet.repository;

import com.mumuca.diet.model.DrinkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DrinkLogRepository extends JpaRepository<DrinkLog, String> {
    List<DrinkLog> findByDateAndUserId(LocalDate date, String userId);
}
