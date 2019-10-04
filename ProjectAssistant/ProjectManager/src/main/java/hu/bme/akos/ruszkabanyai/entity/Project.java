package hu.bme.akos.ruszkabanyai.entity;

import hu.bme.akos.ruszkabanyai.dto.ProjectDTO;
import hu.bme.akos.ruszkabanyai.entity.helper.EntityMapper;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@Entity
@EqualsAndHashCode(callSuper = false, of = "name")
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    @Builder.Default
    private List<Meeting> meetingList = new ArrayList<>();

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    @Builder.Default
    private List<User> participantList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private User projectOwner;

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    @Builder.Default
    private List<Task> taskList = new ArrayList<>();

    public void addParticipant(User user) {
        participantList.add(user);
    }

    public ProjectDTO entityToDto() {
        return EntityMapper.entityToDTO(this);
    }

    public static Project dtoToEntity(ProjectDTO dto) {
        return Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .meetingList(dto.getMeetingList().stream().map(Meeting::dtoToEntity).collect(Collectors.toList()))
                .participantList(dto.getParticipantList().stream().map(User::dtoToEntity).collect(Collectors.toList()))
                .projectOwner(User.dtoToEntity(dto.getOwner()))
                .taskList(dto.getTaskList().stream().map(Task::dtoToEntity).collect(Collectors.toList()))
                .build();
    }

    public void setProjectOwner(User user) {
        projectOwner = user;
        user.getOwnProjectList().add(this);
    }
}
