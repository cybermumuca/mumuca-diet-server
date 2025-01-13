package com.mumuca.diet.controller;

import com.mumuca.diet.dto.drinklog.CreateDrinkLogDTO;
import com.mumuca.diet.dto.drinklog.DrinkLogDTO;
import com.mumuca.diet.service.DrinkLogService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
@Slf4j
public class DrinkLogController {

    private DrinkLogService drinkLogService;

    @GetMapping(path = "/v1/drink-logs")
    public ResponseEntity<List<DrinkLogDTO>> getDrinkLogsByDate(
            @RequestParam(
                    value = "date",
                    required = false,
                    defaultValue = "#{T(java.time.LocalDate).now()}"
            )
            LocalDate date,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting drink logs on date [{}]", jwt.getSubject(), date);

        List<DrinkLogDTO> drinkLogDTOList =
                drinkLogService.findDrinkLogsByDate(date, jwt.getSubject());

        log.info("Drink logs found for user [{}] on date [{}]", jwt.getSubject(), date);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(drinkLogDTOList);
    }

    @PostMapping(path = "/v1/meal-logs")
    public ResponseEntity<DrinkLogDTO> createDrinkLog(
            @Valid @RequestBody CreateDrinkLogDTO createDrinkLogDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is creating a drink log with payload [{}]", jwt.getSubject(), createDrinkLogDTO);

        DrinkLogDTO drinkLogDTO = drinkLogService.createDrinkLog(createDrinkLogDTO, jwt.getSubject());

        log.info("Drink log created successfully. Drink Log Id: [{}], User: [{}]", drinkLogDTO.id(), jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(drinkLogDTO);
    }
}
