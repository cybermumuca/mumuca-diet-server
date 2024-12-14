package com.mumuca.diet.service;

import com.mumuca.diet.dto.auth.SignInDTO;
import com.mumuca.diet.dto.auth.SignInResponseDTO;
import com.mumuca.diet.dto.auth.SignUpDTO;

import java.time.Instant;

public interface AuthService {
    void signUp(SignUpDTO signUpDTO);
    SignInResponseDTO signIn(SignInDTO signInDTO);
    void signOut(String tokenValue, Instant expiresIn);
}
