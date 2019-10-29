package hu.bme.szakdolgozat.projectmanager.service;

import hu.bme.szakdolgozat.projectmanager.dao.MeetingRepository;
import hu.bme.szakdolgozat.projectmanager.dao.ProjectRepository;
import hu.bme.szakdolgozat.projectmanager.dao.TaskRepository;
import hu.bme.szakdolgozat.projectmanager.dao.UserRepository;
import hu.bme.szakdolgozat.projectmanager.dto.ProjectDTO;
import hu.bme.szakdolgozat.projectmanager.entity.Meeting;
import hu.bme.szakdolgozat.projectmanager.entity.Project;
import hu.bme.szakdolgozat.projectmanager.entity.Task;
import hu.bme.szakdolgozat.projectmanager.entity.User;
import hu.bme.szakdolgozat.projectmanager.entity.helper.EntityMapper;
import hu.bme.szakdolgozat.projectmanager.helper.FoundEntityException;
import hu.bme.szakdolgozat.projectmanager.helper.StringConstants;
import hu.bme.szakdolgozat.projectmanager.security.IAuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("project")
@PreAuthorize("hasRole('USER')")
@Slf4j
public class ProjectRestService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final MeetingRepository meetingRepository;

    private final IAuthenticationFacade authentication;

    private final MessageService messageService;

    public ProjectRestService(ProjectRepository projectRepository, TaskRepository taskRepository,
                              UserRepository userRepository, MeetingRepository meetingRepository,
                              IAuthenticationFacade authentication, MessageService messageService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
        this.taskRepository = taskRepository;
        this.authentication = authentication;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getProjects(Pageable pageable) {
        return ResponseEntity.ok(projectRepository.findAllByNameIn(new ArrayList<>(getAuthenticatedUser().getProjectNameSet()), pageable)
                .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()));
    }

    @GetMapping("{projectName}")
    @PreAuthorize("@securityService.isInsider(#projectName)")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable String projectName) {
        if (getAuthenticatedUser().getProjectNameSet().contains(projectName)) {
            return projectRepository.findByName(projectName)
                    .map(Project::entityToDto).map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ARCHITECT')")
    public ResponseEntity newProject(@RequestBody @Valid ProjectDTO dto) {
        if (projectRepository.findByName(dto.getName()).isPresent()) {
            throw new FoundEntityException(StringConstants.PROJECT_FOUND);
        }
        Project newProject = Project.builder()
                .name(dto.getName()).description(dto.getDescription()).build();
        newProject.setProjectOwner(getAuthenticatedUser());
        userRepository.save(getAuthenticatedUser());

        userRepository.findAllByEmailIn(new ArrayList<>(dto.getParticipantSet())).forEach(u -> {
            newProject.addParticipant(u);
            userRepository.save(u);
        });

        projectRepository.save(newProject);

        return messageService.publish(new ArrayList<>(newProject.getEmailsNotification()), StringConstants.PROJECT_NEW, initializeModel(newProject))
                .orElse(ResponseEntity.ok(newProject.entityToDto()));
    }

    @PutMapping("{projectName}")
    @PreAuthorize("@securityService.isOwner(#projectName)")
    public ResponseEntity updateProject(@PathVariable String projectName, @RequestBody @Valid ProjectDTO dto) {
        if (!(dto.getName().equals(projectName)) && projectRepository.findByName(dto.getName()).isPresent()) {
            log.error(String.format("Projectname : %s already exists.", dto.getName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(StringConstants.PROJECT_FOUND);
        }
        Project project = getProjectByName(projectName);
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        Set<String> intersection = new HashSet<>(project.getParticipantEmailSet());
        intersection.retainAll(dto.getParticipantSet()); // intersection contains only elements in both set
        Set<String> removableUsersName = new HashSet<>(project.getParticipantEmailSet());
        removableUsersName.removeAll(intersection);

        List<User> removableUsers = userRepository.findAllByEmailIn(new ArrayList<>(removableUsersName));
        removableUsers.forEach(user -> {
            user.getProjectNameSet().remove(projectName);
            project.getParticipantEmailSet().remove(user.getEmail());
        });
        userRepository.saveAll(removableUsers);

        List<User> userInProject = userRepository.findAllByEmailIn(new ArrayList<>(dto.getParticipantSet()));
        project.setParticipantSet(new HashSet<>(userInProject));
        userRepository.saveAll(userInProject);

        if (!projectName.equals(dto.getName())) {
            List<Task> task = taskRepository.findAllByInfoNameIn(new ArrayList<>(project.getTaskNameSet()), Pageable.unpaged());
            task.forEach(t -> t.setProject(project));
            taskRepository.saveAll(task);

            List<Meeting> meetings = meetingRepository.findAllByNameIn(new ArrayList<>(project.getMeetingMap().keySet()), Pageable.unpaged());
            meetings.forEach(m -> m.setProject(project));
            meetingRepository.saveAll(meetings);
        }

        Project savedProject = projectRepository.save(project);

        return messageService.publish(removableUsers.stream()
                .map(User::getEmail).collect(Collectors.toList()), StringConstants.PROJECT_REMOVED_USER, initializeModel(project))
                .orElseGet(() ->
                        messageService.publish(project.getEmailsNotification(), StringConstants.PROJECT_UPDATE, initializeModel(project))
                                .orElse(ResponseEntity.ok(savedProject.entityToDto())));
    }

    @DeleteMapping("{projectName}")
    @PreAuthorize("@securityService.isOwner(#projectName)")
    public ResponseEntity deleteProject(@PathVariable String projectName) {
        Project project = getProjectByName(projectName);

        List<User> users = userRepository.findAllByEmailIn(new ArrayList<>(project.getParticipantEmailSet()));
        users.forEach(user -> {
            user.getProjectNameSet().remove(project.getName());
            user.getTaskNameSet().removeAll(project.getTaskNameSet());
            user.getMeetingNameSet().removeAll(project.getMeetingMap().keySet());
        });
        userRepository.saveAll(users);

        User userOwner = userRepository.findByEmail(project.getProjectOwnerEmail()).get();
        userOwner.getProjectNameSet().remove(projectName);
        userRepository.save(userOwner);

        List<Meeting> meetings = meetingRepository.findAllByNameIn(new ArrayList<>(project.getMeetingMap().keySet()), Pageable.unpaged());
        meetingRepository.deleteAll(meetings);
        List<Task> tasks = taskRepository.findAllByInfoNameIn(new ArrayList<>(project.getTaskNameSet()), Pageable.unpaged());
        taskRepository.deleteAll(tasks);

        projectRepository.delete(project);
        return messageService.publish(users.stream().map(User::getEmail).collect(Collectors.toList()), StringConstants.PROJECT_DELETED, initializeModel(project))
                .orElse(ResponseEntity.ok().build());
    }

    @GetMapping("{projectName}/tasks")
    @PreAuthorize("@securityService.isInsider(#projectName)")
    public ResponseEntity getTasksForProject(@PathVariable String projectName) {
        Project project = getProjectByName(projectName);
        List<Task> tasks = taskRepository.findAllByInfoNameIn(new ArrayList<>(project.getTaskNameSet()), Pageable.unpaged());
        return ResponseEntity.ok(tasks.stream().map(EntityMapper::entityToDtoOtherCall));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Project getProjectByName(String projectName) {
        return projectRepository.findByName(projectName).get();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private User getAuthenticatedUser() {
        return userRepository.findByEmail(authentication.getUserName()).get();
    }

    private Map<String, Object> initializeModel(Project project) {
        HashMap<String, Object> model = new HashMap<>();
        model.put("project", project);
        return model;
    }
}
