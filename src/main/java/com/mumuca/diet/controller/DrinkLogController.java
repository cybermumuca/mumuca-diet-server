package com.mumuca.diet.controller;

import com.mumuca.diet.dto.drinklog.CreateDrinkLogDTO;
import com.mumuca.diet.dto.drinklog.DrinkLogDTO;
import com.mumuca.diet.dto.drinklog.UpdateDrinkLogDTO;
import com.mumuca.diet.service.DrinkLogService;
import com.mumuca.diet.validator.ValidUUID;
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

    @PostMapping(path = "/v1/drink-logs")
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

    @PutMapping(path = "/v1/drink-logs/{id}")
    public ResponseEntity<Void> updateMealLog(
            @PathVariable("id") @Valid @ValidUUID String drinkLogId,
            @Valid @RequestBody UpdateDrinkLogDTO updateDrinkLogDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is updating meal log [{}] with payload [{}]", jwt.getSubject(), drinkLogId, updateDrinkLogDTO);

        drinkLogService.updateDrinkLog(drinkLogId, updateDrinkLogDTO, jwt.getSubject());

        log.info("Drink log updated successfully. Drink log id: [{}], User: [{}]", drinkLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/drink-logs/{id}")
    public ResponseEntity<Void> deleteDrinkLog(
            @PathVariable("id") @Valid @ValidUUID String drinkLogId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is deleting drink log [{}]", jwt.getSubject(), drinkLogId);

        drinkLogService.deleteDrinkLog(drinkLogId, jwt.getSubject());

        log.info("Drink log deleted successfully. Meal log id: [{}], User: [{}]", drinkLogId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
