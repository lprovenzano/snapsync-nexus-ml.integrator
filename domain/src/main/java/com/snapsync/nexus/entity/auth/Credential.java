package com.snapsync.nexus.entity.auth;

import lombok.*;

@Data
@Builder(setterPrefix = "set")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Credential {
    private String grantType;
    private Long clientId;
    private String clientSecret;
    private String code;
    private String redirectUri;
    private String refreshToken;
}
