package com.mumuca.diet.repository;

import com.mumuca.diet.model.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeightRepository extends JpaRepository<Weight, String> {
    Optional<Weight> findByIdAndUserId(String id, String userId);
}
