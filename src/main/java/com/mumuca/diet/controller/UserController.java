package com.mumuca.diet.controller;

import com.mumuca.diet.dto.CompleteRegistrationDTO;
import com.mumuca.diet.dto.DiagnosisDTO;
import com.mumuca.diet.dto.RegistrationCompletedDTO;
import com.mumuca.diet.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/v1/me/complete-registration")
    public ResponseEntity<RegistrationCompletedDTO> completeRegistration(
            @Valid @RequestBody CompleteRegistrationDTO completeRegistrationDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is completing registration", jwt.getSubject());

        RegistrationCompletedDTO registrationCompletedDTO = userService.completeRegistration(completeRegistrationDTO, jwt.getSubject());

        log.info("User [{}] completed registration", jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(registrationCompletedDTO);
    }

    @GetMapping(path = "/v1/me/diagnosis")
    public ResponseEntity<DiagnosisDTO> getDiagnosis(
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is getting diagnosis", jwt.getSubject());

        DiagnosisDTO diagnosisDTO = userService.generateDiagnosis(jwt.getSubject());

        log.info("Diagnosis performed successfully to user [{}]", jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(diagnosisDTO);
    }
}
