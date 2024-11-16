package com.auth.entity;

import java.time.Instant;

import javax.persistence.Entity;

import com.auth.entity.config.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken extends BaseEntity {

    private String token;

    private boolean isLoggedOut;

    private Instant expiryDate;

    private String userId;

}
