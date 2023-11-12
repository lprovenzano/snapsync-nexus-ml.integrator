package com.snapsync.nexus.entity.auth;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(setterPrefix = "set")
@ToString
public class Credential {
    private final String grantType = "authorization_code";
    private Long clientId;
    private String clientSecret;
    private String code;
    private String redirectUri;
}
