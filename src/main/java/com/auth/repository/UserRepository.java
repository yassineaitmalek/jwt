package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findTopByUsername(String username);

}
