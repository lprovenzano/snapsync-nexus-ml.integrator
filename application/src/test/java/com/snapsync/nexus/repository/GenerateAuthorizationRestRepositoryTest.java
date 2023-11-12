package com.snapsync.nexus.repository;

import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.interfaces.GenerateAuthorizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

// TODO: Make unit test, like PingEntrypointTest
@SpringBootTest
class GenerateAuthorizationRestRepositoryTest {
    @Autowired
    private GenerateAuthorizationRepository generateAuthorizationRepository;

    @Test
    @DisplayName("Given a credential, when not exists in ML, then bad request exception")
    void test1() {
        final Credential credential = Credential.builder()
                .setClientId(6663702242922673L)
                .setClientSecret("uX4aBfrS9JriB921ay3UKA2lvXo9TY3I")
                .setCode("TG-655052b1d76fad000182dc0a-92932223")
                .setRedirectUri("https://webhook.site/3df7fcb9-3c7b-4c8d-b613-8797c0636281")
                .build();
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> this.generateAuthorizationRepository.execute(credential));
    }

}