package com.mumuca.diet.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mumuca.diet.dto.ErrorResponseDTO;
import com.mumuca.diet.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserValidationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/v1/calculator",
            "/api/v1/auth/sign-in",
            "/api/v1/auth/sign-up"
    );

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
        }

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String userId = jwt.getSubject();

            if (!userRepository.existsUserById((userId))) {
                ErrorResponseDTO<Void> errorResponse = new ErrorResponseDTO<>(
                        request.getRequestURI(),
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Oops!",
                        null,
                        LocalDateTime.now()
                );

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}