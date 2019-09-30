package hu.bme.akos.ruszkabanyai.service;

import hu.bme.akos.ruszkabanyai.dto.ProjectDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MeetingRestService {

    @GetMapping
    public ProjectDTO get() {
        return ProjectDTO.builder().name("sanyi").description("leírás").build();
    }
}
