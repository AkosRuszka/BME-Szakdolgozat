package hu.bme.akos.ruszkabanyai.entity;

import hu.bme.akos.ruszkabanyai.dto.TaskDTO;
import hu.bme.akos.ruszkabanyai.entity.helper.EntityMapper;
import hu.bme.akos.ruszkabanyai.entity.helper.TaskDescription;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@EqualsAndHashCode(callSuper = false, of = "info")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @NotNull
    @Embedded
    public TaskDescription info;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User developer;

    public TaskDTO entityToDTO() {
        return EntityMapper.entityToDTO(this);
    }

    public static Task dtoToEntity(TaskDTO dto) {
        return Task.builder()
                .info(dto.getInfo())
                .developer(User.dtoToEntity(dto.getDeveloper()))
                .project(Project.dtoToEntity(dto.getProject()))
                .build();
    }
}
