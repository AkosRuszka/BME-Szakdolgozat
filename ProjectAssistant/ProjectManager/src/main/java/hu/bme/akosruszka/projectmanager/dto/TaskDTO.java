package hu.bme.akosruszka.projectmanager.dto;

import hu.bme.akosruszka.projectmanager.entity.helper.TaskDescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class TaskDTO {
    @NotNull
    private TaskDescription info;
    private String developerEmail;
    @NotNull
    private String projectName;
}
