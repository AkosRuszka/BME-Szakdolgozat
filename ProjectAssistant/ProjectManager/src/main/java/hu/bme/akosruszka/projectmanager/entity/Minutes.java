package hu.bme.akosruszka.projectmanager.entity;

import hu.bme.akosruszka.projectmanager.dto.MinutesDTO;
import hu.bme.akosruszka.projectmanager.entity.base.BaseEntity;
import hu.bme.akosruszka.projectmanager.entity.helper.EntityMapper;
import hu.bme.akosruszka.projectmanager.entity.helper.TaskDescription;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@Document
@EqualsAndHashCode(callSuper = false, of = "title")
@AllArgsConstructor
@NoArgsConstructor
public class Minutes extends BaseEntity {
    @Id
    private String title;

    @NotNull
    private String meetingName;

    @Builder.Default
    private Set<String> absentEmailSet = new HashSet<>();

    @Builder.Default
    private Set<TaskDescription> taskSet = new HashSet<>();

    public void setMeeting(Meeting meeting) {
        this.meetingName = meeting.getName();
        if (meeting.getMinuteName() == null) {
            meeting.setMinute(this);
        }
    }

    public void setAbsentNameSet(Set<User> users) {
        this.absentEmailSet = users.stream().map(User::getEmail).collect(Collectors.toSet());
    }

    public MinutesDTO entityToDTO() {
        return EntityMapper.entityToDTO(this);
    }
}
