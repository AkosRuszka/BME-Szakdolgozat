package hu.bme.akos.ruszkabanyai.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.bme.akos.ruszkabanyai.dto.MeetingDTO;
import hu.bme.akos.ruszkabanyai.entity.base.BaseEntity;
import hu.bme.akos.ruszkabanyai.entity.helper.EntityMapper;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "name")
public class Meeting extends BaseEntity {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @NotBlank
    private String location;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date date;

    @OneToOne
    @JoinColumn(name = "minute_id")
    private Minutes minute;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User chairPerson;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "meeting_attendee_person",
            joinColumns = @JoinColumn(name = "fk_meeting"), inverseJoinColumns = @JoinColumn(name = "fk_user"))
    private List<User> attendeeList;

    public MeetingDTO entityToDTO() {
        return EntityMapper.entityToDTO(this);
    }

    public static Meeting dtoToEntity(MeetingDTO dto) {
        return null;
    }
}
