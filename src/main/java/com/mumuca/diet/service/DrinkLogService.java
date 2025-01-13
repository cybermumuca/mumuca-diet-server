package com.mumuca.diet.service;

import com.mumuca.diet.dto.drinklog.DrinkLogDTO;

import java.time.LocalDate;
import java.util.List;

public interface DrinkLogService {
    List<DrinkLogDTO> findDrinkLogsByDate(LocalDate date, String userId);
}
