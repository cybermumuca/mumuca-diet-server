package com.mumuca.diet.repository;

import com.mumuca.diet.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
    Optional<Food> findByIdAndUserId(String id, String userId);
}
