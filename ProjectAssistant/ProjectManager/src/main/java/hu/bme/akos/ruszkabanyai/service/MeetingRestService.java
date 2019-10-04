package hu.bme.akos.ruszkabanyai.service;

import hu.bme.akos.ruszkabanyai.dao.interfaces.MeetingRepository;
import hu.bme.akos.ruszkabanyai.dao.interfaces.ProjectRepository;
import hu.bme.akos.ruszkabanyai.dao.interfaces.UserRepository;
import hu.bme.akos.ruszkabanyai.dto.MeetingDTO;
import hu.bme.akos.ruszkabanyai.entity.Meeting;
import hu.bme.akos.ruszkabanyai.entity.Project;
import hu.bme.akos.ruszkabanyai.entity.helper.EntityMapper;
import hu.bme.akos.ruszkabanyai.security.IAuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("meeting")
@PreAuthorize("hasRole('USER')")
@Slf4j
public class MeetingRestService {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("all")
    public ResponseEntity getAllMeetingForUser() {
        List<MeetingDTO> meetings = meetingRepository
                .findAllByAttendeeList_Email(authenticationFacade.getUserName())
                .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList());
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("{meetingName}")
    public ResponseEntity getMeetingByName(@PathVariable String meetingName) {
        Optional<Meeting> optionalMeeting = meetingRepository.findByName(meetingName);
        if (optionalMeeting.isPresent()) {
            Meeting meeting = optionalMeeting.get();
            return meeting.getAttendeeList().stream().anyMatch(u -> u.getEmail().equals(authenticationFacade.getUserName())) ?
                    ResponseEntity.ok(meeting.entityToDTO()) : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("project/{projectName}")
    @PreAuthorize("@securityService.isInsider(#projectName)")
    public ResponseEntity getAllMeetingForProject(@PathVariable String projectName) {
        Optional<Project> project = projectRepository.findByName(projectName);
        if (project.isPresent()) {
            return ResponseEntity.ok(project.get().getMeetingList().stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /* új meeting meghirdetése */
    @PostMapping
    public ResponseEntity newMeeting(@RequestBody @Valid MeetingDTO dto) {
        if (meetingRepository.findByName(dto.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Meeting meeting = Meeting.dtoToEntity(dto);
        meetingRepository.save(meeting);
        return null;
    }

    /* meeting szerkesztése */
    public ResponseEntity updateMeeting() {
        return null;
    }
    /* meeting törlése */
}
