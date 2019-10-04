package hu.bme.akos.ruszkabanyai.security;

import hu.bme.akos.ruszkabanyai.dao.interfaces.ProjectRepository;
import hu.bme.akos.ruszkabanyai.dao.interfaces.TaskRepository;
import hu.bme.akos.ruszkabanyai.entity.User;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Service
public class SecurityService {

    private final IAuthenticationFacade auth;

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    public SecurityService(IAuthenticationFacade auth,
                           ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.auth = auth;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public boolean isOwner(@NotBlank String projectName) {
        return projectRepository.findByName(projectName)
                .map(project -> project.getProjectOwner().equals(getUser())).orElse(false);
    }

    public boolean isDeveloper(@NotBlank String projectName) {
        return projectRepository.findByName(projectName)
                .map(project -> project.getParticipantList().contains(getUser())).orElse(false);
    }

    public boolean isInsider(@NotBlank String projectName) {
        return isDeveloper(projectName) || isOwner(projectName);
    }

    public boolean isInsiderOnTask(@NotBlank String taskName) {
        return taskRepository.findByInfoName(taskName).filter(task ->
                isOwner(task.getProject().getName()) || ((isDeveloper(task.getProject().getName()) &&
                        (task.getDeveloper() == null || task.getDeveloper().equals(getUser()))))
        ).isPresent();
    }

    private User getUser() {
        return ((UserPrincipal) auth.getAuthentication().getPrincipal()).getUser();
    }
}
