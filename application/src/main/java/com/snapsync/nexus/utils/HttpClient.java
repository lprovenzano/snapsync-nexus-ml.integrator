package com.snapsync.nexus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static java.util.stream.Collectors.joining;

@Component
public class HttpClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public HttpClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String get(String url, Map<String, List<String>> customHeaders) {
        HttpEntity<String> entity = new HttpEntity<>(buildHeaders(customHeaders));
        final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public String post(String url, Map<String, List<String>> headers, Object body) {
        return executeRequestWithBody(HttpMethod.POST, url, body, headers);
    }

    public String put(String url, Map<String, List<String>> headers, Object body) {
        return executeRequestWithBody(HttpMethod.PUT, url, body, headers);
    }

    public String delete(String url, Map<String, List<String>> headers, Object body) {
        return executeRequestWithBody(HttpMethod.DELETE, url, body, headers);
    }

    private String executeRequestWithBody(HttpMethod method, String url, Object body, Map<String, List<String>> customHeaders) {
        final HttpHeaders headers = buildHeaders(customHeaders);
        final String stringBody = parseBodyByContentType(body, headers);
        HttpEntity<String> entity = new HttpEntity<>(stringBody, headers);
        final ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
        return response.getBody();
    }

    private String parseBodyByContentType(Object body, HttpHeaders headers) {
        String stringBody = "";
        if (Objects.requireNonNull(headers.getContentType())
                .equalsTypeAndSubtype(MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))) {
            final Map<String, Object> bodyMap = objectMapper.convertValue(body, Map.class);
            stringBody = bodyMap.entrySet()
                    .stream()
                    .map(Object::toString)
                    .collect(joining("&"));
        } else {
            try {
                stringBody = objectMapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return stringBody;
    }

    private HttpHeaders buildHeaders(Map<String, List<String>> customHeaders) {
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

    public <T> T jsonToModel(String json, Class<T> type) {
        try {
            return new ObjectMapper().readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
