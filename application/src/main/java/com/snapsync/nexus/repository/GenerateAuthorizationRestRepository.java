package com.snapsync.nexus.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapsync.nexus.entity.auth.Authorization;
import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.interfaces.GenerateAuthorizationRepository;
import com.snapsync.nexus.repository.dto.AuthorizationDTO;
import com.snapsync.nexus.utils.HttpClient;
import com.snapsync.nexus.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

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
        final AuthorizationDTO response =
                httpClient.setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .post(Util.getEndpoint(urlBase, path), credential);
        return response.map();

    }
}
