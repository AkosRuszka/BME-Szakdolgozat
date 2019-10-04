package hu.bme.akos.ruszkabanyai.service;

import hu.bme.akos.ruszkabanyai.dao.interfaces.ProjectRepository;
import hu.bme.akos.ruszkabanyai.dao.interfaces.UserRepository;
import hu.bme.akos.ruszkabanyai.dto.ProjectDTO;
import hu.bme.akos.ruszkabanyai.dto.UserDTO;
import hu.bme.akos.ruszkabanyai.entity.Project;
import hu.bme.akos.ruszkabanyai.entity.User;
import hu.bme.akos.ruszkabanyai.entity.helper.EntityMapper;
import hu.bme.akos.ruszkabanyai.security.IAuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("project")
@PreAuthorize("hasRole('USER')")
@Slf4j
public class ProjectRestService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final IAuthenticationFacade authentication;

    public ProjectRestService(ProjectRepository projectRepository,
                              UserRepository userRepository, IAuthenticationFacade authentication) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.authentication = authentication;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return ResponseEntity.ok(getAuthenticatedUser().getAllProjects().stream()
                .map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()));
    }

    @GetMapping("{projectName}")
    @PreAuthorize("@securityService.isInsider(#projectName)")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable String projectName) {
        Optional<ProjectDTO> result = getAuthenticatedUser().getAllProjects().stream()
                .filter(p -> p.getName().equals(projectName))
                .map(Project::entityToDto)
                .findFirst();

        return result.isPresent() ?
                ResponseEntity.ok(result.get()) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ARCHITECT')")
    public ResponseEntity<ProjectDTO> newProject(@RequestBody @Valid ProjectDTO dto) {
        Project newProject = Project.builder()
                .name(dto.getName()).description(dto.getDescription()).build();
        newProject.setProjectOwner(getAuthenticatedUser());

        userRepository.findAllByNameIn(
                dto.getParticipantList()
                        .stream().map(UserDTO::getName)
                        .collect(Collectors.toList())
        ).forEach(u -> {
            u.getParticipanProjectList().add(newProject);
            newProject.addParticipant(u);
            userRepository.save(u);
        });

        return ResponseEntity.ok(projectRepository.save(newProject).entityToDto());
    }

    @PutMapping("{projectName}")
    @PreAuthorize("@securityService.isOwner(#projectName)")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable String projectName, @RequestBody @Valid ProjectDTO dto) {
        if (!(dto.getName().equals(projectName)) && projectRepository.findByName(dto.getName()).isPresent()) {
            log.error(String.format("Projectname : %s already exists.", dto.getName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Project project = getProjectByName(projectName);
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        List<User> notRemovableUsers = userRepository.findAllByNameIn(dto.getParticipantList().stream()
                .map(UserDTO::getName).collect(Collectors.toList()));
        List<User> removableUsers = project.getParticipantList().stream()
                .filter(u -> !(notRemovableUsers.contains(u))).collect(Collectors.toList());
        project.getParticipantList().removeAll(removableUsers);

        return ResponseEntity.ok(projectRepository.save(project).entityToDto());
    }

    @DeleteMapping("{projectName}")
    @PreAuthorize("@securityService.isOwner(#projectName)")
    public ResponseEntity deleteProject(@PathVariable String projectName) {
        Project project = getProjectByName(projectName);
        projectRepository.delete(project);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{projectName}/tasks")
    @PreAuthorize("@securityService.isInsider(#projectName)")
    public ResponseEntity getTasksForProject(@PathVariable String projectName) {
        Project project = getProjectByName(projectName);
        return ResponseEntity.ok(project.getTaskList().stream().map(EntityMapper::entityToDtoOtherCall));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Project getProjectByName(String projectName) {
        return projectRepository.findByName(projectName).get();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private User getAuthenticatedUser() {
        return userRepository.findByEmail(authentication.getUserName()).get();
    }
}
