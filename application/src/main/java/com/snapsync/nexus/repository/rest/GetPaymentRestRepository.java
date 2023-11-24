package com.snapsync.nexus.repository.rest;

import com.snapsync.nexus.entity.auth.Authorization;
import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.entity.payments.Payment;
import com.snapsync.nexus.interfaces.GenerateAuthorizationRepository;
import com.snapsync.nexus.interfaces.GetPaymentRepository;
import com.snapsync.nexus.repository.rest.dto.AuthorizationDTO;
import com.snapsync.nexus.repository.rest.dto.payments.PaymentDTO;
import com.snapsync.nexus.utils.HttpClient;
import com.snapsync.nexus.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class GetPaymentRestRepository implements GetPaymentRepository {

    private final HttpClient httpClient;

    @Value("${endpoints.base-url-ml}")
    private String urlBase;

    @Value("${endpoints.get.payment}")
    private String path;

    @Autowired
    public GetPaymentRestRepository(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    @Override
    public Payment execute(Long id) {
        return httpClient.jsonToModel(httpClient.get(Util.getEndpoint(urlBase, String.format(path, id))), PaymentDTO.class).map();
    }
}
