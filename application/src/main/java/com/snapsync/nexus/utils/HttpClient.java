package com.snapsync.nexus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapsync.nexus.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
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
        HttpEntity<String> entity = new HttpEntity<>(buildHeaders(customHeaders));
        final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        handleError(response.getStatusCode());
        final String bodyResponse = response.getBody();
        try {
            return objectMapper.readValue(bodyResponse, new TypeReference<T>() {
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
        HttpEntity<String> entity = new HttpEntity<>(body, buildHeaders(customHeaders));
        final ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
        handleError(response.getStatusCode());
        final String bodyResponse = response.getBody();
        try {
            return objectMapper.readValue(bodyResponse, new TypeReference<T>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleError(HttpStatusCode httpStatusCode) {
        if (httpStatusCode.isError()) {
            switch (httpStatusCode) {
                case HttpStatus.CONFLICT -> throw new ConflictException();
                case HttpStatus.BAD_REQUEST -> throw new BadRequestException();
                case HttpStatus.NOT_FOUND -> throw new NotFoundException();
                case HttpStatus.INTERNAL_SERVER_ERROR -> throw new InternalServerErrorException();
                case HttpStatus.UNAUTHORIZED -> throw new UnauthorizedException();
                default -> throw new RuntimeException("Unexpected status code: " + httpStatusCode);
            }
        }
    }

    private HttpHeaders buildHeaders(Map<String, List<String>> customHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(new MultiValueMapAdapter<>(customHeaders));
        return headers;
    }
}
