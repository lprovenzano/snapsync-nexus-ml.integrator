package com.snapsync.nexus.entity.auth;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(setterPrefix = "set")
public class Authorization {
    private String accessToken;
    private String tokenType;
    private LocalDateTime expiration;
    private Long userId;
    private String refreshToken;
}
