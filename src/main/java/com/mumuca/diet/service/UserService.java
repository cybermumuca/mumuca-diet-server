package com.mumuca.diet.service;

import com.mumuca.diet.dto.SignInDTO;
import com.mumuca.diet.dto.SignInResponseDTO;
import com.mumuca.diet.dto.SignUpDTO;

public interface UserService {
    void signUp(SignUpDTO signUpDTO);
    SignInResponseDTO signIn(SignInDTO signInDTO);
}
