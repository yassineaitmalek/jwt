package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findTopByToken(String token);

}
