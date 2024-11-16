package com.auth.service;

import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.dto.UserDTO;
import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.repository.RoleRepository;
import com.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public User createUser(UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(getOrSaveRoles(userDTO.getRoles()));
        return userRepository.save(user);

    }

    public String formatRoleName(String roleName) {
        return roleName.trim().toUpperCase();
    }

    public Role createRole(String name) {
        return Optional.of(name)
                .map(this::formatRoleName)
                .map(roleRepository::findTopByName)
                .filter(e -> !e.isPresent())
                .map(e -> Role.builder().name(formatRoleName(name)).build())
                .map(roleRepository::save)
                .orElseGet(() -> null);

    }

    public List<Role> getOrSaveRoles(List<String> rolesName) {

        return Optional.ofNullable(rolesName)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .map(this::formatRoleName)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::ofNullable))
                .filter(e -> !e.isEmpty())
                .map(roleRepository::findAllByNameIn)
                .orElseGet(Collections::emptyList);

    }

}
