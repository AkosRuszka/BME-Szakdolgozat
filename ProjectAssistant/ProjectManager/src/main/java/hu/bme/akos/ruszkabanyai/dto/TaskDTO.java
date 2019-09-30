package hu.bme.akos.ruszkabanyai.dto;

import hu.bme.akos.ruszkabanyai.entity.helper.TaskDescription;
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
    private UserDTO developer;
    @NotNull
    private ProjectDTO project;
}
