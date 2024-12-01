package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.SignInDTO;
import com.mumuca.diet.dto.SignInResponseDTO;
import com.mumuca.diet.exception.CredentialsMismatchException;
import com.mumuca.diet.exception.UserAlreadyExistsException;
import com.mumuca.diet.dto.SignUpDTO;
import com.mumuca.diet.model.Role;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.RolesRepository;
import com.mumuca.diet.repository.UsersRepository;
import com.mumuca.diet.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Override
    public void signUp(SignUpDTO signUpDTO) {
        Optional<User> optionalUser = usersRepository.findByEmail(signUpDTO.email());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with email: " + signUpDTO.email());
        }

        Role userRole = rolesRepository.findByAuthority("USER")
                .orElseThrow();

        User user = new User();

        user.setRoles(Set.of(userRole));
        user.setFirstName(signUpDTO.firstName());
        user.setLastName(signUpDTO.lastName());
        user.setEmail(signUpDTO.email());
        user.setPassword(passwordEncoder.encode(signUpDTO.password()));

        usersRepository.save(user);
    }

    @Override
    public SignInResponseDTO signIn(SignInDTO signInDTO) {
        var user = usersRepository.findByEmail(signInDTO.email());

        if (user.isEmpty() || !passwordEncoder.matches(signInDTO.password(), user.get().getPassword())) {
            throw new CredentialsMismatchException("Email or password is invalid");
        }

        var now = Instant.now();
        long expiresIn = 604800;

        List<String> roles = user.get().getRoles()
                .stream()
                .map(Role::getAuthority)
                .toList();

        var claims = JwtClaimsSet.builder()
                .issuer("Mumuca Diet")
                .subject(user.get().getId())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("roles", roles)
                .build();

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new SignInResponseDTO(jwt, expiresIn);
    }
}
