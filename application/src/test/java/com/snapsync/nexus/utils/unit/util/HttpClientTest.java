package com.snapsync.nexus.utils.unit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapsync.nexus.exception.*;
import com.snapsync.nexus.utils.HttpClient;
import org.junit.jupiter.api.Assertions;
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

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class HttpClientTest {

    @InjectMocks
    private HttpClient<String> httpClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private static final String URI = "/test/dev";
    private static final String ERROR_URI = "/test/dev/error";

    private static final Map<String, List<String>> CUSTOM_HEADERS = Map.of("custom-header-key", List.of("custom-header-value"));
    private static final Map<String, List<String>> EMPTY_HEADERS = Map.of();

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("Given get request, when there are no errors, then return ok.")
    void get(int index) throws JsonProcessingException {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(index);
        final HttpHeaders customHeaders = getCustomHeaders(headers);

        Mockito.when(restTemplate.exchange(
                Mockito.eq(URI),
                Mockito.eq(HttpMethod.GET),
                Mockito.eq(new HttpEntity<>(customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn("{}");

        // Act
        final String response = httpClient.get(URI, headers);

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
        final String jsonBody = "{\"id\": 1}";
        Mockito.when(restTemplate.exchange(
                Mockito.eq(URI),
                Mockito.eq(HttpMethod.POST),
                Mockito.eq(new HttpEntity<>(jsonBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn("{}");

        // Act
        final String response = httpClient.post(URI, headers, jsonBody);

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
        final String jsonBody = "{\"id\": 1, \"data\": \"test\"}";
        Mockito.when(restTemplate.exchange(
                Mockito.eq(URI),
                Mockito.eq(HttpMethod.PUT),
                Mockito.eq(new HttpEntity<>(jsonBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn("{}");

        // Act
        final String response = httpClient.put(URI, headers, jsonBody);

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
        final String jsonBody = "{\"id\": 1}";
        Mockito.when(restTemplate.exchange(
                Mockito.eq(URI),
                Mockito.eq(HttpMethod.DELETE),
                Mockito.eq(new HttpEntity<>(jsonBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn("{}");

        // Act
        final String response = httpClient.delete(URI, headers, jsonBody);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("{}", response);
    }

    @Test
    @DisplayName("Given get request, when outage happened, then return internal server error (500).")
    void get_error() {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(0);
        final HttpHeaders customHeaders = getCustomHeaders(headers);

        Mockito.when(restTemplate.exchange(
                Mockito.eq(ERROR_URI),
                Mockito.eq(HttpMethod.GET),
                Mockito.eq(new HttpEntity<>(customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        Assertions.assertThrows(InternalServerErrorException.class, () -> httpClient.get(ERROR_URI, headers));
        Mockito.verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("Given get request, when conflict happened, then return conflict (409).")
    void get_error_2() {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(0);
        final HttpHeaders customHeaders = getCustomHeaders(headers);

        Mockito.when(restTemplate.exchange(
                Mockito.eq(ERROR_URI),
                Mockito.eq(HttpMethod.GET),
                Mockito.eq(new HttpEntity<>(customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>(HttpStatus.CONFLICT));

        // Act & Assert
        Assertions.assertThrows(ConflictException.class, () -> httpClient.get(ERROR_URI, headers));
        Mockito.verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("Given get request, when authorization happened, then return unauthorized (401).")
    void get_error_3() {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(0);
        final HttpHeaders customHeaders = getCustomHeaders(headers);

        Mockito.when(restTemplate.exchange(
                Mockito.eq(ERROR_URI),
                Mockito.eq(HttpMethod.GET),
                Mockito.eq(new HttpEntity<>(customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));

        // Act & Assert
        Assertions.assertThrows(UnauthorizedException.class, () -> httpClient.get(ERROR_URI, headers));
        Mockito.verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("Given put request, when resource not exists, then return not found (404).")
    void put_error() {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(0);
        final HttpHeaders customHeaders = getCustomHeaders(headers);
        final String jsonBody = "{\"id\": 1, \"data\": \"test\"}";
        Mockito.when(restTemplate.exchange(
                Mockito.eq(ERROR_URI),
                Mockito.eq(HttpMethod.PUT),
                Mockito.eq(new HttpEntity<>(jsonBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // Act & Assert
        Assertions.assertThrows(NotFoundException.class, () -> httpClient.put(ERROR_URI, headers, jsonBody));
        Mockito.verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("Given put request, when resource target has malformed id, then return bad request (400).")
    void put_error_2() {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(0);
        final HttpHeaders customHeaders = getCustomHeaders(headers);
        final String jsonBody = "{\"id\": 1, \"data\": \"test\"}";
        Mockito.when(restTemplate.exchange(
                Mockito.eq(ERROR_URI),
                Mockito.eq(HttpMethod.PUT),
                Mockito.eq(new HttpEntity<>(jsonBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Act & Assert
        Assertions.assertThrows(BadRequestException.class, () -> httpClient.put(ERROR_URI, headers, jsonBody));
        Mockito.verifyNoInteractions(objectMapper);
    }

    private HttpHeaders getCustomHeaders(Map<String, List<String>> customHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(new MultiValueMapAdapter<>(customHeaders));
        return headers;
    }

    private List<Map<String, List<String>>> allHeaders() {
        return List.of(EMPTY_HEADERS, CUSTOM_HEADERS);
    }
}