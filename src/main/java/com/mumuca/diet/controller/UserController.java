package com.mumuca.diet.controller;

import com.mumuca.diet.dto.CompleteRegistrationDTO;
import com.mumuca.diet.dto.DiagnosisDTO;
import com.mumuca.diet.dto.RegistrationCompletedDTO;
import com.mumuca.diet.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/v1/me/complete-registration")
    public ResponseEntity<RegistrationCompletedDTO> completeRegistration(
            @Valid @RequestBody CompleteRegistrationDTO completeRegistrationDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        RegistrationCompletedDTO registrationCompletedDTO = userService.completeRegistration(completeRegistrationDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(registrationCompletedDTO);
    }

    @GetMapping(path = "/v1/me/diagnosis")
    public ResponseEntity<DiagnosisDTO> getDiagnosis(
            @AuthenticationPrincipal Jwt jwt
    ) {
        DiagnosisDTO diagnosisDTO = userService.generateDiagnosis(jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(diagnosisDTO);
    }
}
