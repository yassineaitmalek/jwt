package com.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.dto.AuthDTO;
import com.auth.dto.JwtResponse;
import com.auth.dto.RefreshTokenDTO;
import com.auth.dto.RoleDTO;
import com.auth.dto.UserDTO;
import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.service.JwtService;
import com.auth.service.RefreshTokenService;
import com.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/sign-up")
    public User createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PostMapping("/role")
    public Role createUser(@RequestBody RoleDTO roleDTO) {
        return userService.createRole(roleDTO.getRoleName());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test/admin")
    public String helloAdmin() {
        return "Hello Admin";
    }

    @GetMapping("/test/regular")
    public String helloRegular() {
        return "Hello regular";
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody AuthDTO authDTO) {
        return refreshTokenService.logIn(authDTO);
    }

    @PostMapping("/refresh-token")
    public JwtResponse refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        return refreshTokenService.refreshToken(refreshTokenDTO);
    }

}
