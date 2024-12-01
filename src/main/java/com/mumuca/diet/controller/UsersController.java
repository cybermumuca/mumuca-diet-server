package com.mumuca.diet.controller;

import com.mumuca.diet.dto.SignInDTO;
import com.mumuca.diet.dto.SignInResponseDTO;
import com.mumuca.diet.dto.SignUpDTO;
import com.mumuca.diet.service.UsersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping(path = "/v1/auth/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        usersService.signUp(signUpDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(path = "/v1/auth/sign-in")
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody SignInDTO signInDTO) {
        var signInResponse = usersService.signIn(signInDTO);
        return ResponseEntity.ok(signInResponse);
    }
}
