package com.snapsync.nexus.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.entity.auth.GrantType;
import com.snapsync.nexus.interfaces.GenerateAuthorizationRepository;
import com.snapsync.nexus.utils.Util;
import com.snapsync.nexus.utils.unit.util.Json;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenerateAuthorizationRestRepositoryTest {
    @Autowired
    private GenerateAuthorizationRepository generateAuthorizationRepository;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private static final String URL = "https://api.mercadolibre.com";
    private static final String PATH = "/oauth/token";

    @BeforeAll
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("Given a credential, when not exists in ML, then bad request exception")
    void test1() {
        final String jsonResponse = Json.parse("/generate-authorization/responseOK.json");
        final Credential credential = Credential.builder()
                .setGrantType(GrantType.AUTHORIZATION_CODE.getName())
                .setClientId(6663702242922673L)
                .setClientSecret("uX4aBfrS9JriB921ay3UKA2lvXo9TY3I")
                .setCode("TG-655052b1d76fad000182dc0a-92932223")
                .setRedirectUri("https://webhook.site/3df7fcb9-3c7b-4c8d-b613-8797c0636281")
                .build();
        mockServer.expect(requestTo(Util.getEndpoint(URL, PATH)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));
        this.generateAuthorizationRepository.execute(credential);
    }

}