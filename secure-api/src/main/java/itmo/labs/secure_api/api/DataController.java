package itmo.labs.secure_api.api;

import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/data", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

    @GetMapping
    public Map<String, Object> getData(Authentication auth) {
        return Map.of(
                "currentUser", auth.getName(),
                "items", List.of(Map.of("id", 1, "title", "Hello"), Map.of("id", 2, "title", "World")));
    }
}
