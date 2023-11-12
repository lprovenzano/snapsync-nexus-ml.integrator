package com.snapsync.nexus.utils.unit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.entity.auth.GrantType;
import com.snapsync.nexus.utils.HttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class HttpClientTest {

    @InjectMocks
    private HttpClient httpClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private static final String URI = "/test/dev";

    private static final Map<String, List<String>> CUSTOM_HEADERS = Map.of("custom-header-key", List.of("custom-header-value"));
    private static final Map<String, List<String>> DEFAULT_HEADERS = new HashMap<>();

    @BeforeEach
    void init() {
        DEFAULT_HEADERS.putIfAbsent("Content-Type", List.of(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        DEFAULT_HEADERS.putIfAbsent("Accept", List.of(MediaType.APPLICATION_JSON_VALUE));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("Given get request, when there are no errors, then return ok.")
    void get(int index) {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(index);
        final HttpHeaders customHeaders = getCustomHeaders(headers);
        final String response;

        Mockito.when(restTemplate.exchange(
                eq(URI),
                eq(HttpMethod.GET),
                eq(new HttpEntity<>(customHeaders)),
                eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        // Act
        response = httpClient.get(URI, headers);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("{}", response);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("Given post request with body, when there are no errors, then return ok.")
    void post(int index) throws JsonProcessingException {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(index);
        final HttpHeaders customHeaders = getCustomHeaders(headers);
        final Credential body = Credential.builder()
                .setClientId(6663702242922673L)
                .setClientSecret("uX4aBfrS9JriB921ay3UKA2lvXo9TY3I")
                .setCode("TG-655052b1d76fad000182dc0a-92932223")
                .setRedirectUri("https://webhook.site/3df7fcb9-3c7b-4c8d-b613-8797c0636281")
                .build();
        final String jsonBody = "{}";

        Mockito.when(restTemplate.exchange(
                eq(URI),
                eq(HttpMethod.POST),
                eq(new HttpEntity<>(jsonBody, customHeaders)),
                eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.writeValueAsString(any(Object.class))).thenReturn(jsonBody);
        // Act
        final String response = httpClient.post(URI, headers, body);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("{}", response);
    }

    @Test
    @DisplayName("Given post request with body, when the encoding is APPLICATION_FORM_URLENCODED_VALUE, then return ok.")
    void post2() {
        // Arrange
        HttpHeaders customHeaders = getCustomHeaders(DEFAULT_HEADERS);
        final Credential body = Credential.builder()
                .setGrantType(GrantType.AUTHORIZATION_CODE.getName())
                .setClientId(6663702242922673L)
                .setClientSecret("uX4aBfrS9JriB921ay3UKA2lvXo9TY3I")
                .setCode("TG-655052b1d76fad000182dc0a-92932223")
                .setRedirectUri("https://webhook.site/3df7fcb9-3c7b-4c8d-b613-8797c0636281")
                .build();
        final String formBody = "test=test";
        final String response;

        Mockito.when(restTemplate.exchange(
                eq(URI),
                eq(HttpMethod.POST),
                eq(new HttpEntity<>(formBody, customHeaders)),
                eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.convertValue(any(Credential.class), eq(Map.class))).thenReturn(Map.of("test", "test"));
        // Act

        response = httpClient.post(URI, customHeaders, body);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("{}", response);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("Given put request with body, when there are no errors, then return ok.")
    void put(int index) throws JsonProcessingException {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(index);
        final HttpHeaders customHeaders = getCustomHeaders(headers);
        final Credential body = Credential.builder().build();
        final String jsonBody = "{}";
        Mockito.when(restTemplate.exchange(
                eq(URI),
                eq(HttpMethod.PUT),
                eq(new HttpEntity<>(jsonBody, customHeaders)),
                eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.writeValueAsString(any(Object.class))).thenReturn(jsonBody);
        // Act
        final String response = httpClient.put(URI, headers, body);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("{}", response);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("Given delete request with body, when there are no errors, then return ok.")
    void delete(int index) throws JsonProcessingException {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(index);
        final HttpHeaders customHeaders = getCustomHeaders(headers);
        final Credential body = Credential.builder()
                .setGrantType(GrantType.AUTHORIZATION_CODE.getName())
                .build();
        final String jsonBody = "{}";
        Mockito.when(restTemplate.exchange(
                eq(URI),
                eq(HttpMethod.DELETE),
                eq(new HttpEntity<>(jsonBody, customHeaders)),
                eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.convertValue(eq(body), anyMap()).thenReturn(jsonBody);
        // Act
        final String response = httpClient.delete(URI, headers, body);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("{}", response);
    }

    private HttpHeaders getCustomHeaders(Map<String, List<String>> customHeaders) {
        HttpHeaders headers = new HttpHeaders();
        if (!customHeaders.containsKey("Accept")) {
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        }
        if (!customHeaders.containsKey("Content-Type")) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        headers.addAll(new MultiValueMapAdapter<>(customHeaders));
        return headers;
    }

    private List<Map<String, List<String>>> allHeaders() {
        return List.of(DEFAULT_HEADERS, CUSTOM_HEADERS);
    }
}