package hu.bme.akosruszka.projectmanager.service.rest;

import hu.bme.akosruszka.projectmanager.dao.UserRepository;
import hu.bme.akosruszka.projectmanager.dto.UserDTO;
import hu.bme.akosruszka.projectmanager.entity.helper.EntityMapper;
import hu.bme.akosruszka.projectmanager.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
@PreAuthorize("hasRole('USER')")
public class UserRestService {

    @Autowired
    private IAuthenticationFacade authentication;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<UserDTO> getUserInfo() {
        return userRepository.findByEmail(authentication.getUserName())
                .map(u -> ResponseEntity.ok(u.entityToDTO())).orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping("/getUsersInfo")
    public ResponseEntity<List<UserDTO>> getOtherUsers(@RequestBody List<String> userEmails) {
        List<UserDTO> result = userRepository.findAllByEmailIn(userEmails).stream()
                .map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkUser")
    public ResponseEntity checkUsers(@PathParam(value = "email") String email) {
        return ResponseEntity.ok(userRepository.existsByEmail(email));
    }
}
