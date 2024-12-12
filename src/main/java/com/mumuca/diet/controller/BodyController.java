package com.mumuca.diet.controller;

import com.mumuca.diet.dto.body.BodyDTO;
import com.mumuca.diet.dto.body.BodyRegistryDTO;
import com.mumuca.diet.dto.body.BodyRegistryUpdateDTO;
import com.mumuca.diet.service.BodyService;
import com.mumuca.diet.validator.ValidUUID;
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
public class BodyController {

    private final BodyService bodyService;

    @PostMapping(path = "/v1/bodies")
    public ResponseEntity<BodyDTO> registerBody(
            @Valid @RequestBody BodyRegistryDTO bodyRegistryDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is registering a body with payload [{}]", jwt.getSubject(), bodyRegistryDTO);

        BodyDTO bodyDTO = bodyService.registerBody(bodyRegistryDTO, jwt.getSubject());

        log.info("Body register created successfully. Body id: [{}], User: [{}]", bodyDTO.id(), jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bodyDTO);
    }

    @GetMapping(path = "/v1/bodies/{id}")
    public ResponseEntity<BodyDTO> getBodyRegistry(
            @PathVariable("id") @Valid @ValidUUID String bodyId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is requesting a body registry with id [{}]", jwt.getSubject(), bodyId);

        BodyDTO bodyDTO = bodyService.getBodyRegistry(bodyId, jwt.getSubject());

        log.info("Body registry returned for user [{}]. Body registry id: [{}]", jwt.getSubject(), bodyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bodyDTO);
    }

    @PutMapping(path = "/v1/bodies/{id}")
    public ResponseEntity<Void> updateBodyRegistry(
            @PathVariable("id") @Valid @ValidUUID String bodyId,
            @Valid @RequestBody BodyRegistryUpdateDTO bodyRegistryUpdateDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is updating a body registry [{}] with payload [{}]", jwt.getSubject(), bodyId, bodyRegistryUpdateDTO);

        bodyService.updateBodyRegistry(bodyId, bodyRegistryUpdateDTO, jwt.getSubject());

        log.info("Body registry updated successfully. Body registry id: [{}], User: [{}]", bodyId,  jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/v1/bodies/{id}")
    public ResponseEntity<Void> deleteBodyRegistry(
            @PathVariable("id") @Valid @ValidUUID String bodyId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("User [{}] is deleting Body Registry [{}]", jwt.getSubject(), bodyId);

        bodyService.deleteBodyRegistry(bodyId, jwt.getSubject());

        log.info("Body registry deleted successfully. Body registry id: [{}], User: [{}]", bodyId, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
