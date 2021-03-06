package hu.bme.akosruszka.projectmanager.service.rest;

import hu.bme.akosruszka.projectmanager.constans.StringConstants;
import hu.bme.akosruszka.projectmanager.dao.MeetingRepository;
import hu.bme.akosruszka.projectmanager.dao.MinuteRepository;
import hu.bme.akosruszka.projectmanager.dao.ProjectRepository;
import hu.bme.akosruszka.projectmanager.dao.UserRepository;
import hu.bme.akosruszka.projectmanager.dto.MeetingDTO;
import hu.bme.akosruszka.projectmanager.dto.MinutesDTO;
import hu.bme.akosruszka.projectmanager.entity.Meeting;
import hu.bme.akosruszka.projectmanager.entity.Minutes;
import hu.bme.akosruszka.projectmanager.entity.Project;
import hu.bme.akosruszka.projectmanager.entity.User;
import hu.bme.akosruszka.projectmanager.entity.helper.EntityMapper;
import hu.bme.akosruszka.projectmanager.helper.NotFoundEntityException;
import hu.bme.akosruszka.projectmanager.security.IAuthenticationFacade;
import hu.bme.akosruszka.projectmanager.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("meeting")
@PreAuthorize("hasRole('USER')")
public class MeetingRestService {

    private final MeetingRepository meetingRepository;

    private final IAuthenticationFacade authentication;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final MinuteRepository minuteRepository;

    private final MessageService messageService;

