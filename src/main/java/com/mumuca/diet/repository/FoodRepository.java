package com.mumuca.diet.repository;

import com.mumuca.diet.model.Food;
import com.mumuca.diet.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
    Optional<Food> findByIdAndUserId(String id, String userId);

    @Query("SELECT f FROM Food f WHERE f.id IN :ids AND f.user.id = :userId")
    List<Food> findAllByIdsAndUserId(Iterable<String> ids, String userId);
}
