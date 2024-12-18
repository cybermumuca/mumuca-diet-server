package com.mumuca.diet.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mumuca.diet.dto.ErrorResponseDTO;
import com.mumuca.diet.security.JwtBlacklist;
import com.mumuca.diet.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
@Slf4j
@AllArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtBlacklist jwtBlacklist;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String jwtFromCookie = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwtFromCookie = cookie.getValue();

                    String finalJwtFromCookie = jwtFromCookie;
                    request = new HttpServletRequestWrapper(request) {
                        @Override
                        public String getHeader(String name) {
                            if ("Authorization".equalsIgnoreCase(name)) {
                                return "Bearer " + finalJwtFromCookie;
                            }
                            return super.getHeader(name);
                        }
                    };
                }
            }
        }

        if (jwtFromCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Jwt jwt = jwtService.decode(jwtFromCookie);

            if (jwtBlacklist.isTokenBlacklisted(jwt.getTokenValue())) {
                ErrorResponseDTO<Void> errorResponse = new ErrorResponseDTO<>(
                        request.getRequestURI(),
                        HttpServletResponse.SC_FORBIDDEN,
                        "Sign out already done",
                        null,
                        LocalDateTime.now()
                );

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

                return;
            }

            Authentication authentication = createBearerAuthentication(jwt);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException ex) {
            log.error("Erro ao validar JWT: {}", ex.getMessage());

            ErrorResponseDTO<Void> errorResponse = new ErrorResponseDTO<>(
                    request.getRequestURI(),
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or expired token",
                    null,
                    LocalDateTime.now()
            );

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Authentication createBearerAuthentication(Jwt jwt) {
        String userId = jwt.getSubject();

        List<String> roles = jwt
                .getClaimAsStringList("roles");

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());

        Map<String, Object> attributes = Map.of(
                "sub", userId,
                "roles", roles
        );

        OAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(
                userId,
                attributes,
                authorities
        );

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                jwt.getTokenValue(),
                jwt.getIssuedAt(),
                jwt.getExpiresAt()
        );

        return new BearerTokenAuthentication(principal, accessToken, authorities);
    }
}
