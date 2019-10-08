package hu.bme.akos.ruszkabanyai.service;

import hu.bme.akos.ruszkabanyai.dao.MeetingRepository;
import hu.bme.akos.ruszkabanyai.dao.MinuteRepository;
import hu.bme.akos.ruszkabanyai.dao.ProjectRepository;
import hu.bme.akos.ruszkabanyai.dao.UserRepository;
import hu.bme.akos.ruszkabanyai.dto.MeetingDTO;
import hu.bme.akos.ruszkabanyai.dto.MinutesDTO;
import hu.bme.akos.ruszkabanyai.entity.Meeting;
import hu.bme.akos.ruszkabanyai.entity.Minutes;
import hu.bme.akos.ruszkabanyai.entity.Project;
import hu.bme.akos.ruszkabanyai.entity.User;
import hu.bme.akos.ruszkabanyai.entity.helper.EntityMapper;
import hu.bme.akos.ruszkabanyai.helper.NotFoundEntityException;
import hu.bme.akos.ruszkabanyai.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("meeting")
@PreAuthorize("hasRole('USER')")
public class MeetingRestService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private IAuthenticationFacade authentication;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MinuteRepository minuteRepository;

    @GetMapping
    public ResponseEntity getMeetings() {
        return ResponseEntity.ok(meetingRepository.findAllByNameIn(new ArrayList<>(getUser().getMeetingNameSet())).stream()
                .map(EntityMapper::entityToDTO).collect(Collectors.toList()));
    }

    @GetMapping("{meetingName}")
    public ResponseEntity getMeeting(@PathVariable String meetingName) {
        return meetingRepository.findByName(meetingName)
                .map(t -> ResponseEntity.ok(t.entityToDTO())).orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PostMapping
    @PreAuthorize("@securityService.isInsider(#dto.projectName)")
    public ResponseEntity newMeeting(@RequestBody MeetingDTO dto) {
        if (meetingRepository.findByName(dto.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        try {
            Project project = projectRepository.findByName(dto.getProjectName()).orElseThrow(() -> new NotFoundEntityException("Nem létezik a projekt"));

            Meeting meeting = Meeting.builder().name(dto.getName()).description(dto.getDescription())
                    .projectName(dto.getProjectName()).location(dto.getLocation()).date(dto.getDate())
                    .minuteName(dto.getMinuteName()).chairPersonEmail(dto.getChairPersonEmail()).build();
            project.getMeetingNameSet().add(meeting.getName());
            List<User> users = userRepository.findAllByEmailIn(new ArrayList<>(dto.getAttendeeEmailSet()));
            users.forEach(u -> {
                u.getMeetingNameSet().add(meeting.getName());
                userRepository.save(u);
            });
            projectRepository.save(project);
            meetingRepository.save(meeting);
            return ResponseEntity.ok(meeting.entityToDTO());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @PutMapping("{meetingName}")
    @PreAuthorize("@securityService.isInsider(#dto.projectName)")
    public ResponseEntity updateMeeting(@PathVariable String meetingName, @RequestBody MeetingDTO dto) {
        try {
            Meeting meeting = meetingRepository.findByName(meetingName).orElseThrow(() -> new NotFoundEntityException("Nem található a meeting"));
            Project project;
            if (!meetingName.equals(dto.getName())) {
                if (meetingRepository.findByName(dto.getName()).isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
                project = projectRepository.findByName(meeting.getProjectName()).get();
                project.getMeetingNameSet().remove(meetingName);
                project.getMeetingNameSet().add(dto.getName());

                User newChairPerson = null;
                User oldChairPerson = userRepository.findByEmail(meeting.getChairPersonEmail()).get();
                oldChairPerson.getMeetingNameSet().remove(meetingName);
                if (!dto.getChairPersonEmail().equals(meeting.getChairPersonEmail())) {
                    newChairPerson = userRepository.findByEmail(dto.getChairPersonEmail()).orElseThrow(() ->
                            new NoSuchElementException("Nem található a felhasználó"));
                    newChairPerson.getMeetingNameSet().add(dto.getName());
                } else {
                    oldChairPerson.getMeetingNameSet().add(dto.getName());
                }

                Set<String> intersection = new HashSet<>(meeting.getAttendeeEmailSet());
                intersection.retainAll(dto.getAttendeeEmailSet());
                Set<String> removable = new HashSet<>(meeting.getAttendeeEmailSet());
                removable.removeAll(intersection);

                List<User> deletableUsers = userRepository.findAllByEmailIn(new ArrayList<>(removable));
                deletableUsers.forEach(u -> {
                    u.getMeetingNameSet().remove(meetingName);
                    userRepository.save(u);
                });
                List<User> users = userRepository.findAllByEmailIn(new ArrayList<>(dto.getAttendeeEmailSet()));
                users.forEach(u -> {
                    u.getMeetingNameSet().add(dto.getName());
                    userRepository.save(u);
                });
                projectRepository.save(project);

                if (newChairPerson != null) {
                    userRepository.save(newChairPerson);
                }
                userRepository.save(oldChairPerson);
            }
            meeting.setDescription(dto.getDescription());
            meeting.setLocation(dto.getLocation());
            meeting.setDate(dto.getDate());
            meetingRepository.save(meeting);
            return ResponseEntity.ok(meeting.entityToDTO());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @DeleteMapping("{meetingName}")
    public ResponseEntity deleteMeeting(@PathVariable String meetingName) {
        try {
            Meeting meeting = meetingRepository.findByName(meetingName).orElseThrow(() ->
                    new NotFoundEntityException("Nem található ilyen névvel meeting"));
            projectRepository.findByName(meeting.getProjectName()).ifPresent(p -> {
                p.getMeetingNameSet().remove(meetingName);
                projectRepository.save(p);
            });
            userRepository.findByEmail(meeting.getChairPersonEmail()).ifPresent(u -> {
                u.getMeetingNameSet().remove(meetingName);
                userRepository.save(u);
            });
            userRepository.findAllByEmailIn(new ArrayList<>(meeting.getAttendeeEmailSet())).forEach(u -> {
                u.getMeetingNameSet().remove(meetingName);
                userRepository.save(u);
            });
            minuteRepository.findAllByTitle(meeting.getMinuteName() == null ? "" : meeting.getMinuteName()).ifPresent(m -> minuteRepository.delete(m));
            meetingRepository.delete(meeting);
            return ResponseEntity.ok().build();
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @PostMapping("{meetingName}")
    @PreAuthorize("@securityService.isInsiderOnMeeting(#meetingName)")
    public ResponseEntity addMinute(@PathVariable String meetingName, @RequestBody MinutesDTO dto) {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Meeting meeting = meetingRepository.findByName(meetingName).get();
        Minutes minutes = Minutes.builder().title(dto.getTitle())
                .meetingName(meetingName).absentEmailSet(dto.getAbsentEmailSet())
                .taskSet(dto.getTaskSet()).build();
        meeting.setMinute(minutes);
        meetingRepository.save(meeting);
        return ResponseEntity.ok(meeting.entityToDTO());
    }

    @PostMapping("{meetingName}/{minuteName}")
    @PreAuthorize("@securityService.isInsiderOnMeeting(#meetingName)")
    public ResponseEntity updateMinute(@PathVariable("meetingName") String meetingName,
                                       @PathVariable("minuteName") String minuteName,
                                       @RequestBody MinutesDTO dto) {
        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private User getUser() {
        return userRepository.findByEmail(authentication.getUserName()).get();
    }
}
