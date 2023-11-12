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
public class HttpClient<T> {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final Map<String, List<String>> customHeaders = new HashMap<>();

    @Autowired
    public HttpClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public T get(String url) {
        HttpEntity<String> entity = new HttpEntity<>(buildHeaders());
        final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        final String bodyResponse = response.getBody();
        try {
            return objectMapper.readValue(bodyResponse, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public T post(String url, Object body) {
        return executeRequestWithBody(HttpMethod.POST, url, body);
    }

    public T put(String url, Object body) {
        return executeRequestWithBody(HttpMethod.PUT, url, body);
    }

    public T delete(String url, Object body) {
        return executeRequestWithBody(HttpMethod.DELETE, url, body);
    }

    public HttpClient<T> setHeader(String key, String value) {
        this.customHeaders.put(key, List.of(value));
        return this;
    }

    private T executeRequestWithBody(HttpMethod method, String url, Object body) {
        final HttpHeaders headers = buildHeaders();
        final String stringBody = parseBodyByContentType(body, headers);
        HttpEntity<String> entity = new HttpEntity<>(stringBody, headers);
        final ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
        final String bodyResponse = response.getBody();
        try {
            return objectMapper.readValue(bodyResponse, new TypeReference<T>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (!this.customHeaders.containsKey("Content-Type")) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        headers.addAll(new MultiValueMapAdapter<>(this.customHeaders));
        return headers;
    }
}
