package com.snapsync.nexus.entrypoint;

import com.snapsync.nexus.repository.GetPaymentRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingEntrypoint {
    private static final String PATH = "/ping";

    private final GetPaymentRestRepository getPaymentRepository;

    @Autowired
    public PingEntrypoint(GetPaymentRestRepository getPaymentRepository) {
        this.getPaymentRepository = getPaymentRepository;
    }

    @PostMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> execute(@RequestBody String body) {
        System.out.println(body);
        getPaymentRepository.execute(123456L);
        return ResponseEntity.ok("PONG!");
    }
}
