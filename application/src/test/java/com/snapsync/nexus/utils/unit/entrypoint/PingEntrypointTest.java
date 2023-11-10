package com.snapsync.nexus.utils.unit.entrypoint;

import com.snapsync.nexus.entrypoint.PingEntrypoint;
import com.snapsync.nexus.utils.unit.util.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
class PingEntrypointTest {

    @InjectMocks
    PingEntrypoint pingEntrypoint;

    @Test
    void given_valid_request_when_body_is_not_null_then_response_ok() {
        final String randomData = Json.parse("random-mock.json");

        final ResponseEntity<String> result = pingEntrypoint.execute(randomData);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals("PONG!", result.getBody());
    }
}