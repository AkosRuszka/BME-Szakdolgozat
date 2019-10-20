package hu.bme.akosruszka.projectmanager.entity;

import hu.bme.akosruszka.projectmanager.dto.UserDTO;
import hu.bme.akosruszka.projectmanager.entity.base.BaseEntity;
import hu.bme.akosruszka.projectmanager.entity.helper.EntityMapper;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "email")
@Data
public class User extends BaseEntity {

    @NotBlank
    private String name;

    @Id
    @NotBlank
    @Email
    private String email;

    @Builder.Default
    private Set<String> projectNameSet = new HashSet<>();

    @Builder.Default
    private Set<Role> roleSet = new HashSet<>();

    @Builder.Default
    private Set<String> meetingNameSet = new HashSet<>();

    @Builder.Default
    private Set<String> taskNameSet = new HashSet<>();

    public void addProjectList(Project project) {
        projectNameSet.add(project.getName());
        project.getParticipantEmailSet().add(email);
    }

    public void addProjectListThanOwner(Project project) {
        projectNameSet.add(project.getName());
        if (project.getProjectOwnerEmail() == null || !project.getProjectOwnerEmail().equals(email)) {
            project.setProjectOwner(this);
        }
    }

    public void addMeeting(Meeting meeting) {
        meetingNameSet.add(meeting.getName());
        meeting.getAttendeeEmailSet().add(email);
    }

    public void setProjectSet(Set<Project> projects) {
        this.projectNameSet = projects.stream().map(Project::getName).collect(Collectors.toSet());
        projects.forEach(project -> {
            if (!project.getProjectOwnerEmail().equals(email))
                project.getParticipantEmailSet().add(email);
        });
    }

    public void setMeetingSet(Set<Meeting> meetings) {
        this.meetingNameSet = meetings.stream().map(Meeting::getName).collect(Collectors.toSet());
        meetings.forEach(meeting -> {
            if (!meeting.getChairPersonEmail().equals(email))
                meeting.getAttendeeEmailSet().add(email);
        });
    }

    public void setTaskSet(Set<Task> tasks) throws Exception {
        if (tasks.stream().anyMatch(task -> task.getDeveloperEmail() != null && !task.getDeveloperEmail().equals(email))) {
            throw new Exception("Task is not valid because this user not owns this task.");
        }
        this.taskNameSet = tasks.stream().map(t -> t.getInfo().getName()).collect(Collectors.toSet());
        tasks.forEach(task -> {
            if (task.getDeveloperEmail() == null || !task.getDeveloperEmail().equals(email)) {
                task.setDeveloper(this);
            }
        });
    }

    public UserDTO entityToDTO() {
        return EntityMapper.entityToDTO(this);
    }

    public static User dtoToEntity(UserDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
//                .roleSet()
                .build();
    }
}
