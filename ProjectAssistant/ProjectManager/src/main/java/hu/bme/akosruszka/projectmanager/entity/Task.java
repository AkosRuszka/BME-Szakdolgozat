package hu.bme.akosruszka.projectmanager.entity;

import hu.bme.akosruszka.projectmanager.dto.TaskDTO;
import hu.bme.akosruszka.projectmanager.entity.base.BaseEntity;
import hu.bme.akosruszka.projectmanager.entity.helper.EntityMapper;
import hu.bme.akosruszka.projectmanager.entity.helper.TaskDescription;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Document
@Builder
@EqualsAndHashCode(callSuper = false, of = "info")
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity {
    @Id
    @NotBlank
    public String taskName;

    @NotNull
    public TaskDescription info;

    private String projectName;

    public String developerEmail;

    public void setProject(Project project) {
        this.projectName = project.getName();
        project.getTaskNameSet().add(info.name);
    }

    public void setDeveloper(User user) {
        if (user != null) {
            this.developerEmail = user.getEmail();
            user.getTaskNameSet().add(info.name);
        }
    }

    public TaskDTO entityToDTO() {
        return EntityMapper.entityToDTO(this);
    }

    public static Task dtoToEntity(TaskDTO dto) {
        return Task.builder()
                .info(dto.getInfo())
                .developerEmail(dto.getDeveloperEmail())
                .projectName(dto.getProjectName())
                .build();
    }
}
