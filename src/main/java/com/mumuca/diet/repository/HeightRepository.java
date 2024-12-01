package com.mumuca.diet.repository;

import com.mumuca.diet.model.Height;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeightRepository extends JpaRepository<Height, String> {
    Optional<Height> findByIdAndUserId(String id, String userId);
}
