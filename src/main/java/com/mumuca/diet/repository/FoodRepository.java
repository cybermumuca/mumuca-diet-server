package com.mumuca.diet.repository;

import com.mumuca.diet.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
    Optional<Food> findByIdAndUserId(String id, String userId);

    @Query("SELECT f FROM Food f WHERE f.id IN :ids AND f.user.id = :userId")
    List<Food> findAllByIdsAndUserId(Iterable<String> ids, String userId);

    List<Food> findAllByMealLogsIdAndUserId(String mealLogId, String userId);

    @Query("SELECT COUNT(f) FROM Food f WHERE f.id IN :ids AND f.user.id = :userId")
    long countByIdsAndUserId(@Param("ids") Iterable<String> ids, @Param("userId") String userId);
}
