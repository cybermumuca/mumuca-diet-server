package com.mumuca.diet.controller;

import com.mumuca.diet.dto.HeightDTO;
import com.mumuca.diet.dto.HeightRegistryDTO;
import com.mumuca.diet.service.HeightService;
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
public class HeightController {

    private final HeightService heightService;

    @PostMapping(path = "/v1/heights")
    public ResponseEntity<HeightDTO> createHeightRegistry(
            @Valid @RequestBody HeightRegistryDTO heightRegistryDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        HeightDTO heightDTO = heightService.createHeightRegistry(heightRegistryDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(heightDTO);
    }
}
