package hu.bme.akosruszka.projectmanager.service.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    @GetMapping("/getOAuthClients")
    public ResponseEntity<Map<String, String>> getAuthentication() {

        ResponseEntity<Map> response = new RestTemplate()
                .getForEntity("http://localhost:9200/getRegisteredClients", Map.class);

        HashMap<String, String> body = (HashMap<String, String>) response.getBody();

        return ResponseEntity.ok(body);
    }
}
