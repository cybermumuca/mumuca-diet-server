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

@Component
@AllArgsConstructor
public class UserValidationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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