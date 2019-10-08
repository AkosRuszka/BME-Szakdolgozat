package hu.bme.akos.ruszkabanyai.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.bme.akos.ruszkabanyai.dto.MeetingDTO;
import hu.bme.akos.ruszkabanyai.entity.base.BaseEntity;
import hu.bme.akos.ruszkabanyai.entity.helper.EntityMapper;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "name")
public class Meeting extends BaseEntity {
    @Id
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String projectName;

    @NotBlank
    private String location;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date date;

    @NotBlank
    private String minuteName;

    @NotBlank
    private String chairPersonEmail;

    @Builder.Default
    private Set<String> attendeeEmailSet = new HashSet<>();

    public void setProject(Project project) {
        this.projectName = project.getName();
        project.getMeetingNameSet().add(this.name);
    }

    public void setMinute(Minutes minute) {
        this.minuteName = minute.getTitle();
        if (minute.getMeetingName() == null) {
            minute.setMeetingName(name);
        }
    }

    public void setChairPerson(User user) {
        this.chairPersonEmail = user.getEmail();
        user.getMeetingNameSet().add(this.getName());
    }

    public void setAttendeeSet(Set<User> users) {
        this.attendeeEmailSet = users.stream().map(User::getEmail).collect(Collectors.toSet());
        users.forEach(user -> user.addMeeting(this));
    }

    public MeetingDTO entityToDTO() {
        return EntityMapper.entityToDTO(this);
    }

    public static Meeting dtoToEntity(MeetingDTO dto) {
        return Meeting.builder()
                .name(dto.getName()).description(dto.getDescription())
                .projectName(dto.getProjectName()).location(dto.getLocation())
                .date(dto.getDate()).minuteName(dto.getMinuteName())
                .chairPersonEmail(dto.getChairPersonEmail()).attendeeEmailSet(dto.getAttendeeEmailSet())
                .build();
    }
}
