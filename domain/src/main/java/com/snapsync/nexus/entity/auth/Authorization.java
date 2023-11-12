package com.snapsync.nexus.entity.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
public class Authorization {
    private String accessToken;
    private String tokenType;
    private Long expiration;
    private Long userId;
    private String refreshToken;
}
