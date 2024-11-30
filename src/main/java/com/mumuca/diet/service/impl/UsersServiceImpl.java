package com.mumuca.diet.service.impl;

import com.mumuca.diet.exception.UserAlreadyExistsException;
import com.mumuca.diet.dto.SignUpDTO;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.UsersRepository;
import com.mumuca.diet.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    public void signUp(SignUpDTO signUpDTO) {
        Optional<User> optionalUser = usersRepository.findByEmail(signUpDTO.email());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with email: " + signUpDTO.email());
        }

        User user = new User();

        user.setFirstName(signUpDTO.firstName());
        user.setLastName(signUpDTO.lastName());
        user.setEmail(signUpDTO.email());
        user.setPassword(signUpDTO.password());

        usersRepository.save(user);
    }
}
