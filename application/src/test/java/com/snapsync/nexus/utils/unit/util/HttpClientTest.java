package com.snapsync.nexus.utils.unit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapsync.nexus.entity.auth.Credential;
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
        final String response;

        Mockito.when(restTemplate.exchange(
                Mockito.eq(URI),
                Mockito.eq(HttpMethod.GET),
                Mockito.eq(new HttpEntity<>(customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn("{}");

        // Act
        if (headers.isEmpty()) {
            response = httpClient.get(URI);
        } else {
            response = httpClient
                    .setHeader("custom-header-key", "custom-header-value")
                    .get(URI);
        }

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
        final String response;

        Mockito.when(restTemplate.exchange(
                Mockito.eq(URI),
                Mockito.eq(HttpMethod.POST),
                Mockito.eq(new HttpEntity<>(jsonBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn("{}");
        Mockito.when(objectMapper.writeValueAsString(any(Object.class))).thenReturn(jsonBody);
        // Act
        if (headers.isEmpty()) {
            response = httpClient.post(URI, body);
        } else {
            response = httpClient
                    .setHeader("custom-header-key", "custom-header-value")
                    .post(URI, body);
        }

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("{}", response);
    }

    @Test
    @DisplayName("Given post request with body, when the encoding is APPLICATION_FORM_URLENCODED_VALUE, then return ok.")
    void post2() throws JsonProcessingException {
        // Arrange
        HttpHeaders customHeaders = getCustomHeaders(EMPTY_HEADERS);
        customHeaders.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        final Credential body = Credential.builder()
                .setClientId(6663702242922673L)
                .setClientSecret("uX4aBfrS9JriB921ay3UKA2lvXo9TY3I")
                .setCode("TG-655052b1d76fad000182dc0a-92932223")
                .setRedirectUri("https://webhook.site/3df7fcb9-3c7b-4c8d-b613-8797c0636281")
                .build();
        final String formBody = "test=test";
        final String response;

        Mockito.when(restTemplate.exchange(
                Mockito.eq(URI),
                Mockito.eq(HttpMethod.POST),
                Mockito.eq(new HttpEntity<>(formBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn("{}");
        Mockito.when(objectMapper.convertValue(any(Credential.class), Mockito.eq(Map.class))).thenReturn(Map.of("test", "test"));
        // Act

        response = httpClient
                .setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .post(URI, body);

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
        final String response;
        Mockito.when(restTemplate.exchange(
                Mockito.eq(URI),
                Mockito.eq(HttpMethod.PUT),
                Mockito.eq(new HttpEntity<>(jsonBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn("{}");
        Mockito.when(objectMapper.writeValueAsString(any(Object.class))).thenReturn(jsonBody);
        // Act
        if (headers.isEmpty()) {
            response = httpClient.put(URI, body);
        } else {
            response = httpClient
                    .setHeader("custom-header-key", "custom-header-value")
                    .put(URI, body);
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
        final Credential body = Credential.builder().build();
        final String jsonBody = "{}";
        final String response;
        Mockito.when(restTemplate.exchange(
                Mockito.eq(URI),
                Mockito.eq(HttpMethod.DELETE),
                Mockito.eq(new HttpEntity<>(jsonBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn("{}");
        Mockito.when(objectMapper.writeValueAsString(any(Object.class))).thenReturn(jsonBody);
        // Act
        if (headers.isEmpty()) {
            response = httpClient.delete(URI, body);
        } else {
            response = httpClient
                    .setHeader("custom-header-key", "custom-header-value")
                    .delete(URI, body);
        }

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("{}", response);
    }

    @Test
    @DisplayName("Given any request, when deserialization fails, then return runtime exception.")
    void put_error_3() throws JsonProcessingException {
        // Arrange
        final Map<String, List<String>> headers = allHeaders().get(0);
        final HttpHeaders customHeaders = getCustomHeaders(headers);
        final Credential jsonBody = Credential.builder().build();
        Mockito.when(restTemplate.exchange(
                Mockito.eq(ERROR_URI),
                Mockito.eq(HttpMethod.PUT),
                Mockito.eq(new HttpEntity<>(jsonBody, customHeaders)),
                Mockito.eq(String.class))).thenReturn(new ResponseEntity<>("This is not a JSON", HttpStatus.OK));

        Mockito.when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenThrow(JsonProcessingException.class);

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> httpClient.put(ERROR_URI, jsonBody));
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