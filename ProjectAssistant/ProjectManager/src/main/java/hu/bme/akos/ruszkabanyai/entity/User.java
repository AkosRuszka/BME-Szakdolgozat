package hu.bme.akos.ruszkabanyai.entity;

import hu.bme.akos.ruszkabanyai.dto.UserDTO;
import hu.bme.akos.ruszkabanyai.entity.helper.EntityMapper;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "email")
@Data
public class User {

    @NotBlank
    private String name;

    @Id
    @NotBlank
    @Email
    private String email;

    @ManyToMany(mappedBy = "participantList", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Project> participanProjectList = new ArrayList<>();

    @OneToMany(mappedBy = "projectOwner", fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Project> ownProjectList = new HashSet<>();

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @Builder.Default
    private Set<Role> roleList = new HashSet<>();

    @OneToMany(mappedBy = "chairPerson", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Meeting> ownMeetingList = new ArrayList<>();

    @ManyToMany(mappedBy = "attendeeList", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Meeting> meetingList = new ArrayList<>();

    @OneToMany(mappedBy = "developer", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Task> taskList = new ArrayList<>();

    public List<Project> getAllProjects() {
        return Stream.concat(participanProjectList.stream(), ownProjectList.stream()).collect(Collectors.toList());
    }

    public void addOwnProject(Project project) {
        ownProjectList.add(project);
        project.setProjectOwner(this);
    }

    public UserDTO entityToDTO() {
        return EntityMapper.entityToDTO(this);
    }

    public static User dtoToEntity(UserDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
//                .roleList()
                .build();
    }
}
