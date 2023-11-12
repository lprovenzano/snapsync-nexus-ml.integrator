package com.snapsync.nexus.entity.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
public class Credential {
    private final String grantType = "authorization_code";
    private Long clientId;
    private String clientSecret;
    private String code;
    private String redirectUrl;
    private String refreshToken;
}
