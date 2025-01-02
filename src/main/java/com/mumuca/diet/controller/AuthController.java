package com.mumuca.diet.controller;

import com.mumuca.diet.dto.auth.SignInDTO;
import com.mumuca.diet.dto.auth.SignInResponseDTO;
import com.mumuca.diet.dto.auth.SignUpDTO;
import com.mumuca.diet.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/v1/auth/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        authService.signUp(signUpDTO);

        log.info("[{}] signed up", signUpDTO.email());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(path = "/v1/auth/sign-in")
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody SignInDTO signInDTO, HttpServletResponse response) {
        var signInResponse = authService.signIn(signInDTO);

        log.info("User with email [{}] signed in", signInDTO.email());

        Cookie jwtCookie = new Cookie("jwt", signInResponse.accessToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(Math.toIntExact(signInResponse.expiresIn()));

        response.addCookie(jwtCookie);

        return ResponseEntity.ok(signInResponse);
    }

    @PostMapping(path = "/v1/auth/sign-out")
    public ResponseEntity<Void> signOut(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletResponse response
    ) {
        log.info("User [{}] is logging out", jwt.getSubject());

        authService.signOut(jwt.getTokenValue(), jwt.getExpiresAt());

        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);

        response.addCookie(jwtCookie);

        log.info("User [{}] logged out successfully", jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
