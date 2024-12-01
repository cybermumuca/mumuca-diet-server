package com.mumuca.diet.controller;

import com.mumuca.diet.dto.*;
import com.mumuca.diet.service.HeightService;
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

    @GetMapping(path = "/v1/heights/{id}")
    public ResponseEntity<HeightDTO> getHeightRegistry(
            @PathVariable("id") String heightId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        HeightDTO heightDTO = heightService.getHeightRegistry(heightId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(heightDTO);
    }

    @PutMapping(path = "/v1/heights/{id}")
    public ResponseEntity<HeightDTO> updateHeightRegistry(
            @PathVariable(value = "id") String heightId,
            @Valid @RequestBody HeightUpdateDTO heightUpdateDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        HeightDTO heightDTO = heightService.updateHeightRegistry(heightId, heightUpdateDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(heightDTO);
    }

    @DeleteMapping(path = "/v1/heights/{id}")
    public ResponseEntity<Void> deleteHeightRegistry(
            @PathVariable(value = "id") String heightId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        heightService.deleteHeightRegistry(heightId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
