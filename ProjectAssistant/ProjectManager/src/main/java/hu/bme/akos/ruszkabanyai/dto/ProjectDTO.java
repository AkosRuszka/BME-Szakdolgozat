package hu.bme.akos.ruszkabanyai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private List<MeetingDTO> meetingList;
    private List<UserDTO> participantList;
    @NotNull
    @Valid
    private UserDTO owner;
    private List<TaskDTO> taskList;
}
