package com.mumuca.diet.auth.service;

import com.mumuca.diet.auth.dto.SignInDTO;
import com.mumuca.diet.auth.dto.SignInResponseDTO;
import com.mumuca.diet.auth.dto.SignUpDTO;
import com.mumuca.diet.auth.exception.CredentialsMismatchException;
import com.mumuca.diet.auth.exception.UserAlreadyExistsException;
import com.mumuca.diet.auth.model.Role;
import com.mumuca.diet.auth.model.User;
import com.mumuca.diet.auth.repository.RoleRepository;
import com.mumuca.diet.auth.repository.UserRepository;
import com.mumuca.diet.security.JwtBlacklist;
import com.mumuca.diet.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("AuthServiceImpl Integration Tests")
public class AuthServiceImplIntegrationTests {

    @Autowired
    private AuthServiceImpl sut;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private JwtBlacklist jwtBlacklist;

    @Nested
    @DisplayName("signUp tests")
    class SignUpTests {
        @Test
        @Transactional
        @DisplayName("should be able to sign up")
        void shouldBeAbleToSignUp() {
            // Arrange
            String firstName = "Samuel";
            String lastName = "Laurindo";
            String email = "samuel@email.com";
            String password = "12345678";

            SignUpDTO signUpDTO = new SignUpDTO(
                    firstName,
                    lastName,
                    email,
                    password
            );

            // Act
            sut.signUp(signUpDTO);

            // Assert
            var userInDatabase = userRepository.findByEmail(email).orElseThrow();

            assertThat(userInDatabase).isNotNull();
            assertThat(userInDatabase.getEmail()).isEqualTo(email);
            assertThat(passwordEncoder.matches(password, userInDatabase.getPassword())).isTrue();
            assertThat(userInDatabase.getFirstName()).isEqualTo(firstName);
            assertThat(userInDatabase.getLastName()).isEqualTo(lastName);
            assertThat(userInDatabase.getRoles())
                    .hasSize(1)
                    .extracting("authority")
                    .contains("USER");
        }

        @Test
        @Transactional
        @DisplayName("should throw UserAlreadyExistsException if email is already registered")
        void shouldThrowUserAlreadyExistsExceptionIfEmailIsAlreadyRegistered() {
            // Arrange
            String email = "samuel@email.com";

            User user = User.builder()
                    .firstName("Samuel")
                    .lastName("Laurindo")
                    .password(passwordEncoder.encode("12345678"))
                    .email(email)
                    .build();

            userRepository.save(user);

            SignUpDTO signUpDTO = new SignUpDTO("Samuel", "Laurindo", email, "newpassword");

            // Act & Assert
            assertThatThrownBy(() -> sut.signUp(signUpDTO))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining("User already registered with email");
        }
    }

    @Nested
    @DisplayName("signIn tests")
    class SignInTests {
        @Test
        @Transactional
        @DisplayName("should sign in with valid credentials")
        void shouldSignInWithValidCredentials() {
            // Arrange
            String email = "samuel@email.com";
            String password = "12345678";
            String hashedPassword = passwordEncoder.encode(password);

            Role userRole = roleRepository.findByAuthority("USER")
                    .orElseThrow();

            User user = User.builder()
                    .firstName("Samuel")
                    .lastName("Laurindo")
                    .email(email)
                    .password(hashedPassword)
                    .roles(Set.of(userRole))
                    .build();

            userRepository.save(user);

            SignInDTO signInDTO = new SignInDTO(email, password);

            // Act
            SignInResponseDTO response = sut.signIn(signInDTO);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.accessToken()).isNotBlank();
            assertThat(response.expiresIn()).isEqualTo(604800);

            var tokenDecoded = jwtDecoder.decode(response.accessToken());

            assertThat(tokenDecoded).isNotNull();
            assertThat(tokenDecoded.getSubject()).isEqualTo(user.getId());
            assertThat(tokenDecoded.getClaimAsStringList("roles")).hasSize(1).contains("USER");
        }

        @Test
        @Transactional
        @DisplayName("should throw CredentialsMismatchException if email does not exist")
        void shouldThrowCredentialsMismatchExceptionIfEmailDoesNotExist() {
            // Arrange
            SignInDTO signInDTO = new SignInDTO("nonexistent@email.com", "12345678");

            // Act & Assert
            assertThatThrownBy(() -> sut.signIn(signInDTO))
                    .isInstanceOf(CredentialsMismatchException.class)
                    .hasMessageContaining("Email or password is invalid");
        }

        @Test
        @Transactional
        @DisplayName("should throw CredentialsMismatchException if password is incorrect")
        void shouldThrowCredentialsMismatchExceptionIfPasswordIsIncorrect() {
            // Arrange
            String email = "samuel@email.com";
            String password = "12345678";
            String hashedPassword = passwordEncoder.encode(password);

            Role userRole = roleRepository.findByAuthority("USER")
                    .orElseThrow();

            User user = User.builder()
                    .firstName("Samuel")
                    .lastName("Laurindo")
                    .email(email)
                    .password(hashedPassword)
                    .roles(Set.of(userRole))
                    .build();

            userRepository.save(user);

            SignInDTO signInDTO = new SignInDTO(email, "wrongpassword");

            // Act & Assert
            assertThatThrownBy(() -> sut.signIn(signInDTO))
                    .isInstanceOf(CredentialsMismatchException.class)
                    .hasMessageContaining("Email or password is invalid");
        }
    }

    @Nested
    @DisplayName("signOut tests")
    class SignOutTests {
        @Test
        @DisplayName("should add token to blacklist on sign out")
        void shouldAddTokenToBlacklistOnSignOut() {
            // Arrange
            String token = "some-valid-token";
            Instant expiresAt = Instant.now().plusSeconds(3600);

            // Act
            sut.signOut(token, expiresAt);

            // Assert
            assertThat(jwtBlacklist.isTokenBlacklisted(token)).isTrue();
        }
    }

}
