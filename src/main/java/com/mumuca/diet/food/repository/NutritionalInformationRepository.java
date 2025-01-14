package com.mumuca.diet.food.repository;

import com.mumuca.diet.food.model.NutritionalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionalInformationRepository extends JpaRepository<NutritionalInformation, String> {}
