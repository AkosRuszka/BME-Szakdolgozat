package hu.bme.akosruszka.authenticationmanager.service;

import hu.bme.akosruszka.authenticationmanager.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/getRegisteredClients")
public class RegisteredClientRestService {

    @Autowired
    private Environment env;

    @GetMapping
    public ResponseEntity<Map<String, String>> getRegisteredClients() {
        List<String> clients = SecurityConfig.clients;
        String clientPropertyKey = "spring.security.oauth2.client.registration.";
        Map<String, String> result = new HashMap<>();

        clients.forEach(client -> result.put(client, env.getProperty(clientPropertyKey + client + ".authorize-uri")));

        return ResponseEntity.ok(result);
    }
}
