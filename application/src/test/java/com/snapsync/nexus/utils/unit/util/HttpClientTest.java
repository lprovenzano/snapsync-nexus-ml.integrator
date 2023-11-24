package com.snapsync.nexus.utils.unit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.entity.auth.GrantType;
import com.snapsync.nexus.utils.HttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Objects;

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
    @DisplayName("Given psot request with body, when there are no errors, then return ok.")
    void post(int index) throws JsonProcessingException {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(index);
        final HttpHeaders customHeaders = getCustomHeaders(headers);
        final Credential body = Credential.builder()
                .setGrantType(GrantType.AUTHORIZATION_CODE.getName())
                .build();
        final String jsonBody = "{}";
        final String urlEncodedBody = "test=test";
        final boolean isNotJsonEncoded = Objects.requireNonNull(customHeaders.getContentType()).equalsTypeAndSubtype(MediaType.APPLICATION_FORM_URLENCODED);
        final String response;
        // Act
        if (isNotJsonEncoded) {
            Mockito.when(restTemplate.exchange(
                    eq(URI),
                    eq(HttpMethod.POST),
                    eq(new HttpEntity<>(urlEncodedBody, customHeaders)),
                    eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
            Mockito.when(objectMapper.convertValue(eq(urlEncodedBody), eq(Map.class))).thenReturn(Map.of("test", "test"));
            response = httpClient.post(URI, headers, urlEncodedBody);

        } else {
            Mockito.when(restTemplate.exchange(
                    eq(URI),
                    eq(HttpMethod.POST),
                    eq(new HttpEntity<>(jsonBody, customHeaders)),
                    eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
            Mockito.when(objectMapper.writeValueAsString(any(Object.class))).thenReturn(jsonBody);
            response = httpClient.post(URI, headers, body);
        }

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
        final Credential body = Credential.builder()
                .setGrantType(GrantType.AUTHORIZATION_CODE.getName())
                .build();
        final String jsonBody = "{}";
        final String urlEncodedBody = "test=test";
        final boolean isNotJsonEncoded = Objects.requireNonNull(customHeaders.getContentType()).equalsTypeAndSubtype(MediaType.APPLICATION_FORM_URLENCODED);
        final String response;
        // Act
        if (isNotJsonEncoded) {
            Mockito.when(restTemplate.exchange(
                    eq(URI),
                    eq(HttpMethod.PUT),
                    eq(new HttpEntity<>(urlEncodedBody, customHeaders)),
                    eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
            Mockito.when(objectMapper.convertValue(eq(urlEncodedBody), eq(Map.class))).thenReturn(Map.of("test", "test"));
            response = httpClient.put(URI, headers, urlEncodedBody);

        } else {
            Mockito.when(restTemplate.exchange(
                    eq(URI),
                    eq(HttpMethod.PUT),
                    eq(new HttpEntity<>(jsonBody, customHeaders)),
                    eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
            Mockito.when(objectMapper.writeValueAsString(any(Object.class))).thenReturn(jsonBody);
            response = httpClient.put(URI, headers, body);
        }

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
        final String urlEncodedBody = "test=test";
        final boolean isNotJsonEncoded = Objects.requireNonNull(customHeaders.getContentType()).equalsTypeAndSubtype(MediaType.APPLICATION_FORM_URLENCODED);
        final String response;
        // Act
        if (isNotJsonEncoded) {
            Mockito.when(restTemplate.exchange(
                    eq(URI),
                    eq(HttpMethod.DELETE),
                    eq(new HttpEntity<>(urlEncodedBody, customHeaders)),
                    eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
            Mockito.when(objectMapper.convertValue(eq(urlEncodedBody), eq(Map.class))).thenReturn(Map.of("test", "test"));
            response = httpClient.delete(URI, headers, urlEncodedBody);

        } else {
            Mockito.when(restTemplate.exchange(
                    eq(URI),
                    eq(HttpMethod.DELETE),
                    eq(new HttpEntity<>(jsonBody, customHeaders)),
                    eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
            Mockito.when(objectMapper.writeValueAsString(any(Object.class))).thenReturn(jsonBody);
            response = httpClient.delete(URI, headers, body);
        }

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