package com.mumuca.diet.service;

import com.mumuca.diet.dto.SignInDTO;
import com.mumuca.diet.dto.SignInResponseDTO;
import com.mumuca.diet.dto.SignUpDTO;

public interface UsersService {
    void signUp(SignUpDTO signUpDTO);
    SignInResponseDTO signIn(SignInDTO signInDTO);
}
