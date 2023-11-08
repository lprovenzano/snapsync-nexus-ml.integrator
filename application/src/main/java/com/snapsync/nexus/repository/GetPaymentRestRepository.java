package com.snapsync.nexus.repository;

import com.snapsync.nexus.interfaces.GetPaymentRepository;
import com.snapsync.nexus.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetPaymentRestRepository implements GetPaymentRepository {
    private final HttpClient<String> httpClient;

    @Value("${endpoints.get.payment}")
    private String url;

    @Autowired
    public GetPaymentRestRepository(HttpClient<String> httpClient) {
        this.httpClient = httpClient;
    }

    public String execute(Long value) {
        final String endpoint = String.format(url, value);
        return httpClient.get(endpoint, Map.of());
    }
}
