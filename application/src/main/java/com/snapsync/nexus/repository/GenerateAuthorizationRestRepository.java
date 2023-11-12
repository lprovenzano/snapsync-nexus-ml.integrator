package com.snapsync.nexus.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapsync.nexus.entity.auth.Authorization;
import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.interfaces.GenerateAuthorizationRepository;
import com.snapsync.nexus.repository.dto.AuthorizationDTO;
import com.snapsync.nexus.utils.HttpClient;
import com.snapsync.nexus.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Collections;

@Repository
public class GenerateAuthorizationRestRepository implements GenerateAuthorizationRepository {

    private final HttpClient<AuthorizationDTO> httpClient;
    private final ObjectMapper mapper;

    @Value("${endpoints.base-url-ml}")
    private String urlBase;

    @Value("${endpoints.post.oauth-token}")
    private String path;

    @Autowired
    public GenerateAuthorizationRestRepository(HttpClient<AuthorizationDTO> httpClient,
                                               ObjectMapper mapper) {
        this.httpClient = httpClient;
        this.mapper = mapper;
    }

    @Override
    public Authorization execute(Credential credential) {
        try {
            final AuthorizationDTO response = httpClient.post(Util.getEndpoint(urlBase, path),
                    Collections.emptyMap(),
                    mapper.writeValueAsString(credential));
            return response.map();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
