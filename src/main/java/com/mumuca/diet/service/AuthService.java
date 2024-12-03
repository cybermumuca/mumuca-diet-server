package com.mumuca.diet.service;

import com.mumuca.diet.dto.auth.SignInDTO;
import com.mumuca.diet.dto.auth.SignInResponseDTO;
import com.mumuca.diet.dto.auth.SignUpDTO;

public interface AuthService {
    void signUp(SignUpDTO signUpDTO);
    SignInResponseDTO signIn(SignInDTO signInDTO);
}
