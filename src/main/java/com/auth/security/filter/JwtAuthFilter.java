package com.auth.security.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth.security.service.UserContainer;
import com.auth.security.service.UserLoadService;
import com.auth.service.JwtService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserLoadService userLoadService;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserToken {
        private String token;
        private String userId;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        Optional.ofNullable("Authorization")
                .map(request::getHeader)
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(this::extractUserToken)
                .filter(e -> SecurityContextHolder.getContext().getAuthentication() == null)
                .ifPresent(e -> addAuthentication(request, e));

        filterChain.doFilter(request, response);

    }

    public UserToken extractUserToken(String authHeader) {
        String token = jwtService.extractToken(authHeader);
        return UserToken.builder()
                .token(token)
                .userId(jwtService.extractUserId(token))
                .build();
    }

    public void addAuthentication(HttpServletRequest request, UserToken userToken) {

        UserContainer userDetails = userLoadService.loadUserById(userToken.getUserId());
        if (!jwtService.validateToken(userToken.getToken(), userDetails)) {
            return;
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
