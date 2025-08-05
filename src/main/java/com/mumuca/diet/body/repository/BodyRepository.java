package com.mumuca.diet.body.repository;

import com.mumuca.diet.body.model.Body;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BodyRepository extends JpaRepository<Body, String> {
    Optional<Body> findByIdAndUserId(String id, String userId);
    Optional<Body> findFirstByUserIdOrderByDateDesc(String userId);
    Page<Body> findByUserId(Pageable pageable, String userId);
    void deleteAllByUserId(String userId);
}