    public MeetingRestService(MeetingRepository meetingRepository, IAuthenticationFacade authentication,
                              UserRepository userRepository, ProjectRepository projectRepository,
                              MinuteRepository minuteRepository, MessageService messageService) {
        this.meetingRepository = meetingRepository;
        this.authentication = authentication;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.minuteRepository = minuteRepository;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity getMeetings() {
        return ResponseEntity.ok(meetingRepository.findAllByNameIn(new ArrayList<>(getUser().getMeetingNameSet())).stream()
                .map(EntityMapper::entityToDTO).collect(Collectors.toList()));
    }

    @GetMapping("project/{projectName}")
    @PreAuthorize("@securityService.isInsider(#projectName)")
    public ResponseEntity getMeetingsFromProject(@PathVariable String projectName) {
        List<String> meetingNames = projectRepository.findByName(projectName)
                .map(m -> new ArrayList<>(m.getMeetingNameSet())).orElse(new ArrayList<>());

        List<MeetingDTO> meetings = meetingRepository.findAllByNameIn(meetingNames).stream()
                .map(Meeting::entityToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("{meetingName}")
    @PreAuthorize("@securityService.isInsiderOnMeeting(#meetingName)")
    public ResponseEntity getMeeting(@PathVariable String meetingName) {
        return meetingRepository.findByName(meetingName)
                .map(t -> ResponseEntity.ok(t.entityToDTO())).orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping("check")
    public ResponseEntity checkMeeting(@PathParam(value = "name") String meetingName) {
        return ResponseEntity.ok(meetingRepository.findByName(meetingName).isPresent());
    }

    @PostMapping
    @PreAuthorize("@securityService.isInsider(#dto.projectName)")
    public ResponseEntity newMeeting(@RequestBody MeetingDTO dto) {
        if (meetingRepository.findByName(dto.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        try {
            Project project = projectRepository.findByName(dto.getProjectName()).orElseThrow(
                    () -> new NotFoundEntityException(StringConstants.PROJECT_NOT_FOUND));

            Meeting meeting = Meeting.builder().name(dto.getName()).description(dto.getDescription())
                    .projectName(dto.getProjectName()).location(dto.getLocation()).date(dto.getDate())
                    .minuteName(dto.getMinuteName()).build();
            project.getMeetingNameSet().add(meeting.getName());

            User chairPerson = getUser();
            meeting.setChairPerson(chairPerson);
            userRepository.save(chairPerson);

            List<User> users = userRepository.findAllByEmailIn(new ArrayList<>(dto.getAttendeeEmailSet()));
            users.forEach(u -> {
                u.getMeetingNameSet().add(meeting.getName());
                userRepository.save(u);
                meeting.getAttendeeEmailSet().add(u.getEmail());
            });
            projectRepository.save(project);
            meetingRepository.save(meeting);

            return messageService.publish(new ArrayList<>(meeting.getEmailsNotification()),
                    StringConstants.MEETING_NEW, initializeModel(meeting)).orElse(ResponseEntity.ok(meeting.entityToDTO()));
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @PutMapping("{meetingName}")
    @PreAuthorize("@securityService.isMeetingOwner(#dto.projectName)")
    public ResponseEntity updateMeeting(@PathVariable String meetingName, @RequestBody MeetingDTO dto) {
        try {
            Meeting meeting = meetingRepository.findByName(meetingName).orElseThrow(
                    () -> new NotFoundEntityException(StringConstants.MEETING_NOT_FOUND));
            Project project;
            Set<String> removable = new HashSet<>();
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
                            new NoSuchElementException(StringConstants.USER_NOT_FOUND));
                    newChairPerson.getMeetingNameSet().add(dto.getName());
                } else {
                    oldChairPerson.getMeetingNameSet().add(dto.getName());
                }

                Set<String> intersection = new HashSet<>(meeting.getAttendeeEmailSet());
                intersection.retainAll(dto.getAttendeeEmailSet());
                removable = new HashSet<>(meeting.getAttendeeEmailSet());
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
            meeting.setStartTime(dto.getStartTime());
            meeting.setEndTime(dto.getEndTime());
            meetingRepository.save(meeting);

            return messageService.publish(new ArrayList<>(removable), StringConstants.MEETING_REMOVED_USER, initializeModel(meeting))
                    .orElseGet(() ->
                            messageService.publish(new ArrayList<>(meeting.getEmailsNotification()), StringConstants.MEETING_UPDATED, initializeModel(meeting))
                                    .orElse(ResponseEntity.ok(meeting.entityToDTO())));
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @DeleteMapping("{meetingName}")
    @PreAuthorize("@securityService.isMeetingOwner(#meetingName)")
    public ResponseEntity deleteMeeting(@PathVariable String meetingName) {
        try {
            Meeting meeting = meetingRepository.findByName(meetingName).orElseThrow(() ->
                    new NotFoundEntityException(StringConstants.MEETING_NOT_FOUND));
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
            minuteRepository
                    .findAllByTitle(meeting.getMinuteName() == null ? "" : meeting.getMinuteName())
                    .ifPresent(minuteRepository::delete);
            meetingRepository.delete(meeting);

            return messageService.publish(new ArrayList<>(meeting.getEmailsNotification()), StringConstants.MEETING_DELETED, initializeModel(meeting))
                    .orElse(ResponseEntity.ok().build());
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
        minuteRepository.save(minutes);
        meetingRepository.save(meeting);
        try {
            return messageService.publish(new ArrayList<>(meeting.getEmailsNotification()), StringConstants.MEETING_ADD_MINUTE, initializeModel(meeting, minutes))
                    .orElse(ResponseEntity.ok(meeting.entityToDTO()));
        } catch (NotFoundEntityException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @PostMapping("{meetingName}/{minuteName}")
    @PreAuthorize("@securityService.isInsiderOnMeeting(#meetingName)")
    public ResponseEntity updateMinute(@PathVariable("meetingName") String meetingName,
                                       @PathVariable("minuteName") String minuteName,
                                       @RequestBody MinutesDTO dto) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{meetingName}/minute")
    @PreAuthorize("@securityService.isInsiderOnMeeting(#meetingName)")
    public ResponseEntity getMinute(@PathVariable("meetingName") String meetingName) {
        ResponseEntity responseEntity;
        Optional<Meeting> meeting = meetingRepository.findByName(meetingName);

        responseEntity = meeting.<ResponseEntity>map(value -> minuteRepository.findAllByTitle(value.getMinuteName())
                .map(minute -> ResponseEntity.ok().body(minute.entityToDTO()))
                .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        return responseEntity;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private User getUser() {
        return userRepository.findByEmail(authentication.getUserName()).get();
    }

    private HashMap<String, Object> initializeModel(Meeting meeting, Minutes minutes) {
        HashMap<String, Object> model = initializeModel(meeting);
        model.put("minute", minutes);
        return model;
    }

    private HashMap<String, Object> initializeModel(Meeting meeting) {
        HashMap<String, Object> model = new HashMap<>();
        model.put("meeting", meeting);
        return model;
    }
}
