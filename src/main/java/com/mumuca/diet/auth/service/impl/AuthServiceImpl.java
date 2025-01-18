package com.mumuca.diet.auth.service.impl;

import com.mumuca.diet.auth.dto.SignInDTO;
import com.mumuca.diet.auth.dto.SignInResponseDTO;
import com.mumuca.diet.auth.dto.SignUpDTO;
import com.mumuca.diet.auth.exception.CredentialsMismatchException;
import com.mumuca.diet.auth.exception.UserAlreadyExistsException;
import com.mumuca.diet.auth.model.Role;
import com.mumuca.diet.auth.model.User;
import com.mumuca.diet.auth.repository.RoleRepository;
import com.mumuca.diet.auth.repository.UserRepository;
import com.mumuca.diet.service.impl.JwtBlacklist;
import com.mumuca.diet.auth.service.AuthService;
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
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtBlacklist jwtBlacklist;

    @Override
    public void signUp(SignUpDTO signUpDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(signUpDTO.email());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with email: " + signUpDTO.email());
        }

        Role userRole = roleRepository.findByAuthority("USER")
                .orElseThrow();

        User user = new User();

        user.setRoles(Set.of(userRole));
        user.setFirstName(signUpDTO.firstName());
        user.setLastName(signUpDTO.lastName());
        user.setEmail(signUpDTO.email());
        user.setPassword(passwordEncoder.encode(signUpDTO.password()));

        userRepository.save(user);
    }

    @Override
    public SignInResponseDTO signIn(SignInDTO signInDTO) {
        var user = userRepository.findByEmail(signInDTO.email());

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

    @Override
    public void signOut(String tokenValue, Instant expiresIn) {
        jwtBlacklist.addToBlacklist(tokenValue, expiresIn);
    }
}
