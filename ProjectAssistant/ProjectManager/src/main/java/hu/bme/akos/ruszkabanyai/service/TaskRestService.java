package hu.bme.akos.ruszkabanyai.service;

import hu.bme.akos.ruszkabanyai.dao.ProjectRepository;
import hu.bme.akos.ruszkabanyai.dao.TaskRepository;
import hu.bme.akos.ruszkabanyai.dao.UserRepository;
import hu.bme.akos.ruszkabanyai.dto.TaskDTO;
import hu.bme.akos.ruszkabanyai.entity.Project;
import hu.bme.akos.ruszkabanyai.entity.Task;
import hu.bme.akos.ruszkabanyai.entity.User;
import hu.bme.akos.ruszkabanyai.security.IAuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("task")
@PreAuthorize("hasAnyRole('USER')")
@Slf4j
public class TaskRestService {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    private final IAuthenticationFacade authentication;

    private final UserRepository userRepository;

    public TaskRestService(TaskRepository taskRepository, ProjectRepository projectRepository,
                           IAuthenticationFacade authentication, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.authentication = authentication;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity getTasks() {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        User user = userRepository.findByName(authentication.getUserName()).get();
        return ResponseEntity.ok(user.getTaskList().stream().map(Task::entityToDTO).collect(Collectors.toList()));
    }

    @GetMapping("{taskName}")
    public ResponseEntity getTask(@PathVariable String taskName) {
        TaskDTO dto = taskRepository.findByInfoName(taskName).map(Task::entityToDTO).orElse(null);
        return dto == null ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("@securityService.isInsider(#dto.project.getName())")
    public ResponseEntity newTaskForProject(@RequestBody @Valid TaskDTO dto) throws Exception {
        User developer = null;
        if (dto.getDeveloper() != null) {
            developer = userRepository.findByName(dto.getDeveloper().getName()).orElse(null);
        }
        Project project = projectRepository.findByName(dto.getProject().getName())
                .orElseThrow(() -> new Exception("Fail állapot"));
        Task task = Task.builder()
                .info(dto.getInfo())
                .developer(developer)
                .project(project)
                .build();
        return ResponseEntity.ok(taskRepository.save(task).entityToDTO());
    }

    @PutMapping
    @PreAuthorize("@securityService.isInsiderOnTask(#dto.getInfo().getName())")
    public ResponseEntity updateTask(@RequestBody @Valid TaskDTO dto) {
        Task task = taskRepository.findByInfoName(dto.getInfo().getName()).get();
        User developer = null;
        if (dto.getDeveloper() != null) {
            developer = userRepository.findByName(dto.getDeveloper().getName()).orElse(null);
        }
        if (!task.getInfo().getName().equals(dto.getInfo().getName()) &&
                taskRepository.findByInfoName(dto.getInfo().getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.FOUND).body("Már létezik ilyen névvel feladat!");
        }
        task.setInfo(dto.getInfo());
        task.setDeveloper(developer);
        return ResponseEntity.ok(taskRepository.save(task).entityToDTO());
    }

    @DeleteMapping("{taskName}")
    @PreAuthorize("@securityService.isInsiderOnTask(#taskName)")
    public ResponseEntity deleteTask(@PathVariable String taskName) {
        return taskRepository.findByInfoName(taskName).map(task -> {
            taskRepository.delete(task);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
