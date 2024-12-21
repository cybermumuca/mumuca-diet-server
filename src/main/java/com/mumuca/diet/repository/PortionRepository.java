package com.mumuca.diet.repository;

import com.mumuca.diet.model.Portion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortionRepository extends JpaRepository<Portion, String> {}
