package com.mumuca.diet.auth.service;

import com.mumuca.diet.auth.dto.SignInDTO;
import com.mumuca.diet.auth.dto.SignInResponseDTO;
import com.mumuca.diet.auth.dto.SignUpDTO;

import java.time.Instant;

public interface AuthService {
    void signUp(SignUpDTO signUpDTO);
    SignInResponseDTO signIn(SignInDTO signInDTO);
    void signOut(String tokenValue, Instant expiresIn);
}
