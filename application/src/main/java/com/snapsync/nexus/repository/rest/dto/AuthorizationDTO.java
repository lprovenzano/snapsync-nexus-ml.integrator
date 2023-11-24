package com.snapsync.nexus.repository.rest.dto;

import com.snapsync.nexus.entity.auth.Authorization;
import com.snapsync.nexus.exception.validation.ValidationException;
import com.snapsync.nexus.utils.Util;
import lombok.*;


@Builder(setterPrefix = "set")
@EqualsAndHashCode(callSuper = true)
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationDTO extends DomainDTO<Authorization> {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private Long userId;
    private String refreshToken;

    @Override
    protected Authorization mapper() {
        return Authorization.builder()
                .setAccessToken(this.getAccessToken())
                .setTokenType(this.getTokenType())
                .setExpiration(Util.dateTimeNow().plusSeconds(this.getExpiresIn()))
                .setUserId(this.getUserId())
                .setRefreshToken(this.getRefreshToken())
                .build();
    }

    @Override
    protected void checker() {
        if (Util.isNullOrEmpty(this.getAccessToken())) {
            throw new ValidationException("Invalid access token from ML.");
        }
        if (Util.isNullOrEmpty(this.getTokenType())) {
            throw new ValidationException("Invalid token type from ML.");
        }
        if (this.getExpiresIn() == null) {
            throw new ValidationException("Invalid expiration token time from ML.");
        }
        if (this.getUserId() == null) {
            throw new ValidationException("Not found user_id from ML.");
        }
        if (Util.isNullOrEmpty(this.getRefreshToken())) {
            throw new ValidationException("Invalid access token from ML.");
        }
    }
}
