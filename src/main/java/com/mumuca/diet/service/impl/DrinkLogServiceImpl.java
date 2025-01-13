package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.drinklog.CreateDrinkLogDTO;
import com.mumuca.diet.dto.drinklog.DrinkLogDTO;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.model.DrinkLog;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.DrinkLogRepository;
import com.mumuca.diet.service.DrinkLogService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DrinkLogServiceImpl implements DrinkLogService {

    private DrinkLogRepository drinkLogRepository;

    @Override
    @Transactional
    public List<DrinkLogDTO> findDrinkLogsByDate(LocalDate date, String userId) {
        List<DrinkLog> drinkLogs = drinkLogRepository.findByDateAndUserId(date, userId);

        return drinkLogs
                .stream()
                .map(drinkLog -> new DrinkLogDTO(
                        drinkLog.getId(),
                        drinkLog.getDate(),
                        drinkLog.getTime(),
                        drinkLog.getLiquidIntake()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DrinkLogDTO createDrinkLog(CreateDrinkLogDTO createDrinkLogDTO, String userId) {
        User user = new User(userId);

        DrinkLog drinkLog = new DrinkLog();

        drinkLog.setDate(createDrinkLogDTO.date());
        drinkLog.setTime(createDrinkLogDTO.time());
        drinkLog.setLiquidIntake(createDrinkLogDTO.liquidIntake());
        drinkLog.setUser(user);

        drinkLogRepository.save(drinkLog);

        return new DrinkLogDTO(
                drinkLog.getId(),
                drinkLog.getDate(),
                drinkLog.getTime(),
                drinkLog.getLiquidIntake()
        );
    }

    @Override
    @Transactional
    public void deleteDrinkLog(String drinkLogId, String userId) {
        DrinkLog drinkLog = drinkLogRepository.findDrinkLogByIdAndUserId(drinkLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Drink Log Not Found."));

        drinkLogRepository.deleteById(drinkLog.getId());
    }
}
