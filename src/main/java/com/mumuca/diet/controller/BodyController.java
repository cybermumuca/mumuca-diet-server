package com.mumuca.diet.controller;

import com.mumuca.diet.dto.body.BodyDTO;
import com.mumuca.diet.dto.body.BodyRegistryDTO;
import com.mumuca.diet.dto.body.BodyRegistryUpdateDTO;
import com.mumuca.diet.service.BodyService;
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
public class BodyController {

    private final BodyService bodyService;

    @PostMapping(path = "/v1/bodies")
    public ResponseEntity<BodyDTO> registerBody(
            @Valid @RequestBody BodyRegistryDTO bodyRegistryDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        BodyDTO bodyDTO = bodyService.registerBody(bodyRegistryDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bodyDTO);
    }

    @GetMapping(path = "/v1/bodies/{id}")
    public ResponseEntity<BodyDTO> getBodyRegistry(
            @PathVariable("id") String bodyId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        BodyDTO bodyDTO = bodyService.getBodyRegistry(bodyId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bodyDTO);
    }

    @PutMapping(path = "/v1/bodies/{id}")
    public ResponseEntity<Void> updateBodyRegistry(
            @PathVariable("id") String bodyId,
            @Valid @RequestBody BodyRegistryUpdateDTO bodyRegistryUpdateDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        bodyService.updateBodyRegistry(bodyId, bodyRegistryUpdateDTO, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/bodies/{id}")
    public ResponseEntity<Void> deleteBodyRegistry(
            @PathVariable("id") String bodyId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        bodyService.deleteBodyRegistry(bodyId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
