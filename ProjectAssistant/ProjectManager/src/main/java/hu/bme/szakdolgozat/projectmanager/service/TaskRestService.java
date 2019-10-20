package hu.bme.szakdolgozat.projectmanager.service;

import hu.bme.akos.ruszkabanyai.dao.ProjectRepository;
import hu.bme.akos.ruszkabanyai.dao.TaskRepository;
import hu.bme.szakdolgozat.projectmanager.dao.UserRepository;
import hu.bme.szakdolgozat.projectmanager.dto.TaskDTO;
import hu.bme.szakdolgozat.projectmanager.entity.Project;
import hu.bme.szakdolgozat.projectmanager.entity.Task;
import hu.bme.szakdolgozat.projectmanager.entity.User;
import hu.bme.szakdolgozat.projectmanager.helper.NotFoundEntityException;
import hu.bme.szakdolgozat.projectmanager.security.IAuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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

    private final MessageService messageService;

    public TaskRestService(TaskRepository taskRepository, ProjectRepository projectRepository,
                           IAuthenticationFacade authentication, UserRepository userRepository, MessageService messageService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.authentication = authentication;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity getTasks(Pageable pageable) {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        User user = userRepository.findByEmail(authentication.getUserName()).get();
        List<Task> tasks = taskRepository.findAllByInfoNameIn(new ArrayList<>(user.getTaskNameSet()), pageable);
        return ResponseEntity.ok(tasks.stream().map(Task::entityToDTO).collect(Collectors.toList()));
    }

    @GetMapping("{taskName}")
    @PreAuthorize("@securityService.isInsiderTask(#taskName)")
    public ResponseEntity getTask(@PathVariable String taskName) {
        return taskRepository.findByTaskName(taskName)
                .map(t -> ResponseEntity.ok(t.entityToDTO())).orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PostMapping
    @PreAuthorize("@securityService.isInsider(#dto.projectName)")
    public ResponseEntity newTaskForProject(@RequestBody @Valid TaskDTO dto) {
        User developer = null;
        if (dto.getDeveloperEmail() != null) {
            developer = userRepository.findByEmail(dto.getDeveloperEmail()).orElse(null);
        }
        try {
            Project project = projectRepository.findByName(dto.getProjectName())
                    .orElseThrow(() -> new NotFoundEntityException("Nem található a projekt"));
            Task task = Task.builder()
                    .info(dto.getInfo())
                    .projectName(project.getName())
                    .build();
            task.setTaskName(dto.getInfo().getName());
            task.setDeveloper(developer);
            userRepository.save(developer);
            return ResponseEntity.ok(taskRepository.save(task).entityToDTO());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @PutMapping("{taskName}")
    @PreAuthorize("@securityService.isInsiderOnTask(#taskName)")
    public ResponseEntity updateTask(@PathVariable String taskName, @RequestBody @Valid TaskDTO dto) {
        Task task = taskRepository.findByTaskName(taskName).get();
        User developer = null;
        if (dto.getDeveloperEmail() != null) {
            developer = userRepository.findByEmail(dto.getDeveloperEmail()).orElse(null);
        }
        if (!taskName.equals(dto.getInfo().getName()) &&
                taskRepository.findByTaskName(dto.getInfo().getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.FOUND).body("Már létezik ilyen névvel feladat!");
        }
        task.setInfo(dto.getInfo());
        task.setTaskName(dto.getInfo().getName());
        task.setDeveloper(developer);

        if (!taskName.equals(dto.getInfo().getName())) {
            Project project = projectRepository.findByName(task.getProjectName()).get();
            project.getTaskNameSet().remove(taskName);
            project.getTaskNameSet().add(dto.getInfo().getName());
            projectRepository.save(project);
            if (developer != null)
                developer.getTaskNameSet().remove(taskName);
        }

        userRepository.save(developer);
        return ResponseEntity.ok(taskRepository.save(task).entityToDTO());
    }

    @DeleteMapping("{taskName}")
    @PreAuthorize("@securityService.isInsiderOnTask(#taskName)")
    public ResponseEntity deleteTask(@PathVariable String taskName) {
        return taskRepository.findByTaskName(taskName).map(task -> {
            taskRepository.delete(task);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
