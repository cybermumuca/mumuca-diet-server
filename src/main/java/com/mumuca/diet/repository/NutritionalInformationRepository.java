package com.mumuca.diet.repository;

import com.mumuca.diet.dto.meal.MealNutritionalInformationDTO;
import com.mumuca.diet.model.NutritionalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionalInformationRepository extends JpaRepository<NutritionalInformation, String> {}
