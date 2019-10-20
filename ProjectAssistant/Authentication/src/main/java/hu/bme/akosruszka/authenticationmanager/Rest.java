package hu.bme.akosruszka.authenticationmanager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Rest {

    @GetMapping
    public String get() {
        return "ok";
    }
}
