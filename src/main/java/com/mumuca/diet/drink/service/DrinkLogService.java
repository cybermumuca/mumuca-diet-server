package com.mumuca.diet.drink.service;

import com.mumuca.diet.drink.dto.CreateDrinkLogDTO;
import com.mumuca.diet.drink.dto.DrinkLogDTO;
import com.mumuca.diet.drink.dto.UpdateDrinkLogDTO;

import java.time.LocalDate;
import java.util.List;

public interface DrinkLogService {
    List<DrinkLogDTO> findDrinkLogsByDate(LocalDate date, String userId);

    DrinkLogDTO createDrinkLog(CreateDrinkLogDTO createDrinkLogDTO, String userId);

    void deleteDrinkLog(String drinkLogId, String userId);

    void updateDrinkLog(String drinkLogId, UpdateDrinkLogDTO updateDrinkLogDTO, String userId);
}
