package hu.bme.akos.ruszkabanyai.entity;

import hu.bme.akos.ruszkabanyai.dto.MinutesDTO;
import hu.bme.akos.ruszkabanyai.entity.helper.EntityMapper;
import hu.bme.akos.ruszkabanyai.entity.helper.TaskDescription;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Entity
@EqualsAndHashCode(callSuper = false, of = "hashMarkId")
@AllArgsConstructor
@NoArgsConstructor
public class Minutes {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer hashMarkId;

    @NotNull
    @OneToOne(mappedBy = "minute")
    private Meeting meeting;

    @ManyToMany
    @JoinTable(name = "absent_minutes_user",
            joinColumns = @JoinColumn(name = "minutes_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> absentList; /* hiányzók */

    @ElementCollection
    @CollectionTable(name = "minute_taskdescription", joinColumns = @JoinColumn(name = "minute_id"))
    private List<TaskDescription> taskList; /* milyen elvégzendő feladat van */

    public MinutesDTO entityToDTO() {
        return EntityMapper.entityToDTO(this);
    }
}
