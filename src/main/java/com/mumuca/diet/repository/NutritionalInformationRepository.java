package com.mumuca.diet.repository;

import com.mumuca.diet.model.NutritionalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionalInformationRepository extends JpaRepository<NutritionalInformation, String> {}
