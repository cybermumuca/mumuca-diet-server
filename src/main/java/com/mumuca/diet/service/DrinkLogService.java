package com.mumuca.diet.service;

import com.mumuca.diet.dto.drinklog.CreateDrinkLogDTO;
import com.mumuca.diet.dto.drinklog.DrinkLogDTO;
import com.mumuca.diet.dto.drinklog.UpdateDrinkLogDTO;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public interface DrinkLogService {
    List<DrinkLogDTO> findDrinkLogsByDate(LocalDate date, String userId);

    DrinkLogDTO createDrinkLog(CreateDrinkLogDTO createDrinkLogDTO, String userId);

    void deleteDrinkLog(String drinkLogId, String userId);

    void updateDrinkLog(String drinkLogId, UpdateDrinkLogDTO updateDrinkLogDTO, String userId);
}
