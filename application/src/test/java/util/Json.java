package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class Json {
    static ObjectMapper objectMapper = new ObjectMapper();
    public static String parse(String jsonFileName){
        File resource = null;
        try {
            resource = new ClassPathResource(
                    "json/" + jsonFileName).getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(resource.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            return objectMapper.writeValueAsString(objectMapper.readValue(data, Object.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
