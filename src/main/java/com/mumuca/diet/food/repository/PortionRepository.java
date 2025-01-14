package com.mumuca.diet.food.repository;

import com.mumuca.diet.food.model.Portion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortionRepository extends JpaRepository<Portion, String> {}
