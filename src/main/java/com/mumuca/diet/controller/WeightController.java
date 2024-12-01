package com.mumuca.diet.controller;

import com.mumuca.diet.dto.WeightDTO;
import com.mumuca.diet.dto.WeightRegistryDTO;
import com.mumuca.diet.service.WeightService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class WeightController {

    private final WeightService weightService;

    @PostMapping(path = "/v1/weights")
    public ResponseEntity<WeightDTO> registerWeight(
            @Valid @RequestBody WeightRegistryDTO weightRegistryDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        WeightDTO weightDTO = weightService.createRegistry(weightRegistryDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(weightDTO);
    }
}
