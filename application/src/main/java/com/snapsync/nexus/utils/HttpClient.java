package com.snapsync.nexus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class HttpClient<T> {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public HttpClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public T get(String url, Map<String, List<String>> customHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(new MultiValueMapAdapter<>(customHeaders));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        final String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        try {
            return objectMapper.readValue(response, new TypeReference<T>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public T post(String url, Map<String, List<String>> customHeaders, String body) {
        return executeRequestWithBody(HttpMethod.POST, url, customHeaders, body);
    }

    public T put(String url, Map<String, List<String>> customHeaders, String body) {
        return executeRequestWithBody(HttpMethod.PUT, url, customHeaders, body);
    }

    public T delete(String url, Map<String, List<String>> customHeaders, String body) {
        return executeRequestWithBody(HttpMethod.DELETE, url, customHeaders, body);
    }

    private T executeRequestWithBody(HttpMethod method, String url, Map<String, List<String>> customHeaders, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(new MultiValueMapAdapter<>(customHeaders));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        final String response = restTemplate.exchange(url, method, entity, String.class).getBody();
        try {
            return objectMapper.readValue(response, new TypeReference<T>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
