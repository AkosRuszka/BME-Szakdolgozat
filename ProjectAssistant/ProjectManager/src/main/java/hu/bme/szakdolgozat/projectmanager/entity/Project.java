package hu.bme.szakdolgozat.projectmanager.entity;

import hu.bme.szakdolgozat.projectmanager.dto.ProjectDTO;
import hu.bme.szakdolgozat.projectmanager.entity.base.BaseEntity;
import hu.bme.szakdolgozat.projectmanager.entity.base.UpdateNotifier;
import hu.bme.szakdolgozat.projectmanager.entity.helper.EntityMapper;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@Document
@EqualsAndHashCode(callSuper = false, of = "name")
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity implements UpdateNotifier {
    @Id
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Builder.Default
    private Boolean active = Boolean.TRUE;

    @Builder.Default
    private Map<String, LocalDateTime> meetingMap = new HashMap<>();

    @Builder.Default
    private Set<String> participantEmailSet = new HashSet<>();

    private String projectOwnerEmail;

    @Builder.Default
    private Set<String> taskNameSet = new HashSet<>();

    public void setProjectOwner(User user) {
        this.projectOwnerEmail = user.getEmail();
        user.addProjectListThanOwner(this);
    }

    public void setMeetingMap(Set<Meeting> meetings) {
        this.meetingMap = meetings.stream().collect(Collectors.toMap(Meeting::getName, Meeting::getDate));
        meetings.forEach(meeting -> {
            if (meeting.getProjectName() == null) meeting.setProject(this);
        });
    }

    public void setParticipantSet(Set<User> participantSet) {
        this.participantEmailSet = participantSet.stream().map(User::getEmail).collect(Collectors.toSet());
        participantSet.forEach(user -> user.addProjectList(this));
    }

    public void addParticipant(User user) {
        this.participantEmailSet.add(user.getEmail());
        user.addProjectList(this);
    }

    public void setTaskSet(Set<Task> tasks) {
        this.taskNameSet = tasks.stream().map(t -> t.getInfo().getName()).collect(Collectors.toSet());
        tasks.forEach(task -> {
            if (task.getProjectName() == null) task.setProject(this);
        });
    }

    public ProjectDTO entityToDto() {
        return EntityMapper.entityToDTO(this);
    }

    public static Project dtoToEntity(ProjectDTO dto) {
        return Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .meetingMap(dto.getMeetingMap())
                .participantEmailSet(dto.getParticipantSet())
                .projectOwnerEmail(dto.getOwnerName())
                .taskNameSet(dto.getTaskSet())
                .build();
    }

    @Override
    public List<String> getEmailsNotification() {
        ArrayList<String> list = new ArrayList<>(participantEmailSet);
        list.add(projectOwnerEmail);
        return list;
    }
}