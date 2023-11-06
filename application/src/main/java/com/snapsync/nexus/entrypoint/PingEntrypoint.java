package com.snapsync.nexus.entrypoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingEntrypoint {
    private static final String PATH = "/ping";
    @PostMapping(PATH)
    public ResponseEntity<String> execute(@RequestBody String body){
        System.out.println(body);
        return ResponseEntity.ok("Pong!");
    }
}
