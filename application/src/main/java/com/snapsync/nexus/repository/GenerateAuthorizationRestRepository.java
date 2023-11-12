package com.snapsync.nexus.repository;

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

import java.util.List;
import java.util.Map;

@Repository
public class GenerateAuthorizationRestRepository implements GenerateAuthorizationRepository {

    private final HttpClient httpClient;

    @Value("${endpoints.base-url-ml}")
    private String urlBase;

    @Value("${endpoints.post.oauth-token}")
    private String path;

    @Autowired
    public GenerateAuthorizationRestRepository(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Authorization execute(Credential credential) {
        final String response = httpClient.post(Util.getEndpoint(urlBase, path),
                Map.of("Content-Type", List.of(MediaType.APPLICATION_FORM_URLENCODED_VALUE)),
                credential);
        return httpClient.jsonToModel(response, AuthorizationDTO.class).map();
    }
}
