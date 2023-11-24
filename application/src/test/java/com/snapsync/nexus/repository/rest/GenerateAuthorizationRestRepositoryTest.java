package com.snapsync.nexus.repository.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapsync.nexus.entity.auth.Authorization;
import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.entity.auth.GrantType;
import com.snapsync.nexus.interfaces.GenerateAuthorizationRepository;
import com.snapsync.nexus.repository.configuration.RestRepositoryTest;
import com.snapsync.nexus.utils.Util;
import com.snapsync.nexus.utils.unit.util.Json;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;


class GenerateAuthorizationRestRepositoryTest extends RestRepositoryTest {
    @Autowired
    private GenerateAuthorizationRepository generateAuthorizationRepository;

    private static final String PATH = "/oauth/token";

    @Autowired
    public GenerateAuthorizationRestRepositoryTest(RestTemplate restTemplate) {
        super(restTemplate);
    }


    @Test
    @DisplayName("Given a credential, when exists in ML, then ok")
    void test1() {
        //Arrange
        final String jsonResponse = Json.parse("/generate-authorization/responseOK.json");
        final Credential credential = Credential.builder()
                .setGrantType(GrantType.AUTHORIZATION_CODE.getName())
                .setClientId(6663702242922673L)
                .setClientSecret("uX4aBfrS9JriB921ay3UKA2lvXo9TY3I")
                .setCode("TG-655052b1d76fad000182dc0a-92932223")
                .setRedirectUri("https://test-hook.site/3df7fcb9-3c7b-4c8d-b613-8797c0636281")
                .build();
        mockServer.expect(requestTo(Util.getEndpoint(URL_ML, PATH)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_FORM_URLENCODED));
        //Act
        final Authorization response = generateAuthorizationRepository.execute(credential);

        //Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("APP_USR-5362092127344837-111123-fe9055b8e9265f17da2d36e15d4651b8-92932223", response.getAccessToken());
        Assertions.assertTrue(response.getExpiration().isAfter(Util.dateTimeNow()));
        Assertions.assertEquals(92932223L, response.getUserId());
        Assertions.assertEquals("TG-655046f45379a00001c9a4f8-92932223", response.getRefreshToken());
        Assertions.assertEquals("Bearer", response.getTokenType());
    }

    @Test
    @DisplayName("Given a credential, when not exists in ML, then bad request exception")
    void test2() {
        //Arrange
        final Credential credential = Credential.builder()
                .setGrantType(GrantType.AUTHORIZATION_CODE.getName())
                .setClientId(6663702242922673L)
                .setClientSecret("uX4aBfrS9JriB921ay3UKA2lvXo9TY3I")
                .setCode("TG-655052b1d76fad000182dc0a-92932223")
                .setRedirectUri("https://test-hook.site/3df7fcb9-3c7b-4c8d-b613-8797c0636281")
                .build();
        mockServer.expect(requestTo(Util.getEndpoint(URL_ML, PATH)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withBadRequest());
        //Act
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> generateAuthorizationRepository.execute(credential));
    }

    @Test
    @DisplayName("Given a credential, when token is expired, then unauthorized exception")
    void test3() {
        //Arrange
        final Credential credential = Credential.builder()
                .setGrantType(GrantType.AUTHORIZATION_CODE.getName())
                .setClientId(6663702242922673L)
                .setClientSecret("uX4aBfrS9JriB921ay3UKA2lvXo9TY3I")
                .setCode("TG-655052b1d76fad000182dc0a-92932223")
                .setRedirectUri("https://test-hook.site/3df7fcb9-3c7b-4c8d-b613-8797c0636281")
                .build();
        mockServer.expect(requestTo(Util.getEndpoint(URL_ML, PATH)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withUnauthorizedRequest());
        //Act
        Assertions.assertThrows(HttpClientErrorException.Unauthorized.class, () -> generateAuthorizationRepository.execute(credential));
    }

}