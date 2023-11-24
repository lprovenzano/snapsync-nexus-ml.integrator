package com.snapsync.nexus.repository.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class RestRepositoryTest {
    protected MockRestServiceServer mockServer;
    private RestTemplate restTemplate;

    protected static final String URL_ML = "https://api.mercadolibre.com";
    protected static final String URL_MP = "https://api.mercadopago.com";

    public RestRepositoryTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
}
