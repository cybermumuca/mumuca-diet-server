package com.mumuca.diet.drink.service.impl;

import com.mumuca.diet.drink.mapper.DrinkLogMapper;
import com.mumuca.diet.drink.dto.CreateDrinkLogDTO;
import com.mumuca.diet.drink.dto.DrinkLogDTO;
import com.mumuca.diet.drink.dto.UpdateDrinkLogDTO;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.drink.model.DrinkLog;
import com.mumuca.diet.model.User;
import com.mumuca.diet.drink.repository.DrinkLogRepository;
import com.mumuca.diet.drink.service.DrinkLogService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DrinkLogServiceImpl implements DrinkLogService {

    private final DrinkLogRepository drinkLogRepository;
    private final DrinkLogMapper drinkLogMapper;

    @Override
    @Transactional
    public List<DrinkLogDTO> findDrinkLogsByDate(LocalDate date, String userId) {
        List<DrinkLog> drinkLogs = drinkLogRepository.findByDateAndUserId(date, userId);

        return drinkLogs
                .stream()
                .map(drinkLogMapper::fromDrinkLogToDrinkLogDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DrinkLogDTO createDrinkLog(CreateDrinkLogDTO createDrinkLogDTO, String userId) {
        User user = new User(userId);

        DrinkLog drinkLog = drinkLogMapper.fromCreateDrinkLogDTOToDrinkLog(createDrinkLogDTO, user);

        drinkLogRepository.save(drinkLog);

        return drinkLogMapper.fromDrinkLogToDrinkLogDTO(drinkLog);
    }

    @Override
    @Transactional
    public void deleteDrinkLog(String drinkLogId, String userId) {
        DrinkLog drinkLog = drinkLogRepository.findDrinkLogByIdAndUserId(drinkLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Drink Log Not Found."));

        drinkLogRepository.deleteById(drinkLog.getId());
    }

    @Override
    @Transactional
    public void updateDrinkLog(String drinkLogId, UpdateDrinkLogDTO updateDrinkLogDTO, String userId) {
        DrinkLog drinkLogToUpdate = drinkLogRepository.findDrinkLogByIdAndUserId(drinkLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Drink Log Not Found."));

        drinkLogMapper.updateDrinkLogFromDTO(updateDrinkLogDTO, drinkLogToUpdate);

        drinkLogRepository.save(drinkLogToUpdate);
    }
}
