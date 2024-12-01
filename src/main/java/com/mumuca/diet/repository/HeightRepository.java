package com.mumuca.diet.repository;

import com.mumuca.diet.model.Height;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeightRepository extends JpaRepository<Height, String> {
    Optional<Height> findByIdAndUserId(String id, String userId);
}
