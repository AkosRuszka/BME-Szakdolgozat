package hu.bme.akosruszka.projectmanager.security;

import hu.bme.akosruszka.projectmanager.dao.MeetingRepository;
import hu.bme.akosruszka.projectmanager.dao.ProjectRepository;
import hu.bme.akosruszka.projectmanager.dao.TaskRepository;
import hu.bme.akosruszka.projectmanager.entity.User;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Service
public class SecurityService {

    private final IAuthenticationFacade auth;

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    private final MeetingRepository meetingRepository;

    public SecurityService(IAuthenticationFacade auth,
                           ProjectRepository projectRepository, TaskRepository taskRepository,
                           MeetingRepository meetingRepository) {
        this.auth = auth;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.meetingRepository = meetingRepository;
    }

    public boolean isOwner(@NotBlank String projectName) {
        return projectRepository.findByName(projectName)
                .map(project -> project.getProjectOwnerEmail().equals(getUser().getEmail())).orElse(false);
    }

    public boolean isDeveloper(@NotBlank String projectName) {
        return projectRepository.findByName(projectName)
                .map(project -> project.getParticipantEmailSet().contains(getUser().getEmail())).orElse(false);
    }

    public boolean isInsider(@NotBlank String projectName) {
        return isDeveloper(projectName) || isOwner(projectName);
    }

    public boolean isInsiderTask(@NotBlank String taskName) {
        return taskRepository.findByTaskName(taskName).map(t -> isInsider(t.getProjectName())).orElse(false);
    }

    public boolean isInsiderOnTask(@NotBlank String taskName) {
        return taskRepository.findByTaskName(taskName).filter(task ->
                isOwner(task.getProjectName()) || ((isDeveloper(task.getProjectName()) &&
                        (task.getDeveloperEmail() == null || task.getDeveloperEmail().equals(getUser().getEmail())))))
                .isPresent();
    }

    public boolean isInsiderOnMeeting(@NotBlank String meetingName) {
        return meetingRepository.findByName(meetingName).filter(meeting ->
                meeting.getChairPersonEmail().equals(getUser().getEmail()))
                .isPresent();
    }

    private User getUser() {
        return ((UserPrincipal) auth.getAuthentication().getPrincipal()).getUser();
    }
}
